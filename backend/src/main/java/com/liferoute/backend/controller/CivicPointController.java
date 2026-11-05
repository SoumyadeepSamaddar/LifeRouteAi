package com.liferoute.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.liferoute.backend.dto.request.AwardCivicPointsRequestDTO;
import com.liferoute.backend.dto.response.CivicPointResponseDTO;
import com.liferoute.backend.dto.response.CivicSummaryResponseDTO;
import com.liferoute.backend.security.CustomUserDetails;
import com.liferoute.backend.service.CivicPointService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CivicPointController {

	private final CivicPointService civicPointService;
	
	@PostMapping("/admin/civic-points")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<CivicPointResponseDTO> awardCivicPoints(
            @Valid @RequestBody AwardCivicPointsRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            HttpServletRequest servletRequest) {
        return ResponseEntity.ok(
                civicPointService.awardPoints(request, currentUser.getId(), servletRequest.getRemoteAddr())
        );
    }
	
	@GetMapping("/admin/civic-points/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HOSPITAL')")
    public ResponseEntity<CivicSummaryResponseDTO> getUserCivicSummary(@PathVariable Long userId) {
        return ResponseEntity.ok(civicPointService.getUserCivicSummary(userId));
    }
	
	@GetMapping("/me/civic-points")
    public ResponseEntity<CivicSummaryResponseDTO> getMyCivicSummary(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(civicPointService.getUserCivicSummary(currentUser.getId()));
    }
}
