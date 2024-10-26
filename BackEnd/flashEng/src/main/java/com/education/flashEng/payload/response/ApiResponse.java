package com.education.flashEng.payload.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ApiResponse<T> {
    private boolean isSuccess = true;
    private String message;
    private T data;
}
