package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.APIResponse;
import com.example.dto.BookingRequest;
import com.example.dto.BookingResponse;
import com.example.enums.BookingStatus;
import com.example.service.BookingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired private BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR','USER')")
    public ResponseEntity<APIResponse<BookingResponse>> create(@Valid @RequestBody BookingRequest req,
                                                  Authentication auth) {
         
        BookingResponse res=bookingService.create(req,auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(APIResponse.success("Booking successfully created",res));
    }

    // @GetMapping
    // @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR')")
    // public ResponseEntity<List<BookingResponse>> getAll(@RequestParam(required = false) String status) {
    //     return ResponseEntity.ok(bookingService.getAll(status));
    // }

    // @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','USER')")
    // public ResponseEntity<BookingResponse> getById(@PathVariable Long id) {
    //     return ResponseEntity.ok(bookingService.getById(id));
    // }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','USER')")
    public ResponseEntity<APIResponse<List<BookingResponse>>> getByUser(@PathVariable Long userId,Authentication auth) {
    	List<BookingResponse> booking_response=bookingService.getByUserId(userId);
        return ResponseEntity.ok(APIResponse.success("All Bookings retrieved",booking_response));
    }

    @GetMapping("/confirmed")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','OPERATOR','USER')")
    public ResponseEntity<APIResponse<List<BookingResponse>>> getConfirmed() {
    	List<BookingResponse> booking_confirmed=bookingService.getConfirmedBooking();
        return ResponseEntity.ok(APIResponse.success("All Bookings retrieved",booking_confirmed));
        
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<APIResponse<BookingResponse>> update(@PathVariable Long id,
                                                  @Valid @RequestBody BookingRequest req,
                                                  Authentication auth) {
    	
    	BookingResponse res=bookingService.update(id,req,auth.getName());
        return ResponseEntity.ok(APIResponse.success("Booking Updated",res));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<BookingResponse>> changeStatus(@PathVariable Long id,
                                                        @Valid @RequestParam BookingStatus newStatus,
                                                        Authentication auth) {
    	BookingResponse res=bookingService.changeStatus(id,newStatus,auth.getName());
        return ResponseEntity.ok(APIResponse.success("Status Updated",res));
    }
}
