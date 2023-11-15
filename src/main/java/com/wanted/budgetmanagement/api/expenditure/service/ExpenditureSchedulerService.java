package com.wanted.budgetmanagement.api.expenditure.service;

import com.wanted.budgetmanagement.api.expenditure.dto.ExpenditureRecommendResponse;
import com.wanted.budgetmanagement.domain.user.entity.User;
import com.wanted.budgetmanagement.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ExpenditureSchedulerService {

    private final ExpenditureService expenditureService;

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    @Transactional
//    @Scheduled(initialDelay = 1000 ,fixedDelay = 60 * 60 * 1000)
    @Scheduled(cron = "0 0 8 * * ?", zone = "Asia/Seoul")
    public void expenditureRecommendScheduler() {
        List<User> users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            ExpenditureRecommendResponse response = expenditureService.expenditureRecommend(user);

            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("안녕하세요! BudgetManagement 서비스 입니다. 오늘의 지출 금액을 추천해드려요!");
            mailMessage.setText(response.getMessage());
            mailSender.send(mailMessage);
        }

    }
}
