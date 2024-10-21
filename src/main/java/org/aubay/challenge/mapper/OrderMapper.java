package org.aubay.challenge.mapper;

import org.aubay.challenge.dto.OrderDto;
import org.aubay.challenge.entity.Orders;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto orderToOrderDto(Orders order);
    List<OrderDto> orderToOrderDto(List<Orders> order);

    Orders orderDtoToOrder(OrderDto orderDto);
    List<Orders> orderDtoToOrder(List<OrderDto> orderDto);
}
