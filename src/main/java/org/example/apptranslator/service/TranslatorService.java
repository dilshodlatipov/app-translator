package org.example.apptranslator.service;


import org.example.apptranslator.enums.Language;

public interface TranslatorService {
    String translate(String text, Language sourceLanguage, Language targetLanguage);
}
