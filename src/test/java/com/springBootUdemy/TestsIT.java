package com.springBootUdemy;

import com.springBootUdemy.controller.Library;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
//import org.testng.annotations.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest

public class TestsIT {
    @Test
    public void getAuthorNameBooksTest() throws JSONException {
        TestRestTemplate restTemplate= new TestRestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/getBooks/author?authorName=Raju", String.class);
        HttpStatusCode statusCode = response.getStatusCode();
        System.out.println(statusCode);
        String body = response.getBody();
        System.out.println(body);

        String expected= "[{\"book_name\":\"Bio\",\"id\":\"mno123\",\"isbn\":\"mno\",\"aisle\":123,\"author\":\"Raju\"},{\"book_name\":\"MoralScience\",\"id\":\"xyz1234\",\"isbn\":\"zxy\",\"aisle\":1234,\"author\":\"Raju\"}]";
        JSONAssert.assertEquals(expected,response.getBody(),false);
    }

    @Test
    public void addBookIntegrationTest(){
        TestRestTemplate restTemplate= new TestRestTemplate();

        //setting up the headers how it is like json format
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //request type needs to be set for the input parameter of json
        //here data is taken form Library buildLibrary() and it should be passed in json format and the info is passed through headers
        HttpEntity<Library> request= new HttpEntity<Library>(buildLibrary(),headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/addBook", request, String.class);
        assertEquals(HttpStatus.CREATED ,response.getStatusCode());

        //if we want to check header of response then 'unique'='sfe222'
        System.out.println(response.getHeaders().get("unique"));
        String uni = response.getHeaders().get("unique").get(0);//as the return type od unique is list
        assertEquals(buildLibrary().getId(),uni);


    }

    public Library buildLibrary(){
        Library lib = new Library();
        lib.setAisle(222);
        lib.setBook_name("Spring");
        lib.setIsbn("sfe");
        lib.setAuthor("LaxmiChauhan");
        lib.setId("sfe222");
        return lib;
    }


}
