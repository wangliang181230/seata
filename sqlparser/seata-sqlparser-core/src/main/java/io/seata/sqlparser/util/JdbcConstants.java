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
package io.seata.sqlparser.util;

/**
 * @author ggndnn
 */
public interface JdbcConstants {
    String ORACLE = "oracle";
    String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
    String ORACLE_DRIVER2 = "oracle.jdbc.driver.OracleDriver";

    String MYSQL = "mysql";
    String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    String MYSQL_DRIVER_6 = "com.mysql.cj.jdbc.Driver";

    String DB2 = "db2";
    String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver"; // Type4
    String DB2_DRIVER2 = "COM.ibm.db2.jdbc.app.DB2Driver"; // Type2
    String DB2_DRIVER3 = "COM.ibm.db2.jdbc.net.DB2Driver"; // Type3

    String H2 = "h2";
    String H2_DRIVER = "org.h2.Driver";

    String MARIADB = "mariadb";
    String MARIADB_DRIVER = "org.mariadb.jdbc.Driver";

    String POSTGRESQL = "postgresql";
    String POSTGRESQL_DRIVER = "org.postgresql.Driver";
}
