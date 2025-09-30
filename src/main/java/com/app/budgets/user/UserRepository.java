package com.app.budgets.user;

import com.app.budgets.user.model.AuthProviderType;
import com.app.budgets.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByProviderIdAndAuthProviderType(String providerId, AuthProviderType authProviderType);
}
