package com.codeartist.expenseapi.repository;

import com.codeartist.expenseapi.entity.ExpenseEntity;
import com.codeartist.expenseapi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepo extends JpaRepository<ExpenseEntity,Long> {
    Optional<ExpenseEntity> findByUser(UserEntity user);
    Optional<ExpenseEntity> findByNameAndUser(String name,UserEntity user);
    List<ExpenseEntity> findByUserAndStDateGreaterThanEqual(UserEntity user , LocalDate stDate);
    List<ExpenseEntity> findByUserAndStDateGreaterThanEqualAndEdDateLessThanEqual(UserEntity user,LocalDate stDate, LocalDate edDate);
}
