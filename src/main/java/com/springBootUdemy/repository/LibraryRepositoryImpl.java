package com.springBootUdemy.repository;

import com.springBootUdemy.controller.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryRepositoryImpl implements LibraryRepositoryCustom{

    @Lazy
    @Autowired
    LibraryRepository repository;

    @Override
    public List<Library> findAllByAuthor(String authorName) {
        List<Library> books = repository.findAll();
//        System.out.println(authorName);
//        System.out.println(books.size());
        List<Library> booksWithAuthor = books.stream().filter(x -> x.getAuthor().equalsIgnoreCase(authorName)).collect(Collectors.toList());
//        System.out.println(booksWithAuthor.size());

//        List<Library> booksWithAuthor= new ArrayList<>();
//        for (Library item:books){
//            if(item.getAuthor().equalsIgnoreCase(authorName)){
//                booksWithAuthor.add(item);
//            }
//        }


        return booksWithAuthor;
    }
}

