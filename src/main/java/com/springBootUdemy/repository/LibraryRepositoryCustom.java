package com.springBootUdemy.repository;

import com.springBootUdemy.controller.Library;

import java.util.List;

public interface LibraryRepositoryCustom {
    List<Library> findAllByAuthor(String authorName);
}
