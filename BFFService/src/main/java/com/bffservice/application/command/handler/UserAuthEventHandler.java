//package com.bffservice.application.command.handler;
//
//import com.bffservice.application.command.model.UserLoggedInEvent;
//import com.bffservice.application.command.model.UserRegisteredEvent;
//import com.bffservice.domain.model.AggregatedUser;
//import com.bffservice.domain.repository.UserAuthRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@Component
//@Transactional
//@RequiredArgsConstructor
//public class UserAuthEventHandler {
//
//    private final UserAuthRepository userRepository;
//
//    public void handleRegistration(UserRegisteredEvent event) {
//        AggregatedUser user = new AggregatedUser(
//                event.getUserId(),
//                event.getEmail(),
//                event.getFirstName(),
//                event.getLastName(),
//                LocalDateTime.now(),
//                null
//        );
//        userRepository.save(user);
//    }
//
//    public void handleLogin(UserLoggedInEvent event) {
//        userRepository.findById(event.getUserId()).ifPresent(user -> {
//            user.setLastLogin(LocalDateTime.now());
//            userRepository.save(user);
//        });
//    }
//}