//1 - Pacote
package petstore;

//2 - Bibliotecas

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

//3- Classe
public class Pet {
    //3.1 - Atributos
    String uri = "https://petstore.swagger.io/v2/pet"; // endereçe da entidade Pet

    //3.2 - Métodos e funções
    public String lerJson(String caminhoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }

    //Incluir - Create - Post
    @Test(priority = 1) // identifica o método ou função como um teste para o testNG
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("db/pet1.json");

        //Sintaxe Gherkin
        //Dado _Quando - Então
        //Given - When - Then

        // rest-assured
        given() // Dado
                .contentType("application/json") // comum em API REST - antigos eram "text/xml"
                .log().all()
                .body(jsonBody)
        .when() //Quando
                .post(uri)
        .then() //Então
                .log().all()
                .statusCode(200)
                .body("name", is("Chocolate"))
                .body("status", is("available"))
                .body("category.name", is("dog"))
                .body("tags.name", contains("sta"))
        ;

    }

    @Test(priority = 2)
    public void consultarPet() {
        String petId = "1990040331";

        // rest-assured
        given() // Dado
                .contentType("application/json") // comum em API REST - antigos eram "text/xml"
                .log().all()
        .when() //Quando
                .get(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Chocolate"))
                .body("status", is("available"))
                .body("category.name", is("dog"))
                .body("tags.name", contains("sta"))
        ;
    }

    @Test(priority = 3)
    public void alterarPet() throws IOException {
        String jsonBody = lerJson("db/pet2.json");

        //Sintaxe Gherkin
        //Dado _Quando - Então
        //Given - When - Then

        // rest-assured
        given() // Dado
                .contentType("application/json") // comum em API REST - antigos eram "text/xml"
                .log().all()
                .body(jsonBody)
        .when() //Quando
                .put(uri)
        .then() //Então
                .log().all()
                .statusCode(200)
                .body("name", is("Chocolate"))
                .body("status", is("sold"))
                .body("category.name", is("dog"))
        ;
    }

    @Test(priority = 4)
    public void excluirPet() {
        String petId = "1990040331";

        // rest-assured
        given() // Dado
                .contentType("application/json") // comum em API REST - antigos eram "text/xml"
                .log().all()
        .when() //Quando
                .delete(uri + "/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", is(petId))
        ;


    }

    @Test (priority = 5)
    public void consultarPetPorStatus() {
        String petStatus = "available";

        // rest-assured
        given() // Dado
                .contentType("application/json") // comum em API REST - antigos eram "text/xml"
                .log().all()
        .when() //Quando
                .get(uri + "/findByStatus?status=available" + petStatus)
        .then()
                .log().all()
                .statusCode(200)
               // .body("name", contains("[\"Chocolate\"]"))
        ;

    }

}
