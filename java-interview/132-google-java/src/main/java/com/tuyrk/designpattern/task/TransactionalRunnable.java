package com.tuyrk.designpattern.task;

public abstract class TransactionalRunnable implements Runnable {
    protected abstract void doRun();

    @Override
    public void run() {
        boolean shouldRollback = false;
        try {
            beginTransaction();
        } catch (Exception e) {
            shouldRollback = true;
            throw e;
        } finally {
            if (shouldRollback) {
                rollback();
            } else {
                commit();
            }
        }
    }

    private void commit() {
        System.out.println("commit");
    }

    private void rollback() {
        System.out.println("rollback");
    }

    private void beginTransaction() {
        System.out.println("beginTransaction");
    }
}
