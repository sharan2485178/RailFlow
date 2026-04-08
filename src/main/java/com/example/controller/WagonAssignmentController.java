package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.WagonAssignmentRequest;
import com.example.dto.WagonAssignmentResponse;
import com.example.service.WagonAssignmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/wagon")
public class WagonAssignmentController {
	
	@Autowired
	private WagonAssignmentService wagonAssignmentService;
	
	@PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<WagonAssignmentResponse>> assign(
            @Valid @RequestBody WagonAssignmentRequest req) {

        WagonAssignmentResponse res = wagonAssignmentService.assign(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Wagon assigned successfully", res));
    }
	
	

}
