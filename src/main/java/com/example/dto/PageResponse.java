package com.example.dto;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> data;         // the actual records
    private int     totalPages;   // so frontend knows how many pages exist
    private long    totalRecords; // total count in DB

    public static <T> PageResponse<T> from(Page<T> page) {
        PageResponse<T> response = new PageResponse<>();
        response.setData(page.getContent());
        response.setTotalPages(page.getTotalPages());
        response.setTotalRecords(page.getTotalElements());
        return response;
    }
}