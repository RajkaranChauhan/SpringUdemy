package com.springBootUdemy.service;

import com.springBootUdemy.controller.Library;
import com.springBootUdemy.repository.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibraryService {
    @Autowired
    LibraryRepository repository;

    public String buildId(String isbn, int aisle){
        System.out.println(isbn+"--"+aisle);
        if(isbn.startsWith("Z")){
            return "OLD"+isbn+aisle;
        }
        return isbn+aisle;
    }
    public boolean checkBookAlreadyExist(String id){
        Optional<Library> byId = repository.findById(id);
        if(byId.isPresent())
            return true;
        else
            return false;
    }
    public Library getBookById(String id){
        return repository.findById(id).get();
    }
}
