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
package io.seata.server.session;

import io.seata.core.exception.TransactionException;
import io.seata.core.model.BranchStatus;
import io.seata.core.model.GlobalStatus;
import io.seata.core.model.GlobalStoppedReason;

/**
 * The interface Session lifecycle.
 *
 * @author sharajava
 */
public interface SessionLifecycle {

    /**
     * Begin.
     *
     * @throws TransactionException the transaction exception
     */
    void begin() throws TransactionException;

    /**
     * Update.
     *
     * @param status the status
     * @param suspendedEndTime the suspended end time
     * @param stoppedReason the stopped reason
     */
    void update(GlobalStatus status, long suspendedEndTime, GlobalStoppedReason stoppedReason) throws TransactionException;

    /**
     * Change status.
     *
     * @param status the status
     * @throws TransactionException the transaction exception
     */
    default void changeStatus(GlobalStatus status) throws TransactionException {
        this.update(status, -1L, null);
    }

    /**
     * Update branch.
     *
     * @param branchSession   the branch session
     * @param status          the status
     * @param applicationData the application data
     * @param retryCount      the retry count
     * @throws TransactionException
     */
    void updateBranch(BranchSession branchSession, BranchStatus status,
                      String applicationData, int retryCount) throws TransactionException;

    /**
     * Change branch status.
     *
     * @param branchSession the branch session
     * @param status        the status
     * @throws TransactionException the transaction exception
     */
    default void changeBranchStatus(BranchSession branchSession, BranchStatus status) throws TransactionException {
        updateBranch(branchSession, status, null, -1);
    }

    /**
     * Add branch.
     *
     * @param branchSession the branch session
     * @throws TransactionException the transaction exception
     */
    void addBranch(BranchSession branchSession) throws TransactionException;

    /**
     * Remove branch.
     *
     * @param branchSession the branch session
     * @throws TransactionException the transaction exception
     */
    void removeBranch(BranchSession branchSession) throws TransactionException;

    /**
     * Is active boolean.
     *
     * @return the boolean
     */
    boolean isActive();

    /**
     * Close.
     *
     * @throws TransactionException the transaction exception
     */
    void close() throws TransactionException;

    /**
     * End.
     *
     * @throws TransactionException the transaction exception
     */
    void end() throws TransactionException;
}
