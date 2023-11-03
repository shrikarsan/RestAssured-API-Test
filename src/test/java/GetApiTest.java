import io.restassured.RestAssured;
import io.restassured.response.Response;
import junit.framework.TestCase;
import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.APIHelper;


public class GetApiTest {

    private static final String baseURI = APIHelper.baseURI;
    private static final String booksPath = APIHelper.booksPath;

    @BeforeClass
    public static void initialSetUp() {
        RestAssured.baseURI = baseURI;
        APIHelper.postBooks();
    }

    @AfterClass
    public static void endSetUp() {
        APIHelper.deleteAllBooks();
    }
    @Test //GET001
    public void whenGetAllBooksWithoutAuth_thenUnauthorized2() {
        Response response = RestAssured
                .get(booksPath);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusCode());
    }

    @Test //GET002
    public void whenAdminGetAllBooks_thenOK() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getAdminBasicAuth())
                .get(booksPath);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test //GET003
    public void whenUserGetAllBooks_thenOK() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getUserBasicAuth())
                .get(booksPath);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test //GET004
    public void whenGetBookByIdWithoutAuth_thenUnauthorized() {
        Response response = RestAssured
                .get(booksPath + APIHelper.validBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusCode());
    }

    @Test //GET005
    public void whenAdminGetBookById_thenOK() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getAdminBasicAuth())
                .get(booksPath + APIHelper.validBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test //GET006
    public void whenUserGetBookById_thenOK() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getUserBasicAuth())
                .get(booksPath + APIHelper.validBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test //GET007
    public void whenGetBookByInvalidIdWithoutAuth_thenUnauthorized() {
        Response response = RestAssured
                .get(booksPath + APIHelper.invalidBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusCode());
    }

    @Test //GET008
    public void whenAdminGetBookByInvalidId_thenNotFound() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getAdminBasicAuth())
                .get(booksPath + APIHelper.invalidBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
    }

    @Test //GET009
    public void whenUserGetBookByInvalidId_thenNotFound() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getUserBasicAuth())
                .get(booksPath + APIHelper.invalidBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
    }

}
