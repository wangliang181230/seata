/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.rm.datasource.exec.postgresql;

import io.seata.common.exception.ShouldNeverHappenException;
import io.seata.common.loader.LoadLevel;
import io.seata.common.loader.Scope;
import io.seata.common.util.StringUtils;
import io.seata.rm.datasource.StatementProxy;
import io.seata.rm.datasource.exec.BaseInsertExecutor;
import io.seata.rm.datasource.exec.StatementCallback;
import io.seata.rm.datasource.sql.struct.ColumnMeta;
import io.seata.sqlparser.SQLRecognizer;
import io.seata.sqlparser.struct.Defaultable;
import io.seata.sqlparser.struct.Sequenceable;
import io.seata.sqlparser.struct.SqlMethodExpr;
import io.seata.sqlparser.struct.SqlSequenceExpr;
import io.seata.sqlparser.struct.SqlDefaultExpr;
import io.seata.sqlparser.util.JdbcConstants;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The type Postgresql insert executor.
 *
 * @author jsbxyyx
 */
@LoadLevel(name = JdbcConstants.POSTGRESQL, scope = Scope.PROTOTYPE)
public class PostgresqlInsertExecutor extends BaseInsertExecutor implements Sequenceable, Defaultable {

    /**
     * Instantiates a new Abstract dml base executor.
     *
     * @param statementProxy    the statement proxy
     * @param statementCallback the statement callback
     * @param sqlRecognizer     the sql recognizer
     */
    public PostgresqlInsertExecutor(StatementProxy statementProxy, StatementCallback statementCallback,
                                    SQLRecognizer sqlRecognizer) {
        super(statementProxy, statementCallback, sqlRecognizer);
    }

    @Override
    public Map<String,List<Object>> getPkValues() throws SQLException {
        Map<String,List<Object>> pkValuesMap = null;
        Boolean isContainsPk = containsPK();
        //when there is only one pk in the table
        if (isContainsPk) {
            pkValuesMap = getPkValuesByColumn();
        }
        else if (containsColumns()) {
            String columnName = getTableMeta().getPrimaryKeyOnlyName().get(0);
            pkValuesMap = Collections.singletonMap(columnName, getGeneratedKeys());
        }
        else {
            pkValuesMap = getPkValuesByColumn();
        }
        return pkValuesMap;
    }

    @Override
    public Map<String,List<Object>> getPkValuesByColumn() throws SQLException {
        Map<String,List<Object>> pkValuesMap = parsePkValuesFromStatement();
        String pkKey = pkValuesMap.keySet().iterator().next();
        List<Object> pkValues = pkValuesMap.get(pkKey);
        if (!pkValues.isEmpty() && pkValues.get(0) instanceof SqlSequenceExpr) {
            pkValuesMap.put(pkKey,getPkValuesBySequence((SqlSequenceExpr) pkValues.get(0)));
        } else if (!pkValues.isEmpty() && pkValues.get(0) instanceof SqlMethodExpr) {
            pkValuesMap.put(pkKey,getGeneratedKeys());
        } else if (!pkValues.isEmpty() && pkValues.get(0) instanceof SqlDefaultExpr) {
            pkValuesMap.put(pkKey,getPkValuesByDefault());
        }

        return pkValuesMap;
    }

    /**
     * get primary key values by default
     * @return
     * @throws SQLException
     */
    @Override
    public List<Object> getPkValuesByDefault() throws SQLException {
        // current version 1.2 only support postgresql.
        Map<String, ColumnMeta> pkMetaMap = getTableMeta().getPrimaryKeyMap();
        ColumnMeta pkMeta = pkMetaMap.values().iterator().next();
        String columnDef = pkMeta.getColumnDef();
        // sample: nextval('test_id_seq'::regclass)
        String seq = org.apache.commons.lang.StringUtils.substringBetween(columnDef, "'", "'");
        String function = org.apache.commons.lang.StringUtils.substringBetween(columnDef, "", "(");
        if (StringUtils.isBlank(seq)) {
            throw new ShouldNeverHappenException("get primary key value failed, cause columnDef is " + columnDef);
        }
        return getPkValuesBySequence(new SqlSequenceExpr("'" + seq + "'", function));
    }

    @Override
    public String getSequenceSql(SqlSequenceExpr expr) {
        return "SELECT currval(" + expr.getSequence() + ")";
    }
}
