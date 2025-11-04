package com.codeartist.expenseapi.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JwtResponseDto {
    private String accessToken;
    private String refreshToken;
}
