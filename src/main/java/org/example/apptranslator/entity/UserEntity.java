package org.example.apptranslator.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.apptranslator.entity.template.AbsUUIDEntity;
import org.example.apptranslator.enums.BotStatus;
import org.example.apptranslator.enums.Language;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity extends AbsUUIDEntity {
    @Column(unique = true, nullable = false)
    private Long telegramId;

    private String firstName;

    private String lastName;

    private String username;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private BotStatus status;

    @Enumerated(EnumType.STRING)
    private Language language;
}
