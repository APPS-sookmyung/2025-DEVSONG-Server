package com.devsong.server.user.service;

import com.devsong.server.user.dto.EmailRequestDto;
import com.devsong.server.user.dto.EmailResponseDto;
import com.devsong.server.user.dto.EmailVerifyRequestDto;
import com.devsong.server.user.entity.EmailVerification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.devsong.server.user.repository.*;

import java.time.LocalDateTime;
import java.security.SecureRandom;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository verificationRepository; // 추가
    private final JavaMailSender mailSender;

    public String sendEmail(EmailRequestDto emailRequestDto) {
        String email = emailRequestDto.getEmail();

        // 1. 이메일 중복 체크
        if (userRepository.existsByEmail(email)) {
            return "This Email is already used";
        }

        // 2. 인증번호 생성 및 DB 저장
        String verificationCode = generateCode();

        // 기존에 발송한 이메일 정보가 있다면 업데이트, 없다면 새로 생성
        EmailVerification verification = verificationRepository.findById(email)
                .orElse(new EmailVerification(email, verificationCode, LocalDateTime.now().plusMinutes(5)));

        verification.updateCode(verificationCode, 5); // 5분 유효
        verificationRepository.save(verification);

        // 3. 이메일 발송
        try {
            sendMail(email, verificationCode);
            return "Sended code";
        } catch (MessagingException e) {
            return "Sending Fail";
        }
    }

    // 인증번호 검증 로직 완성
    public EmailResponseDto verifyEmail(EmailVerifyRequestDto emailVerifyRequestDto) {
        String email = emailVerifyRequestDto.getEmail();
        String inputCode = emailVerifyRequestDto.getCode();

        // 1. 해당 이메일로 발급된 인증번호가 있는지 조회
        EmailVerification verification = verificationRepository.findById(email)
                .orElse(null);

        // 2. 일치 여부 확인
        if (verification == null) {
            // 발급된 적 없는 이메일
            return new EmailResponseDto(false);
        }

        if (verification.isExpired()) {
            // 시간 만료
            verificationRepository.delete(verification); // 만료된 데이터 삭제
            return new EmailResponseDto(false);
        }

        if (!verification.getCode().equals(inputCode)) {
            // 번호 불일치
            return new EmailResponseDto(false);
        }

        // 3. 인증 성공 시 데이터 삭제 (또는 인증 완료 플래그 처리)
        verificationRepository.delete(verification);

        return new EmailResponseDto(true);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }

    private void sendMail(String email, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("[DevSong] 회원가입 인증번호 안내");

        String content = "<h3>안녕하세요, DevSong입니다.</h3>" +
                "<p>회원가입을 위해 아래 인증번호를 입력해 주세요.</p>" +
                "<h2 style='color: blue;'>" + code + "</h2>" +
                "<p>인증번호는 5분간 유효합니다.</p>";

        helper.setText(content, true);
        mailSender.send(message);
    }
}