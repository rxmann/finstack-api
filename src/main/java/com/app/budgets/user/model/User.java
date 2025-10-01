package com.app.budgets.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "users", indexes = {
        @Index(name = "idx_provider_id_provider_type", columnList = "provider_id, provider_type")
})
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "password_hash", nullable = true)
    private String passwordHash;

    @Column(name = "is_active", nullable = false)
    private final Boolean isActive = true;

    @Column(name = "account_locked", nullable = true)
    private boolean accountLocked;

    @Column(nullable = false)
    private List<String> roles;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private AuthProviderType authProviderType;

}
