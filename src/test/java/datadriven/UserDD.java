package datadriven;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Data;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

//3 . Classe

public class UserDD {
    //3.1 - Atributos
    String uri = "https://petstore.swagger.io/v2/user"; // endereçe da entidade User
    Data data;  //objeto que representa a classe utils.Data

    //3.2 - Métodos e funções
    @BeforeMethod
    public void setup() {
        data = new Data();
    }

    //Incluir - Create - Post
    @Test(priority = 1) // identifica o método ou função como um teste para o testNG
    public void incluirUsuario() throws IOException {
        String jsonBody = data.lerJson("db/user1.json");

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

