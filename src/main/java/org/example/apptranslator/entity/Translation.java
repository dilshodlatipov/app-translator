package org.example.apptranslator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.apptranslator.entity.template.AbsUUIDEntity;
import org.example.apptranslator.enums.Language;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Translation extends AbsUUIDEntity {
    @Enumerated(EnumType.STRING)
    private Language sourceLanguage;

    @Enumerated(EnumType.STRING)
    private Language targetLanguage;

    private String text;

    private String translatedText;
}
