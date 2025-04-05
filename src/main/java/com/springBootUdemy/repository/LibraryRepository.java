package com.springBootUdemy.repository;
import com.springBootUdemy.controller.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library,String>,LibraryRepositoryCustom{
}
