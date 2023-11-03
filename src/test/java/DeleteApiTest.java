import io.restassured.RestAssured;
import io.restassured.response.Response;
import junit.framework.TestCase;
import org.apache.http.HttpStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.APIHelper;


public class DeleteApiTest {

    private static final String baseURI = APIHelper.baseURI;
    private static final String booksPath = APIHelper.booksPath;

    @BeforeClass
    public static void initialSetUp() {
        RestAssured.baseURI = baseURI;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = baseURI;
        APIHelper.postBooks();
    }

    @After
    public void endSetUp() {

        APIHelper.deleteAllBooks();
    }

    @Test //DEL001
    public void whenDeleteBookByIdWithoutAuth_thenUnauthorized() {
        Response response = RestAssured
                .delete(booksPath + APIHelper.validBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusCode());
    }

    @Test //DEL002
    public void whenAdminDeleteBookById_thenOK() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getAdminBasicAuth())
                .delete(booksPath + APIHelper.validBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
    }

    @Test //DEL003
    public void whenUserDeleteBookById_thenForbidden() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getUserBasicAuth())
                .delete(booksPath + APIHelper.validBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusCode());
    }
    @Test //DEL004
    public void whenDeleteBookByInvalidIdWithoutAuth_thenUnauthorized() {
        Response response = RestAssured
                .delete(booksPath + APIHelper.invalidBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusCode());
    }

    @Test //DEL005
    public void whenAdminDeleteBookByInvalidId_thenNotFound() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getAdminBasicAuth())
                .delete(booksPath + APIHelper.invalidBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusCode());
    }

    @Test //DEL006
    public void whenUserDeleteBookByInvalidId_thenForbidden() {
        Response response = RestAssured
                .given()
                .spec(APIHelper.getUserBasicAuth())
                .delete(booksPath + APIHelper.invalidBookId);
        response.prettyPeek();
        TestCase.assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusCode());
    }

}
