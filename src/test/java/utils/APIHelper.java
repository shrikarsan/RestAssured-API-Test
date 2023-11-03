package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.*;

public class APIHelper {
    private static final String defaultBaseURI = "http://localhost:7081";
    private static final String defaultBooksPath = "/api/books/";
    private static final String defaultUserUsername = "user";
    private static final String defaultUserPassword = "password";
    private static final String defaultAdminUsername = "admin";
    private static final String defaultAdminPassword = "password";
    private static final int defaultValidBookId = 1;
    private static final int defaultInvalidBookId = 999;

    private static Properties apiConfig;
    private static Properties authConfig;
    static {
        try {
            apiConfig = PropertiesLoader.loadAPIProperties();
            authConfig = PropertiesLoader.loadAuthProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String baseURI = Objects.requireNonNullElse(apiConfig.getProperty("base-uri"), defaultBaseURI);
    public static String booksPath = Objects.requireNonNullElse(apiConfig.getProperty("books-path"), defaultBooksPath);

    public static List<Integer> bookIdList;
    public static int validBookId;
    public static int invalidBookId;

    private static void setValidBookId() {
        bookIdList = getAllBookIds();
        validBookId = bookIdList.get(0);
        validBookId = Objects.requireNonNullElse(validBookId, defaultValidBookId);
    }

    private static void setInvalidBookId() {
        Random random = new Random();
        invalidBookId = random.nextInt(10);
        while (bookIdList.contains(invalidBookId)) {
            invalidBookId = random.nextInt(10);
        }
        invalidBookId = Objects.requireNonNullElse(invalidBookId, defaultInvalidBookId);
    }


    // To add some books before as pre-condition
    public static void postBooks() {

        List<String> requestPayloads = new ArrayList<>();
        requestPayloads.add("{\"title\": \"The Alchemist\", \"author\": \"Paulo Coelho\"}");
        requestPayloads.add("{\"title\": \"To Kill a Mockingbird\", \"author\": \"Harper Lee\"}");
        requestPayloads.add("{\"title\": \"Frankenstein\", \"author\": \"Mary Shelley\"}");

        for (String payload : requestPayloads) {
            RestAssured
                    .given()
                    .spec(APIHelper.getUserBasicAuth())
                    .contentType(ContentType.JSON)
                    .body(payload)
                    .post(booksPath);
        }

        setValidBookId();
        setInvalidBookId();
    }

    private static List<Integer> getAllBookIds() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getUserBasicAuth())
                .contentType(ContentType.JSON)
                .get(booksPath);

        return response.jsonPath().getList("id");
    }

    // To delete the added books after as post-condition
    public static void deleteAllBooks() {
        List<Integer> bookIdList = getAllBookIds();

        for (int id : bookIdList) {
            RestAssured
                    .given()
                    .spec(APIHelper.getUserBasicAuth())
                    .contentType(ContentType.JSON)
                    .delete(booksPath + id);
        }
    }

    public static RequestSpecification getUserBasicAuth() {
        return RestAssured
                .given()
                .auth()
                .basic(
                        Objects.requireNonNullElse(authConfig.getProperty("user-username"), defaultUserUsername),
                        Objects.requireNonNullElse(authConfig.getProperty("user-password"), defaultUserPassword)
                );
    }

    public static RequestSpecification getAdminBasicAuth() {
        return RestAssured
                .given()
                .auth()
                .basic(
                        Objects.requireNonNullElse(authConfig.getProperty("admin-username"), defaultAdminUsername),
                        Objects.requireNonNullElse(authConfig.getProperty("admin-password"), defaultAdminPassword)
                );
    }
}
