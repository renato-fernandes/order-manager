package org.aubay.challenge.mapper;

import org.aubay.challenge.dto.StockMovementDto;
import org.aubay.challenge.entity.StockMovements;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {

    StockMovementDto smToSmDto(StockMovements stockMovement);
    List<StockMovementDto> smToSmDto(List<StockMovements> stockMovement);

    StockMovements smDtoToSm(StockMovementDto stockMovementDto);
    List<StockMovements> smDtoToSm(List<StockMovementDto> stockMovementDto);

}
