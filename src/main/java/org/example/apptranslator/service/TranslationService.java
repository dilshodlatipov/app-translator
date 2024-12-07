package org.example.apptranslator.service;

import org.example.apptranslator.entity.Translation;
import org.example.apptranslator.entity.UserEntity;

import java.util.UUID;

public interface TranslationService {
    Translation getLastTranslation(UserEntity user);

    Translation saveTranslation(UserEntity user, Translation translation);

    Translation updateTranslation(UUID translationId, Translation translation);
}
