package repositories;

import models.Transaction;
import models.TransactionType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountsRepository {

    private static final long STARTING_AMOUNT = 100L;

    private static Map<Long, Long> accounts = new ConcurrentHashMap<>();

    /**
     * Makes financial transaction depending on {@link TransactionType}.
     * If the type is write-off, funds should only be subtracted from the account, and if the type is accrual,
     * funds should be added to the account.
     */
    public void makeFinancialTransaction(Transaction transaction) {
        if (!transaction.getType().equals(TransactionType.WRITEOFF)) {
            accounts.put(transaction.getToId(), accounts.get(transaction.getToId()) + transaction.getAmount());
        } else if (!transaction.getType().equals(TransactionType.ACCRUAL)) {
            accounts.put(transaction.getFromId(), accounts.get(transaction.getFromId()) - transaction.getAmount());
        }
    }

    public void createAccountFotTransactionIfAbsent(Long accountId) {
        if (accountId != null) {
            accounts.putIfAbsent(accountId, STARTING_AMOUNT);
        }
    }

    public Long getAccount(Long id) {
        return accounts.get(id);
    }

    public Map<Long, Long> getAccounts() {
        return accounts;
    }
}