package com.springBootUdemy;

import com.springBootUdemy.controller.Library;
import com.springBootUdemy.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;


@SpringBootApplication
//CommandLineRunner was used to override run method and so some database interaction to see jpa methods call
//public class SpringBootUdemyApplication implements CommandLineRunner {
public class SpringBootUdemyApplication {
	@Autowired
	LibraryRepository repository;


	public static void main(String[] args) {
		SpringApplication.run(SpringBootUdemyApplication.class, args);
	}


//below method was used to set data in DB using Jpa now as we are building api and need to pass data through json body
	// we need to comment this out
//	@Override
//	public void run(String[] args){
//		Library lib = repository.findById("abcd1234").get();
//		String author = lib.getAuthor();
//		System.out.println(author);
//		String bookName = lib.getBook_name();
//		System.out.println(bookName);
//		//method to add records in table
//		//for that we need to create obj of Library class
//		Library library= new Library();
//		library.setAisle(123);
//		library.setAuthor("Laxmi");
//		library.setIsbn("chau");
//		library.setBook_name("Biology");
//		library.setId("chau123");
//		//repository.save(library);
//
//
//		List<Library> all = repository.findAll();
//		for(Library item: all){
//			System.out.println(item.getBook_name());
//		}
//
//		//for deletion
//		repository.delete(library);
//	}

}
