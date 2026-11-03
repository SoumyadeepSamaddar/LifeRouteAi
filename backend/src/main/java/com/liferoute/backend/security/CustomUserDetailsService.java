package com.liferoute.backend.security;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.liferoute.backend.entity.Hospital;
import com.liferoute.backend.entity.User;
import com.liferoute.backend.repository.HospitalRepository;
import com.liferoute.backend.repository.UserRepository;
import com.liferoute.domain.enums.HospitalStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

        if (user.getHospitalId() != null) {
            Hospital hospital = hospitalRepository.findById(user.getHospitalId()).orElse(null);
            if (hospital != null && hospital.getStatus() == HospitalStatus.SUSPENDED) {
                throw new DisabledException("Your hospital account is currently suspended.");
            }
        }
        return new CustomUserDetails(user);
    }
}
