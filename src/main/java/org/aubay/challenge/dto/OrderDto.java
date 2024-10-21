package org.aubay.challenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.aubay.challenge.entity.StockMovements;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto {

    private Long id;
    private Instant creationDate;
    private Integer quantity;
    private Boolean isComplete;
    private List<StockMovements> stockMovements;

    private ItemDto item;
    private UserDto user;

}
