package com.example.project2.service;

import com.example.project2.entity.User;
import com.example.project2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final UserRepository repository;
    public Long login(String username, String password) {
        // 우선 null값으로 설정하고 에러에 대한 것은 spring security적용 후
        // 그것으로 처리하면 될듯
        User user = repository.findByUsername(username).orElse(null);

        if (user== null){ // 아이디가 존재하지 않는 경우
            return null;
        }
        else if(user.getPassword().equals(password)){ // 로그인 성공
            return user.getId();
        }else{ // 비밀번호가 일치하지 않는 경우
            return null;
        }
    }

    public boolean IsExistEmail(String email) {
        return repository.existsByEmail(email);
    }
}
