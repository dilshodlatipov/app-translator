package org.example.apptranslator.service;

import lombok.RequiredArgsConstructor;
import org.example.apptranslator.entity.Translation;
import org.example.apptranslator.entity.UserEntity;
import org.example.apptranslator.exceptions.TelegramException;
import org.example.apptranslator.repository.TranslationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {
    private final TranslationRepository translationRepository;

    @Override
    public Translation getLastTranslation(UserEntity user) {
        return translationRepository.findFirstByCreatedByIdAndDeletedFalseOrderByCreatedAtDesc(user.getId())
                .orElseThrow(() -> TelegramException.restThrow(MessageService.message(user, "translation.not-found"), user.getTelegramId()));
    }

    @Override
    public Translation saveTranslation(UserEntity user, Translation translation) {
        translation.setCreatedById(user.getId());
        translation.setUpdatedById(user.getId());
        return translationRepository.save(translation);
    }

    @Override
    public Translation updateTranslation(UUID translationId, Translation translation) {
        Translation translationOld = translationRepository.findById(translationId).orElseThrow(RuntimeException::new);
        translationOld.setTargetLanguage(translation.getTargetLanguage());
        translationOld.setSourceLanguage(translation.getSourceLanguage());
        translationOld.setText(translation.getText());
        translationOld.setTranslatedText(translation.getTranslatedText());
        return translationRepository.save(translationOld);
    }
}
