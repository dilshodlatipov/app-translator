package org.example.apptranslator.service;

import lombok.RequiredArgsConstructor;
import org.example.apptranslator.entity.UserEntity;
import org.example.apptranslator.enums.BotStatus;
import org.example.apptranslator.enums.Language;
import org.example.apptranslator.mapper.UserMapper;
import org.example.apptranslator.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserEntity save(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        UserEntity userEntity = userMapper.toUserEntity(telegramUser);
        userEntity.setLanguage(Language.ENGLISH);
        userEntity.setStatus(BotStatus.PAGE_ZERO);
        return userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public UserEntity checkUser(org.telegram.telegrambots.meta.api.objects.User telegramUser) {
        Optional<UserEntity> userOptional = userRepository.findByTelegramId(telegramUser.getId());
        return userOptional.orElseGet(() -> save(telegramUser));
    }

    @Override
    @Transactional
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void setStatus(UUID userId, BotStatus botStatus) {
        userRepository.updateUser(userId, botStatus);
    }
}
