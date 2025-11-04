package com.codeartist.expenseapi.repository;

import com.codeartist.expenseapi.entity.ExpenseEntity;
import com.codeartist.expenseapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseRepo extends JpaRepository<ExpenseEntity,Long> {
    Optional<ExpenseEntity> findByUser(UserEntity user);
    Optional<ExpenseEntity> findByName(String name);
}
