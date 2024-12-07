package org.example.apptranslator.repository;

import org.example.apptranslator.entity.UserEntity;
import org.example.apptranslator.enums.BotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByTelegramId(Long telegramId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE UserEntity SET status = ?2 WHERE id = ?1")
    void updateUser(UUID userId, BotStatus botStatus);
}
