package org.prettyjdbc.core.transaction;

/**
 * Enumeration of statuses in which a transaction facade ({@link org.prettyjdbc.core.transaction.Transaction}) might be.
 *
 * @author Oleg Marchenko
 */

public enum TransactionStatus {
    /**
     * The transaction has not yet been started.
     */
    NOT_ACTIVE,
    /**
     * The transaction has been started, but not yet completed.
     */
    ACTIVE,
    /**
     * The transaction has been completed successfully.
     */
    COMPLETED
}