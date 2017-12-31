package models;

public class Transaction {

    private Long id;

    /**
     * Sender`s id.
     */
    private Long fromId;

    /**
     * Receiver`s id.
     */
    private Long toId;

    private TransactionType type;

    /**
     * Amount of funds.
     */
    private Long amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFromId() {
        return fromId;
    }

    public Transaction setFromId(Long fromId) {
        this.fromId = fromId;
        return this;
    }

    public Long getToId() {
        return toId;
    }

    public Transaction setToId(Long toId) {
        this.toId = toId;
        return this;
    }

    public TransactionType getType() {
        return type;
    }

    public Transaction setType(TransactionType type) {
        this.type = type;
        return this;
    }

    public Long getAmount() {
        return amount;
    }

    public Transaction setAmount(Long amount) {
        this.amount = amount;
        return this;
    }
}