package datadriven;


import org.json.JSONObject;
import org.testng.annotations.*;
import utils.Data;
import utils.Log;

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
    Log log;  //Objeto que representa classe utils.log
    int contador;      //Ajuda a contar o numero de linhas executadas
    double soma;  // Somar os valores das senhas(brincadeira com as senhas)

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

        BufferedReader bufferedReader = new BufferedReader(new FileReader("db/usersPairwise.csv"));
        while((linha = bufferedReader.readLine()) != null) {

            testCase = linha.split(",");
            testCases.add(testCase);

        }
        return testCases.iterator();
    }


    @BeforeClass // Antes da classe que executa os testes
    public void setup() throws IOException {
        data = new Data();
        log = new Log();

        log.iniciarLog(); // Criar o arquivo e escrever a linha de cabecalho
    }

    @AfterClass  //Depois que a classe terminar de executar todos os seus testes
    public void tearDown() {
        System.out.println("TOTAL DE REGISTROS = "+ contador); }

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

        String userId =                     //Extraindo token
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

        contador += 1; // Contador sempre é += ou ++
        System.out.println("O userId é "+ userId); //Extraindo token
        System.out.println("Essa é a linha nº " + contador);

        soma = soma + Double.parseDouble(password);  //convertendo password para double
        System.out.println("SOMA TOTAL = "+soma);

    }



}

