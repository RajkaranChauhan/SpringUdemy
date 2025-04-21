package com.springBootUdemy;

//import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springBootUdemy.controller.AddResponse;
import com.springBootUdemy.controller.Library;
import com.springBootUdemy.controller.LibraryController;
import com.springBootUdemy.repository.LibraryRepository;
import com.springBootUdemy.service.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SpringBootUdemyApplicationTests {
	@Autowired
	LibraryService libraryService;
	@Autowired
	LibraryController libraryController; //con
	@MockitoBean
	LibraryRepository libraryRepositoryBean; //repository
	@MockitoBean
	LibraryService libraryServiceBean;// libraryService
	@Autowired
	private MockMvc mockMvc;



	@Test
	void contextLoads() {
	}
	@Test
	public void checkBuildIdLogic(){
		String id = libraryService.buildId("Zman", 24);
		System.out.println("-----------------------------"+id);
		assertEquals(id,"OLDZman24");

		String id2 = libraryService.buildId("Raj", 24);
		assertEquals(id2,"Raj24");
	}

	@Test
	public void addBookTest(){
		Library lib= buildLibrary();
		//mock //String id=libraryService.buildId(library.getIsbn(),library.getAisle()); // Need to mock this
		when(libraryServiceBean.buildId(lib.getIsbn(),lib.getAisle())).thenReturn(lib.getId());
		//mock //boolean b = libraryService.checkBookAlreadyExist(id)
		when(libraryServiceBean.checkBookAlreadyExist(lib.getId())).thenReturn(false);

		ResponseEntity responseEntity = libraryController.addBookImplementation(buildLibrary());
		HttpStatusCode statusCode = responseEntity.getStatusCode();
		System.out.println("--->"+statusCode);
		assertEquals(statusCode, HttpStatus.CREATED);
		AddResponse ad = (AddResponse) responseEntity.getBody();
		ad.getId();
		assertEquals(lib.getId(),ad.getId());
		assertEquals("Success Book is Added",ad.getMsg());
	}
	@Test
	public void addBookControllerTest() throws Exception {
		Library lib= buildLibrary();
		//now as we need to pass the body info as json but we have java object of lib
		//we can convert the java obj lib to json with below method
		ObjectMapper map= new ObjectMapper();
		String requestBodyJson = map.writeValueAsString(lib);


		//mock //String id=libraryService.buildId(library.getIsbn(),library.getAisle()); // Need to mock this
		when(libraryServiceBean.buildId(lib.getIsbn(),lib.getAisle())).thenReturn(lib.getId());
		//mock //boolean b = libraryService.checkBookAlreadyExist(id)
		when(libraryServiceBean.checkBookAlreadyExist(lib.getId())).thenReturn(false);
		//mock repository.save(library);//mock
		when(libraryRepositoryBean.save(any())).thenReturn(lib);

		this.mockMvc.perform(post("/addBook").contentType(MediaType.APPLICATION_JSON).content(requestBodyJson))
				.andDo(print())  // to print the response of the POST
				.andExpect(status().isCreated()) // assertion of isCreated
				.andExpect(jsonPath("$.id").value(lib.getId())); //assertion of the id value

	}

	@Test
	public void getBookByAuthorTest() throws Exception {
		// as we need to pass josn of list of lib set fo date
		// so we are duplicating the same data which does not matter
		List<Library> li = new ArrayList<>();
		li.add(buildLibrary());
		li.add(buildLibrary());

		//mock List<Library> findAllByAuthor(String authorName);
		when(libraryRepositoryBean.findAllByAuthor(any())).thenReturn(li); // here the return should be list of library object

		this.mockMvc.perform(get("/getBooks/author").param("authorName","LaxmiChauhan"))
				.andDo(print())// print the output of request
				.andExpect(status().isOk()) // check if status is success
				.andExpect(jsonPath("$.length()",is(2))) // lenght of the json array which is 2
				.andExpect(jsonPath("$.[0].id").value("sfe222"));//validating output of 0th index id of response body
	}

	@Test
	public void updateBookTest() throws Exception {
		Library lib= buildLibrary();
//mock		Library existingBook= libraryService.getBookById(id);
		when(libraryService.getBookById(any())).thenReturn(buildLibrary());

		// the below fields are updated from the json body but we don't have to mock as it is the core logic not any external dependencies
//		existingBook.setAisle(library.getAisle());
//		existingBook.setAuthor(library.getAuthor());
//		existingBook.setBook_name(library.getBook_name());

//		repository.save(existingBook);
		when(libraryRepositoryBean.save(any())).thenReturn(lib);

		//now as we need to pass the body info as json but we have java object of lib
		//we can convert the java obj lib to json with below method
		ObjectMapper map= new ObjectMapper();
		String requestBodyJson = map.writeValueAsString(UpdateLibrary());

		this.mockMvc.perform(put("/updateBook/"+lib.getId()).contentType(MediaType.APPLICATION_JSON).content(requestBodyJson))
				.andDo(print())
				.andExpect(status().isOk())
				//now to validate entire content of json
				.andExpect(content().json("{\"book_name\":\"Python\",\"id\":\"sfe222\",\"isbn\":\"sfe\",\"aisle\":7,\"author\":\"RajKaran\"}"));
	}

	@Test
	public void deleteBookControllerTest() throws Exception {
		//Library libraryDelete= libraryService.getBookById(library.getId());
		when(libraryService.getBookById(any())).thenReturn(buildLibrary());
		// for save book it was returning library object
		// for delete it does not return anything the return type is void
		//repository.delete(libraryDelete);
		doNothing().when(libraryRepositoryBean).delete(buildLibrary());

		this.mockMvc.perform(delete("/deleteBook").contentType(MediaType.APPLICATION_JSON).content("{\"id\":\"sfe222\"}"))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(content().string("Book is deleted"));
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

	public Library UpdateLibrary(){
		Library lib = new Library();
		lib.setAisle(007);
		lib.setBook_name("Python");
		lib.setIsbn("sfe");
		lib.setAuthor("RajKaran");
		lib.setId("sfe222");
		return lib;
	}



}
