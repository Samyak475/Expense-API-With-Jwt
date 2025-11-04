package com.codeartist.expenseapi.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data@Builder
public class ExpenseResponseDto {
    private String name;
    private String amount;
    private Date stDate;
    private Date    enDate;
}
