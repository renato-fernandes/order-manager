package org.aubay.challenge.mapper;

import org.aubay.challenge.dto.ItemDto;
import org.aubay.challenge.entity.Items;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto itemToItemDto(Items item);
    List<ItemDto> itemToItemDto(List<Items> item);

    Items itemDtoToItems(ItemDto itemDto);
    List<Items> itemDtoToItems(List<ItemDto> itemDto);
}
