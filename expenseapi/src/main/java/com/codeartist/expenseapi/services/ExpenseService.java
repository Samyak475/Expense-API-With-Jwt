package com.codeartist.expenseapi.services;

import com.codeartist.expenseapi.entity.ExpenseEntity;
import com.codeartist.expenseapi.entity.UserEntity;
import com.codeartist.expenseapi.repository.ExpenseRepo;
import com.codeartist.expenseapi.repository.UserRepo;
import com.codeartist.expenseapi.requestDto.ExpenseRequestDto;
import com.codeartist.expenseapi.response.ExpenseResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class ExpenseService {
    @Autowired
    ExpenseRepo expenseRepo;
    @Autowired
    UserRepo userRepo;
    @Transactional
    public ExpenseResponseDto addOrUpdateExpense(ExpenseRequestDto expenseRequestDto){
       UserEntity user =(UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ExpenseEntity expenseEntity = convertExpenseReqToEnt(expenseRequestDto);
        expenseEntity.setUser(user);
        Optional<ExpenseEntity> expenseEntityOptional = expenseRepo.findByNameAndUser(expenseRequestDto.getName(),user);
        if(expenseEntityOptional.isPresent()){
            expenseEntity.setId(expenseEntityOptional.get().getId());
        }
        return convertExpenseEntToRes(expenseRepo.save(expenseEntity));
    }
    @Transactional
    public void removeExpense(String name){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<ExpenseEntity> expenseEntityOptional = expenseRepo.findByNameAndUser(name,user);
        if(expenseEntityOptional.isEmpty()){
            throw  new IllegalArgumentException("no expense with name given for logged in user");
        }
         expenseRepo.deleteById(expenseEntityOptional.get().getId());
    }

    public List<ExpenseResponseDto> getExpenses(String type, LocalDate st, LocalDate en){
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ExpenseEntity> expenseEntityList = new ArrayList<>();
        if(type.contains("Past month")){
          expenseEntityList=  expenseRepo.findByUserAndStDateGreaterThanEqual(user,LocalDate.now().minusMonths(1));
        }
        else if(type.contains("Past three Months")){
            expenseEntityList=  expenseRepo.findByUserAndStDateGreaterThanEqual(user,LocalDate.now().minusMonths(3));
        }
        else if(type.contains("Last week")){
            expenseEntityList=  expenseRepo.findByUserAndStDateGreaterThanEqual(user,LocalDate.now().minusWeeks(1));
        }
        else{
            expenseEntityList = expenseRepo.findByUserAndStDateGreaterThanEqualAndEdDateLessThanEqual(user,st,en);
        }
        List<ExpenseResponseDto>responseDtos = new ArrayList<>();
        for(ExpenseEntity expense :expenseEntityList){
            responseDtos.add(convertExpenseEntToRes(expense));
        }
        return responseDtos;
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
