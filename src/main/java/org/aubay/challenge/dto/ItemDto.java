package org.aubay.challenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDto {

    private Long id;
    private String name;
    private Integer currentStock;
}
