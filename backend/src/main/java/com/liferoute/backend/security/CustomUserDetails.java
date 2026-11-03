package com.liferoute.backend.security;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.liferoute.backend.entity.User;
import com.liferoute.domain.enums.Role;
import com.liferoute.domain.enums.StaffRole;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {
	private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final Role role;
    private final StaffRole staffRole;
    private final Long hospitalId;
    private final boolean active;
    private final Instant lockedUntil;

public CustomUserDetails(User user) {
    this.id = user.getId();
    this.name = user.getName();
    this.email = user.getEmail();
    this.password = user.getPasswordHash();
    this.role = user.getRole();
    this.staffRole = user.getStaffRole();
    this.hospitalId = user.getHospitalId();
    this.active = user.isActive();
    this.lockedUntil = user.getLockedUntil();
}

@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
    if (staffRole != null) {
        authorities.add(new SimpleGrantedAuthority("STAFF_" + staffRole.name()));
    }
    return authorities;
}

@Override
public String getPassword() { return password; }

@Override
public String getUsername() { return email; }

@Override
public boolean isAccountNonExpired() { return true; }

@Override
public boolean isAccountNonLocked() {
    return lockedUntil == null || lockedUntil.isBefore(Instant.now());
}

@Override
public boolean isCredentialsNonExpired() { return true; }

@Override
public boolean isEnabled() { return active; }
}