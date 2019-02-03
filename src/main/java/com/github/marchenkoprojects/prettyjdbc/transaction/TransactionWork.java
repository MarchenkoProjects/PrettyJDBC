package com.github.marchenkoprojects.prettyjdbc.transaction;

import com.github.marchenkoprojects.prettyjdbc.session.Session;

/**
 * This interface describes a contract for the execution of a separate part of transactional working in a database.
 *
 * @author Oleg Marchenko
 *
 * @see Session#doInTransaction(TransactionWork)
 */
@FunctionalInterface
public interface TransactionWork {

    /**
     * Perform discrete transactional work encapsulated by this instance using the provided {@link Session}.
     *
     * @param session the session in which to do the transactional work
     */
    void execute(Session session);
}
