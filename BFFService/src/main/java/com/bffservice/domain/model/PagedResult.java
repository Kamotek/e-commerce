package com.bffservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


/**
 * Represents a paginated result of a query.
 * This is a generic container that holds a page with pagination metadata.
 * Used mostly with bigger volumes such as listing all products in catalog
 *
 * @param <T> the type of the content in the page.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PagedResult<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private boolean last;
}
