//package com.bffservice.application.query.handler;
//
//import com.bffservice.domain.model.AggregatedUser;
//import com.bffservice.domain.repository.UserAuthRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class UserAuthQueryHandler {
//
//    private final UserAuthRepository userRepository;
//
//    public Optional<AggregatedUser> handle(UUID userId) {
//        return userRepository.findById(userId);
//    }
//
//    public List<AggregatedUser> handle() {
//        return userRepository.findAll();
//    }
//}
