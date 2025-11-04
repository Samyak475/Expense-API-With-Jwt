package com.codeartist.expenseapi.controller;

import com.codeartist.expenseapi.requestDto.ExpenseRequestDto;
import com.codeartist.expenseapi.response.ExpenseResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpenseController {


    @PostMapping("/add")
    public ResponseEntity<ExpenseResponseDto> addExpense(@RequestBody ExpenseRequestDto){

    }
}
