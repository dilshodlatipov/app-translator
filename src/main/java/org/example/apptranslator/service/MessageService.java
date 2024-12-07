package org.example.apptranslator.service;

import lombok.RequiredArgsConstructor;
import org.example.apptranslator.entity.UserEntity;
import org.example.apptranslator.enums.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private static MessageSource messageSource;

    @Autowired
    public void set(MessageSource messageSource) {
        MessageService.messageSource = messageSource;
    }

    public static String message(UserEntity userEntity, String code) {
        return messageSource.getMessage(code, null, userEntity.getLanguage().getLocale());
    }

    public static String message(Language language, String code) {
        return messageSource.getMessage(code, null, language.getLocale());
    }
}
