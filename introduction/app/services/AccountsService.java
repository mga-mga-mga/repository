package services;

import models.Transaction;
import models.TransactionType;
import repositories.AccountsRepository;
import repositories.TransactionRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

public class AccountsService {

    private AccountsRepository accountsRepository;
    private TransactionRepository transactionRepository;

    @Inject
    public AccountsService(AccountsRepository accountsRepository,
                           TransactionRepository transactionRepository) {
        this.accountsRepository = accountsRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Checks if it is possible to perform the required operation.
     *
     * @return false if operation leads to subtraction of funds, and after the subtraction the account will be
     * negative, or if sender`s id is null; returns true in other cases.
     */
    public boolean checkTransactionOperation(Transaction transaction) {
        accountsRepository.createAccountFotTransactionIfAbsent(transaction.getFromId());
        accountsRepository.createAccountFotTransactionIfAbsent(transaction.getToId());
        return operationIsPossible(transaction, accountsRepository.getAccounts());
    }

    private boolean operationIsPossible(Transaction transaction, Map<Long, Long> accounts) {
        return !(!transaction.getType().equals(TransactionType.ACCRUAL)
                && transaction.getFromId() != null
                && accounts.get(transaction.getFromId()) - transaction.getAmount() < 0);
    }

    /**
     * Performs transfer of funds, and makes record in transaction history.
     */
    public void makeTransaction(Transaction transaction) {
        transaction.setId(TransactionRepository.getCounter());
        accountsRepository.makeFinancialTransaction(transaction);
        transactionRepository.addTransaction(transaction);
    }

    public Long getAccount(Long id) {
        return accountsRepository.getAccount(id);
    }

    public Map<Long, Long> getAccounts() {
        return accountsRepository.getAccounts();
    }

    public List<Transaction> getTransactions() {
        return transactionRepository.getTransactions();
    }

}