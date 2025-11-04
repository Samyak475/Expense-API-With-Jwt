package com.codeartist.expenseapi.requestDto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ExpenseRequestDto {
    private String name;
    private String amount;
    private Date stDate;
    private Date enDate;
}
