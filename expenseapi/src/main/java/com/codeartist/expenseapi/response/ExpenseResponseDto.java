package com.codeartist.expenseapi.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data@Builder
public class ExpenseResponseDto {
    private String name;
    private String amount;
    private LocalDate stDate;
    private LocalDate    enDate;
}
