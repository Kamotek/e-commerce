// np. w src/main/java/com/catalogservice/api/dto/PagedResult.java
package com.catalogservice.domain.model; // lub inny pakiet w catalog-service

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagedResult<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private int number;
    private int size;
    private boolean last;
}