package com.springBootUdemy.controller;

import com.springBootUdemy.repository.LibraryRepository;
import com.springBootUdemy.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class LibraryController {
    @Autowired
    LibraryRepository repository;
    @Autowired
    AddResponse ad;
    @Autowired
    LibraryService libraryService;

    @PostMapping("/addBook")
    public ResponseEntity addBookImplementation(@RequestBody Library library)
    {
        String id=libraryService.buildId(library.getIsbn(),library.getAisle());
        boolean b = libraryService.checkBookAlreadyExist(id);
        if(!b) {
            library.setId(id);

            repository.save(library);

            ad.setMsg("Success Book is Added");
            ad.setId(id);
            //now as we need to send the above in json response we need to do it using
            //ResponseEntity and also we need to set stauts code
            // now we need to set http status

            //In response header if we want to pass some more header then we can use
            HttpHeaders header = new HttpHeaders();
            header.add("unique", id);
            return new ResponseEntity<AddResponse>(ad, header, HttpStatus.CREATED);
        }
        else {
            ad.setMsg("Book already Exists");
            ad.setId(id);
            return  new ResponseEntity<AddResponse>(ad,HttpStatus.ACCEPTED);
        }


    }
    @GetMapping("/getBooks/{id}")
    public Library getBookById(@PathVariable(value="id") String id){
        try {
            Library libObject = repository.findById(id).get();
            return libObject;
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("getBooks/author")
    public List<Library> getBookByAuthorNames(@RequestParam (value ="authorName") String authorName){
        return repository.findAllByAuthor(authorName);
    }
}
