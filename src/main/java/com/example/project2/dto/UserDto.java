package com.example.project2.dto;

import com.example.project2.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String profileImg;

    public static UserDto fromEntity(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .profileImg(entity.getProfileImg())
                .build();
    }
}
