package com.devsong.server.user.service;

import com.devsong.server.jwt.JwtTokenProvider;
import com.devsong.server.user.dto.*;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.devsong.server.post.entity.*;
import com.devsong.server.post.repository.*;
import java.util.List;
import java.util.stream.Collectors;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    //DI
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostApplyRepository postApplyRepository;

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

    private String preview(String content, int limit) {
        if (content == null) return "";
        return content.length() > limit ? content.substring(0, limit) + "..." : content;
    }

    // 내가 쓴 글
    public List<MyPostDto> getMyPosts(Long userId) {
        return postRepository.findByUserIdOrderByIdDesc(userId).stream()
                .map(post -> MyPostDto.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .preview(preview(post.getContent(), 35))
                        .category(post.getCategory())
                        .username(post.getUser().getUsername())
                        .createdAt(post.getCreatedAt())
                        .closed(post.isClosed())
                        .like(postLikeRepository.countByPostId(post.getId()))
                        .comment(commentRepository.countByPostId(post.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    // 내가 댓글 단 글
    public List<MyPostDto> getMyCommentedPosts(Long userId) {
        return commentRepository.findByUserIdOrderByIdDesc(userId).stream()
                .map(comment -> {
                    var post = comment.getPost();
                    return MyPostDto.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .preview(preview(post.getContent(), 35))
                            .category(post.getCategory())
                            .username(post.getUser().getUsername())
                            .createdAt(post.getCreatedAt())
                            .closed(post.isClosed())
                            .like(postLikeRepository.countByPostId(post.getId()))
                            .comment(commentRepository.countByPostId(post.getId()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 내가 좋아요한 글
    public List<MyPostDto> getMyLikedPosts(Long userId) {
        return postLikeRepository.findByUserId(userId).stream()
                .map(PostLike::getPost)
                .map(post -> MyPostDto.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .preview(preview(post.getContent(), 35))
                        .category(post.getCategory())
                        .username(post.getUser().getUsername())
                        .createdAt(post.getCreatedAt())
                        .closed(post.isClosed())
                        .like(postLikeRepository.countByPostId(post.getId()))
                        .comment(commentRepository.countByPostId(post.getId()))
                        .build())
                .collect(Collectors.toList());
    }

    // 내가 지원한 글
    public List<MyPostDto> getMyAppliedPosts(Long userId) {
        return postApplyRepository.findByUserId(userId).stream()
                .map(PostApply::getPost)
                .map(post -> MyPostDto.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .preview(preview(post.getContent(), 35))
                        .category(post.getCategory())
                        .username(post.getUser().getUsername())
                        .createdAt(post.getCreatedAt())
                        .closed(post.isClosed())
                        .like(postLikeRepository.countByPostId(post.getId()))
                        .comment(commentRepository.countByPostId(post.getId()))
                        .build())
                .collect(Collectors.toList());
    }
}