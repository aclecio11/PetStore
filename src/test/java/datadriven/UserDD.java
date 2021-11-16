package datadriven;


import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

//3 . Classe

public class UserDD {
    //3.1 - Atributos
    String uri = "https://petstore.swagger.io/v2/user"; // endereçe da entidade User
    Data data;  //objeto que representa a classe utils.Data

    //3.2 - Métodos e funções

    @DataProvider
    public Iterator<Object[]> provider() throws IOException {
        List<Object[]> testCases = new ArrayList<>();
        //List<String[]> testCases = new ArrayList<>();
        String[] testCase;
        String linha;

        //List<Object[]> testCases = new ArrayList<>();
       // String[] testCase ;
      //  String linha ;

        BufferedReader bufferedReader = new BufferedReader(new FileReader("db/users.csv"));
        while((linha = bufferedReader.readLine()) != null) {

            testCase = linha.split(",");
            testCases.add(testCase);

        }
        return testCases.iterator();
    }


    @BeforeClass
    public void setup() {
        data = new Data();

    }

    //Incluir - Create - Post
    @Test(dataProvider  = "provider") // identifica o método ou função como um teste para o testNG
    public void incluirUsuario(
                               String id,
                               String username,
                               String firstName,
                               String lastName,
                               String email,
                               String password,
                               String phone,
                               String userStatus) throws IOException {
        String jsonBody = new JSONObject()
                .put("id", id)
                .put("username" ,username)
                .put("firstName", firstName)
                .put("lastName", lastName)
                .put("email", email)
                .put("password", password)
                .put("phone", phone)
                .put("userStatus", userStatus)
                .toString();

        String userId =
                given() // Dado
                        .contentType("application/json") // comum em API REST - antigos eram "text/xml"
                        .log().all()
                        .body(jsonBody)
                .when() //Quando
                        .post(uri)
                .then() //Então
                        .log().all()
                        .statusCode(200)
                        .body("code", is(200))
                        .body("type", is("unknown"))
                .extract()
                        .path("message")
                ;

        System.out.println("O userId é "+ userId);

    }



}

