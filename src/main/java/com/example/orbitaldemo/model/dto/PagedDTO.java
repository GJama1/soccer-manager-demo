package com.example.orbitaldemo.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class PagedDTO<T> {

    public final int numberOfPages;

    public final long numberOfElements;

    public final List<T> content;

    public static <T> PagedDTO<T> of(Page<T> page) {
        return PagedDTO.<T>builder()
                .numberOfPages(page.getTotalPages())
                .numberOfElements(page.getTotalElements())
                .content(page.getContent())
                .build();
    }

}
