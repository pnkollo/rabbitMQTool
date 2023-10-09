package com.example.rabbitshell.repositories;

import com.example.rabbitshell.entities.Cliuser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CliUserRepository extends JpaRepository<Cliuser, Long> {

    @Query(value = "SELECT * FROM CLIUSER WHERE username = : username", nativeQuery = true)
    public Cliuser findByUsername(@Param("username") String username);
}
