package validators;

import models.Transaction;
import models.TransactionType;

public class AccountValidator {

    /**
     * Checks if sender`s or receiver`s (or both) ids are presented (depending on transaction type).
     */
    public boolean fromAndToAreValid(Transaction transaction) {
        if (transaction.getType().equals(TransactionType.ACCRUAL) && transaction.getToId() != null) {
            transaction.setFromId(null);
            return true;
        } else if (transaction.getType().equals(TransactionType.WRITEOFF) && transaction.getFromId() != null) {
            transaction.setToId(null);
            return true;
        } else if (transaction.getType().equals(TransactionType.TRANSFER)
                && transaction.getFromId() != null && transaction.getToId() != null) {
            return true;
        }
        return false;
    }
}