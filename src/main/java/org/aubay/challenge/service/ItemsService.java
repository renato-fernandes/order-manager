package org.aubay.challenge.service;

import org.aubay.challenge.dto.ItemDto;
import org.aubay.challenge.entity.Items;
import org.aubay.challenge.mapper.ItemMapper;
import org.aubay.challenge.repository.ItemsRepository;
import org.aubay.challenge.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ItemsService {



    private final ItemsRepository itemsRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemsService(ItemsRepository itemsRepository, ItemMapper itemMapper){
        this.itemsRepository = itemsRepository;
        this.itemMapper = itemMapper;
    }

    public ItemDto createItem(ItemDto itemDto) {
        return saveItem(itemDto);
    }

    @Transactional
    private ItemDto saveItem(ItemDto itemDto) {
        itemDto.setName(itemDto.getName().toLowerCase());
        return itemMapper.itemToItemDto(
                itemsRepository.saveAndFlush(
                        itemMapper.itemDtoToItems(itemDto)
                )
        );
    }

    public List<ItemDto> getAllItems() {
        return itemMapper.itemToItemDto(itemsRepository.findAll());
    }

    public List<ItemDto> getItem(String name) {
        return itemMapper.itemToItemDto(itemsRepository.findByName(name));
    }

    private Optional<Items> getItem(Long id) {
        return itemsRepository.findById(id);
    }

    public ItemDto getItemDto(Long id){
        Optional<Items> response = getItem(id);
        return itemMapper.itemToItemDto(response.orElse(null));
    }


    public ItemDto updateItem(ItemDto itemDto) {

        if(!getItem(itemDto.getId()).isPresent()){
            throw new IllegalArgumentException(String.format(Constants.INVALID_ID_ERROR, Constants.ITEM, itemDto.getId()));
        }
        return saveItem(itemDto);
    }

    public void deleteItem(ItemDto itemDto) {
        itemsRepository.delete(itemMapper.itemDtoToItems(itemDto));
    }
}
