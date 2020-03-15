package org.hibernate.tutorials;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class FastFunds implements Runnable {

    private static final int INIT_BALANCE = 10031;
    private int reps;
    private int value;
    private boolean add;
    private AtomicReference<Account> accountRef;
    private static Logger logger = Logger.getLogger("FastFunds");

    public static void main(String[] args) {
        Account a = new FastFunds.Account(INIT_BALANCE);
        AtomicReference<Account> atomicReference = new AtomicReference<>(a);
        FastFunds m1 = new FastFunds(false, atomicReference, 200000, 3);
        FastFunds m2 = new FastFunds(true, atomicReference, 200000, 3);
        Thread t1 = new Thread(m1);
        Thread t2 = new Thread(m2);
        t1.start();
        t2.start();

        logger.info("Complete");

        logger.info("Balance: " + a.getBalance());

        if (a.getBalance() != INIT_BALANCE) {
            logger.severe("Expecting balance to be " + INIT_BALANCE);
        }
    }

    public FastFunds(boolean add, AtomicReference<Account> a, int reps, int value) {
        this.add = add;

        this.accountRef = a;
        this.reps = reps;
        this.value = value;
    }

    @Override
    public void run() {
        for (int i = 0; i < reps; i++) {
            if (add) {
                accountRef.getAndUpdate(account -> account.addFunds(value));
            } else {
                accountRef.getAndUpdate(account -> account.withdraw(value));
            }
        }
        System.out.println(accountRef.get().getBalance());
    }

    static final class Account {
        private final int balance;

        public Account(int initialBalance) {
            balance = initialBalance;
        }

        public Account addFunds(int value) {
            return new Account(balance + value);
        }

        public Account withdraw(int value) {
            return new Account(balance - value);
        }

        public int getBalance() {
            return balance;
        }
    }
}

