package com.devsong.server.user.service;

import com.devsong.server.user.dto.*;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        //ReqeustDto를 Entity로 변환
        User userEntity = User.builder()
                .email(signupRequestDto.getEmail())
                .password(signupRequestDto.getPassword())
                .username(signupRequestDto.getUsername())
                .studentId(signupRequestDto.getStudentId())
                .major(signupRequestDto.getMajor())
                .build(); //변환 완료
        // 만약 bojid, githubid 있으면 넣기

        //UserRepository.save
        userRepository.save(userEntity);

        //UserRepository.FindByEmail로 id 찾기
        Long Id = userRepository.findByEmail(userEntity.getEmail()).getId();

        //id 를 ResponseDto로 변환 후 return
        return new SignupResponseDto(Id);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        //RequestDto에서 email 추출
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        //UserRepository.FindByEmail로 id 찾기
        User userEntity = userRepository.findByEmail(email);

        //UserRepository에서 비밀번호 일치하는지 확인
        //ResponseDto로 변환 후 return
        if (userEntity.getPassword().equals(password)) { //일치할 경우
            return LoginResponseDto.builder()
                    .id(userEntity.getId())
                    .message("Login Sucess")
                    .build();
        }
        else { //불일치할 경우
            return LoginResponseDto.builder()
                    .id(null)
                    .message("wrong password")
                    .build();
        }
    }
}
