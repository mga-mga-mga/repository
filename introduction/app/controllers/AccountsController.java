package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import models.Transaction;
import play.http.HttpEntity;
import play.libs.Json;
import play.mvc.Result;
import services.AccountsService;
import validators.AccountValidator;

import java.util.List;
import java.util.Map;

/**
 * Main controller of the application.
 */
public class AccountsController extends play.mvc.Controller {

    /**
     * This particular code suits the business logic failure the most appropriate way,
     * as the server understands the content type of the request entity (415 Unsupported
     * Media Type status code is inappropriate), and the syntax of the request entity is
     * correct (400 Bad Request status code is inappropriate) but was unable to process
     * the contained instructions - there are some business rules that are violated.
     */
    private static final int UNPROCESSABLE_ENTITY_CODE = 422;

    private AccountsService accountsService;
    private AccountValidator accountValidator;

    @Inject
    public AccountsController(AccountsService accountsService,
                              AccountValidator accountValidator) {
        this.accountsService = accountsService;
        this.accountValidator = accountValidator;
    }

    /**
     * Validates input data and performs transaction if data is valid.
     */
    public Result makeFinancialTransaction() {
        Transaction transaction;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            transaction = objectMapper.treeToValue(request().body().asJson(), Transaction.class);
        } catch (Exception ex) {
            return badRequest(Json.toJson("Invalid data"));
        }

        if (!accountValidator.fromAndToAreValid(transaction)) {
            return badRequest(Json.toJson("Invalid from or to field(s)"));
        }
        if (!accountsService.checkTransactionOperation(transaction)) {
            return new Result(UNPROCESSABLE_ENTITY_CODE, HttpEntity
                    .fromString("Not enough funds to write-off", "UTF-8"));
        }

        accountsService.makeTransaction(transaction);
        return ok(objectMapper.convertValue(transaction, JsonNode.class));
    }

    public Result getAccount(Long id) {
        Long account = accountsService.getAccount(id);
        if (account == null) {
            return notFound(Json.toJson("Account with id:" + id + " not found"));
        }
        return ok(Json.toJson(account));
    }

    public Result getAllAccounts() {
        Map<Long, Long> accounts = accountsService.getAccounts();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonData = mapper.convertValue(accounts, JsonNode.class);
        return ok(jsonData);
    }

    public Result getAllTransactions() {
        List<Transaction> transactions = accountsService.getTransactions();
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonData = mapper.convertValue(transactions, JsonNode.class);
        return ok(jsonData);
    }
}