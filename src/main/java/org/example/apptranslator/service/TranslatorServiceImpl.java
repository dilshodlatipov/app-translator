package org.example.apptranslator.service;

import com.google.cloud.translate.v3.*;
import lombok.RequiredArgsConstructor;
import org.example.apptranslator.enums.Language;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranslatorServiceImpl implements TranslatorService {
    private final TranslationServiceClient client;

    @Value("${app.google.cloud.project-id}")
    private String projectId;

    @Override
    public String translate(String text, Language sourceLanguage, Language targetLanguage) {
        LocationName name = LocationName.of(projectId, "global");
        TranslateTextRequest request = TranslateTextRequest.newBuilder()
                .setParent(name.toString())
                .setMimeType("text/plain")
                .setSourceLanguageCode(sourceLanguage.getLocale().getLanguage())
                .setTargetLanguageCode(targetLanguage.getLocale().getLanguage())
                .addContents(text)
                .build();

        TranslateTextResponse response = client.translateText(request);
        return response.getTranslationsList().stream().map(Translation::getTranslatedText).reduce(String::concat).get();
    }
}
