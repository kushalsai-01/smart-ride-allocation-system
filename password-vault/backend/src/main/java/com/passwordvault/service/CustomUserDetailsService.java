package com.passwordvault.service;

import com.passwordvault.entity.User;
import com.passwordvault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Custom UserDetailsService implementation for Spring Security
 * Loads user details from the database for authentication
 */
@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmailIgnoreCase(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));
        
        return new CustomUserPrincipal(user);
    }
    
    /**
     * Custom UserDetails implementation
     */
    public static class CustomUserPrincipal implements UserDetails {
        
        private final User user;
        
        public CustomUserPrincipal(User user) {
            this.user = user;
        }
        
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authorities = new ArrayList<>();
            
            // Add default user role
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            
            // Add admin role if needed (can be extended with role table)
            if ("admin".equalsIgnoreCase(user.getUsername())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            
            return authorities;
        }
        
        @Override
        public String getPassword() {
            return user.getPasswordHash();
        }
        
        @Override
        public String getUsername() {
            return user.getUsername();
        }
        
        @Override
        public boolean isAccountNonExpired() {
            return user.getAccountNonExpired();
        }
        
        @Override
        public boolean isAccountNonLocked() {
            return user.getAccountNonLocked();
        }
        
        @Override
        public boolean isCredentialsNonExpired() {
            return user.getCredentialsNonExpired();
        }
        
        @Override
        public boolean isEnabled() {
            return user.getEnabled();
        }
        
        // Additional methods to access user data
        public Long getId() {
            return user.getId();
        }
        
        public String getEmail() {
            return user.getEmail();
        }
        
        public String getFullName() {
            return user.getFullName();
        }
        
        public User getUser() {
            return user;
        }
    }
}