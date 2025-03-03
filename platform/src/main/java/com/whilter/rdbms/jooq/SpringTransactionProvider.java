package com.whilter.rdbms.jooq;

import org.jooq.TransactionContext;
import org.jooq.TransactionProvider;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRED;

public class SpringTransactionProvider implements TransactionProvider {

    private PlatformTransactionManager txMgr;

    public SpringTransactionProvider(PlatformTransactionManager txMgr) {
        this.txMgr = txMgr;
    }

    @Override
    public void begin(TransactionContext ctx) {
        TransactionStatus tx = txMgr.getTransaction(new DefaultTransactionDefinition(PROPAGATION_REQUIRED));
        ctx.transaction(new SpringTransaction(tx));
    }

    @Override
    public void commit(TransactionContext ctx) {
        txMgr.commit(((SpringTransaction) ctx.transaction()).tx);
    }

    @Override
    public void rollback(TransactionContext ctx) {
        txMgr.rollback(((SpringTransaction) ctx.transaction()).tx);
    }
}
