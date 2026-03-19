package com.project.reflash.backend.repository;

import com.project.reflash.backend.dto.Administrator;
import com.project.reflash.backend.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {
    @Query("SELECT a from Administrator a WHERE a.username = :username")
    Optional<Administrator> findByUsername(@Param("username") String username);
}
