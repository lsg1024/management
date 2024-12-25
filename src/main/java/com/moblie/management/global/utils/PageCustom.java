package com.moblie.management.global.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class PageCustom<T> extends PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageCustom(@JsonProperty("content") List<T> content,
                    @JsonProperty("number") int page,
                    @JsonProperty("size") int size,
                    @JsonProperty("totalElements") long totalElements) {
        super(content, PageRequest.of(page, size), totalElements);
    }

    public PageCustom(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    public PageCustom(List<T> content, Pageable pageable, Long total) {
        super(content, pageable, total);
    }
}
