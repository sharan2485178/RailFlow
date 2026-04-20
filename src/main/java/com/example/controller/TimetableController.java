package com.example.controller;

import java.util.List;

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
import com.example.dto.PageResponse;
import com.example.dto.TimetableAssetsResponse;
import com.example.dto.TimetableRequest;
import com.example.dto.TimetableResponse;
import com.example.dto.TimetableStatusRequest;
import com.example.dto.TimetableUpdateRequest;
import com.example.service.TimetableService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/timetables")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<TimetableResponse>> create(
            @Valid @RequestBody TimetableRequest req,
            Authentication auth) {

        TimetableResponse res = timetableService.create(req, auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Timetable created successfully", res));
    }

    
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<PageResponse<TimetableResponse>>>getAllByPageAndSort(@RequestParam(name="page")int page,@RequestParam(name="size") int size, @RequestParam(name="sortBy") String sortBy){
    	     PageResponse<TimetableResponse> res=timetableService.getAllByPageAndSort(page,size,sortBy);
    	     return ResponseEntity.ok(APIResponse.success("Retrieved Successfully",res));
    }
    
   
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','ENGINEER','MAINTENANCE')")
    public ResponseEntity<APIResponse<List<TimetableResponse>>> getAll(
            @RequestParam(required = false) String status) {

        List<TimetableResponse> res = timetableService.getAll(status);
        return ResponseEntity.ok(
                APIResponse.success("Timetables fetched successfully", res));
    }

   

    //id - timetableId
    @GetMapping("/{id}/assets")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER','ENGINEER','MAINTENANCE','YARD_MANAGER')")
    public ResponseEntity<APIResponse<TimetableAssetsResponse>> getAssets(
            @PathVariable Long id) {

        TimetableAssetsResponse res = timetableService.getAssets(id);
        return ResponseEntity.ok(
                APIResponse.success("Assets fetched successfully", res));
    }

   
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<TimetableResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody TimetableUpdateRequest req,
            Authentication auth) {

        TimetableResponse res = timetableService.update(id, req, auth.getName());
        return ResponseEntity.ok(
                APIResponse.success("Timetable updated successfully", res));
    }
    @PutMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DISPATCHER')")
    public ResponseEntity<APIResponse<TimetableResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody TimetableStatusRequest req,
            Authentication auth) {

        TimetableResponse res = timetableService.updateStatus(id, req, auth.getName());
        return ResponseEntity.ok(
                APIResponse.success("Timetable status updated successfully", res));
    }
    
    
    
}