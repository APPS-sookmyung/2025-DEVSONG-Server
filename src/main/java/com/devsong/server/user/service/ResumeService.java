package com.devsong.server.user.service;

import com.devsong.server.user.dto.*;
import com.devsong.server.user.entity.Resume;
import com.devsong.server.user.entity.User;
import com.devsong.server.user.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.devsong.server.user.dto.UpdateResumeRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;

    private Long getLoginUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthenticated");
        }

        return (Long) auth.getPrincipal();
    }

    //이력서 조회
    @Transactional(readOnly = true)
    public ResumeResponseDto getResume() {

        Long userId = getLoginUserId();

        Resume resume = resumeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));

        User user = resume.getUser();

        return ResumeResponseDto.builder()
                .profileImage(resume.getProfileImage())
                .username(user.getUsername())
                .studentId(user.getStudentId())
                .major(user.getMajor())
                .bojId(user.getBojId())
                .githubId(user.getGithubId())
                .techStack(user.getTechStack())
                .interests(resume.getInterests())
                .content(resume.getContent())
                .build();
    }

    //이력서 수정
    @Transactional
    public UpdateResumeResponseDto updateResume(UpdateResumeRequestDto dto) {

        Long userId = getLoginUserId();

        Resume resume = resumeRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));

        resume.update(
                dto.getInterests(),
                dto.getContent(),
                dto.getProfileImage()
        );

        if (dto.getTechStack() != null) {
            resume.getUser().updateTechStack(dto.getTechStack());
        }

        return UpdateResumeResponseDto.builder()
                .message("Update Success")
                .build();
    }

}
