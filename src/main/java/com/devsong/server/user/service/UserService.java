package com.devsong.server.user.service;

import com.devsong.server.jwt.JwtTokenProvider;
import com.devsong.server.user.dto.*;
import com.devsong.server.user.entity.TechStack;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.access.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    //DI
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        //ReqeustDto를 Entity로 변환
        User userEntity = User.builder()
                .email(signupRequestDto.getEmail())
                .password(signupRequestDto.getPassword())
                .username(signupRequestDto.getUsername())
                .studentId(signupRequestDto.getStudentId())
                .major(signupRequestDto.getMajor())
                .bojId(signupRequestDto.getBojId())
                .githubId(signupRequestDto.getGithubId())
                .techStack(signupRequestDto.getTechStack())
                .build(); //변환 완료

        //UserRepository.save DB에 저장
        userRepository.save(userEntity);

        //UserRepository.FindByEmail로 id 찾기
        Long Id = userRepository.findByEmail(userEntity.getEmail()).getId();

        //id 를 ResponseDto로 변환 후 return
        return new SignupResponseDto("SignUp Success");
    }

    // 로그인
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        //RequestDto에서 email 추출
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        //UserRepository.FindByEmail로 id 찾기
        User userEntity = userRepository.findByEmail(email);

        //UserRepository에서 비밀번호 일치하는지 확인
        //ResponseDto로 변환 후 return
        if (userEntity.getPassword().equals(password)) { //일치할 경우

            String token = jwtTokenProvider.createToken(userEntity); // jwt 발급

            return LoginResponseDto.builder()
                    .message("Login Sucess")
                    .token(token)
                    .build();
        }
        else { //불일치할 경우
            return LoginResponseDto.builder()
                    .message("wrong password")
                    .token(null)
                    .build();
        }
    }

    public EmailResponseDto checkEmail(EmailRequestDto emailRequestDto) {
        boolean isExist = (userRepository.findByEmail(emailRequestDto.getEmail()) != null);
        return new EmailResponseDto(!isExist);
    }

    @Transactional
    public UpdateTechStackResponseDto updateTechStack(Long userId, List<TechStack> incoming) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long loginUserId = (Long) auth.getPrincipal();

        if (!loginUserId.equals(userId)) {
            throw new AccessDeniedException("Not Authorized");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setTechStack(incoming == null ? Collections.emptyList() : incoming);

        return new UpdateTechStackResponseDto(user.getId(), user.getTechStack());
    }

}
