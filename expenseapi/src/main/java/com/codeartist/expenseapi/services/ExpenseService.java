package com.codeartist.expenseapi.services;

import com.codeartist.expenseapi.entity.ExpenseEntity;
import com.codeartist.expenseapi.repository.ExpenseRepo;
import com.codeartist.expenseapi.requestDto.ExpenseRequestDto;
import com.codeartist.expenseapi.response.ExpenseResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {
    @Autowired
    ExpenseRepo expenseRepo;

    public ExpenseResponseDto addOrUpdateExpense(ExpenseRequestDto expenseRequestDto){
        ExpenseEntity expenseEntity = convertExpenseReqToEnt(expenseRequestDto);
        return convertExpenseEntToRes(expenseRepo.save(expenseEntity));
    }

    private ExpenseEntity convertExpenseReqToEnt(ExpenseRequestDto expenseRequestDto){
        return ExpenseEntity.builder()
                .name(expenseRequestDto.getName())
                .amount(expenseRequestDto.getAmount())
                .stDate(expenseRequestDto.getStDate())
                .edDate(expenseRequestDto.getEnDate())
                .build();
    }
    private ExpenseResponseDto convertExpenseEntToRes(ExpenseEntity expense){
        return ExpenseResponseDto.builder()
                .name(expense.getName())
                .amount(expense.getAmount())
                .stDate(expense.getStDate())
                .enDate(expense.getEdDate())
                .build();
    }
}
