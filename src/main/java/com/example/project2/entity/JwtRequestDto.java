package com.example.project2.entity;

import lombok.Data;

@Data
public class JwtRequestDto {
    private String username;
    private String password;
}
