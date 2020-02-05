package edu.itserulik.transfer;

import edu.itserulik.transfer.model.document.Account;
import edu.itserulik.transfer.model.document.Person;
import edu.itserulik.transfer.model.dto.AccountDto;
import edu.itserulik.transfer.model.dto.PersonDto;
import edu.itserulik.transfer.model.dto.TransferDto;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

class ReactiveApplicationTest extends AbstractTest {

    private static final String NAME1 = "Ivan";
    private static final String NAME2 = "Ekaterina";

    @Test
    void getAccount_success() {
        ObjectId objectId = ObjectId.get();
        dao.save(createAccount(objectId, BigDecimal.TEN, NAME1)).block();

        get("/api/account?id=" + objectId.toHexString()).then()
                .statusCode(200)
                .assertThat()
                .body("party.firstName", equalTo(NAME1));
    }

    @Test
    void getAccount_failNotFound() {
        ObjectId objectId = ObjectId.get();

        get("/api/account?id=" + objectId.toHexString()).then()
                .statusCode(400)
                .assertThat()
                .body("message", equalTo("Not found"));
    }


    @Test
    void createAccount_success() {
        with().body(
                createAccountDto(BigDecimal.ONE, NAME1))
                .when()
                .request("POST", "/api/saveAccount")
                .then()
                .statusCode(200)
                .assertThat()
                .body("party.firstName", equalTo(NAME1));
    }

    @Test
    void createAccount_failNotEnoughMoney() {
        with().body(
                createAccountDto(BigDecimal.valueOf(-1), NAME1))
                .when()
                .request("POST", "/api/saveAccount")
                .then()
                .statusCode(400)
                .assertThat()
                .body("message", equalTo("Not enough money to transfer from account"));
    }

    @Test
    void transferMoney_success() {
        ObjectId accountId1 = ObjectId.get();
        dao.save(createAccount(accountId1, BigDecimal.ONE, NAME1)).block();

        ObjectId accountId2 = ObjectId.get();
        dao.save(createAccount(accountId2, BigDecimal.TEN, NAME2)).block();

        with().body(createTransferDto(BigDecimal.ONE, accountId1, accountId2))
                .when()
                .request("POST", "/api/transfer")
                .then()
                .statusCode(200)
                .assertThat()
                .body("balance", hasItems(0, 11));

        get("/api/account?id=" + accountId1.toHexString()).then().statusCode(200)
                .assertThat()
                .body("balance", equalTo(0));
        get("/api/account?id=" + accountId2.toHexString()).then().statusCode(200)
                .assertThat()
                .body("balance", equalTo(11));
    }

    @Test
    void transferMoney_failNotEnoughMoney() {
        ObjectId accountId1 = ObjectId.get();
        dao.save(createAccount(accountId1, BigDecimal.ZERO, NAME1)).block();

        ObjectId accountId2 = ObjectId.get();
        dao.save(createAccount(accountId2, BigDecimal.ONE, NAME2)).block();

        with().body(createTransferDto(BigDecimal.TEN, accountId1, accountId2))
                .when()
                .request("POST", "/api/transfer")
                .then()
                .statusCode(400)
                .assertThat()
                .body("message", equalTo("Not enough money to transfer from account"));

        get("/api/account?id=" + accountId1.toHexString()).then().statusCode(200)
                .assertThat()
                .body("balance", equalTo(0));
        get("/api/account?id=" + accountId2.toHexString()).then().statusCode(200)
                .assertThat()
                .body("balance", equalTo(1));
    }

    @Test
    void transferMoney_failAccountNotFound() {
        ObjectId accountId1 = ObjectId.get();
        ObjectId accountId2 = ObjectId.get();

        with().body(createTransferDto(BigDecimal.TEN, accountId1, accountId2))
                .when()
                .request("POST", "/api/transfer")
                .then()
                .statusCode(400)
                .assertThat()
                .body("message", equalTo("Not found"));
    }

    private Account createAccount(ObjectId id, BigDecimal balance, String firstName) {
        return Account.builder()
                .id(id)
                .balance(balance)
                .party(Person.builder()
                        .firstName(firstName)
                        .build())
                .build();
    }

    private AccountDto createAccountDto(BigDecimal balance, String firstName) {
        return AccountDto.builder()
                .balance(balance)
                .party(PersonDto.builder()
                        .firstName(firstName)
                        .build())
                .build();
    }

    private TransferDto createTransferDto(BigDecimal balance, ObjectId id1, ObjectId id2) {
        return TransferDto.builder()
                .accountFromId(id1.toHexString())
                .accountToId(id2.toHexString())
                .sum(balance)
                .build();
    }

}
