package com.example;

import com.example.model.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import javax.ws.rs.core.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerTest {
    @Test
    @DisplayName("Test Get All and test filter by developer ")
    @Order(1)
    public void testGetAll(){
        given().when()
                .get("/task").then()
                .statusCode(200)
                .body("size()", is(2));

        given().when()
                .get("/task?developer=Hortelino").then()
                .statusCode(200)
                .body("size()", is(1));
    }
    @Test
    @Order(2)
    @DisplayName("Test Get By Id ")
    public void testGetById(){
        given().when()
                .get("/task/1")
                .then()
                .statusCode(200)
                .body("developer", is("Patolino"))
                .body("project", containsString("Project 1"));

        given().when()
                .get("/task/244")
                .then()
                .statusCode(204);

    }
    @Test
    @Order(3)
    @DisplayName("Test Create ")
    public void testeCreate(){
        Task task = new Task();
        task.description = "Super Task 3";
        task.developer = "Frajola";
        task.project = "Mega Project 2";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            given().when().contentType(ContentType.JSON).body(objectMapper.writeValueAsString(task))
                    .post("/task")
                    .then().statusCode(204);

            given().when()
                    .get("/task").then()
                    .statusCode(200)
                    .body("size()", is(3))
                    .body("developer", hasItem("Frajola"));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(4)
    @DisplayName("Test Delete ")
    public void testeDelete(){
        Task task = (Task) Task.list("developer", "Frajola").get(0);
        given().when().delete("/task/" + task.id).then()
                .statusCode(204);
        given().when()
                .get("/task").then()
                .statusCode(200)
                .body("size()", is(2))
                .body("developer", not(hasItem("Frajola")));

    }
    @Test
    @Order(5)
    @DisplayName("Test Find By static")
    public void testFindBy(){
        given().when()
                .get("/task/developer/Hortelino").then()
                .statusCode(200)
                .body("developer", containsString("Hortelino"));
    }

}
