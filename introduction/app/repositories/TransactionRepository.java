package repositories;

import models.Transaction;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionRepository {

    private static AtomicLong counter = new AtomicLong();
    private static List<Transaction> transactions = new CopyOnWriteArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public static long getCounter() {
        return counter.getAndIncrement();
    }
}