package org.example.apptranslator.service;


import org.example.apptranslator.entity.UserEntity;
import org.example.apptranslator.enums.BotStatus;

import java.util.UUID;

public interface UserService {
    UserEntity save(org.telegram.telegrambots.meta.api.objects.User user);

    UserEntity checkUser(org.telegram.telegrambots.meta.api.objects.User user);

    UserEntity save(UserEntity user);

    void setStatus(UUID userId, BotStatus botStatus);
}
