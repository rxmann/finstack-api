package com.app.budgets.user;

import com.app.budgets.auth.model.CustomUserDetails;
import com.app.budgets.user.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserAuth {
    public User getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        return user;
    }
}
