package com.codeartist.expenseapi.controller;

import com.codeartist.expenseapi.requestDto.ExpenseRequestDto;
import com.codeartist.expenseapi.requestDto.GetExpenseDto;
import com.codeartist.expenseapi.response.ExpenseResponseDto;
import com.codeartist.expenseapi.services.ExpenseService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<ExpenseResponseDto> addExpense(@RequestBody ExpenseRequestDto requestDto){
           return new ResponseEntity<>(expenseService.addOrUpdateExpense(requestDto), HttpStatus.ACCEPTED);
    }
    @PostMapping("/update")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@RequestBody ExpenseRequestDto requestDto){
        return new ResponseEntity<>(expenseService.addOrUpdateExpense(requestDto), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String>deleteExpense(@RequestParam(name = "name") String name){
        try {
            expenseService.removeExpense(name);
            return new ResponseEntity<>("expense deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<ExpenseResponseDto>> getAllExpense(@RequestParam(name = "date")String type,
                                                  @RequestBody(required = false) GetExpenseDto getExpenseDto)
    {
        try {
        List<ExpenseResponseDto> responseDtos =expenseService.getExpenses(type,getExpenseDto.getStDate(),getExpenseDto.getEdDate());
        return new ResponseEntity<>(responseDtos,HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
