package org.aubay.challenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockMovementDto {

    private Long id;
    private Instant creationDate;
    private Integer quantity;

    private ItemDto item;
    private OrderDto order;
}
