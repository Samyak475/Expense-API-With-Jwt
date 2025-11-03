package com.codeartist.expenseapi.repository;

import com.codeartist.expenseapi.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepo extends JpaRepository<ExpenseEntity,Long> {
}
