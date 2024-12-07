package org.example.apptranslator.repository;

import org.example.apptranslator.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TranslationRepository extends JpaRepository<Translation, UUID> {
    Optional<Translation> findFirstByCreatedByIdAndDeletedFalseOrderByCreatedAtDesc(UUID createdBy);
}
