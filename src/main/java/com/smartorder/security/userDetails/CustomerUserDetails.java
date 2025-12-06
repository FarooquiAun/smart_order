package com.smartorder.security.userDetails;

import com.smartorder.security.model.Role;
import com.smartorder.security.model.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomerUserDetails implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final Set<GrantedAuthority> authorities=new HashSet<>();

    public CustomerUserDetails(User user){
        this.id=user.getId();
        this.username= user.getUsername();
        this.password=user.getPassword();
        this.enabled=user.isEnabled();
        for (Role role:user.getRoles()){
            this.authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
    }
    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public  String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
