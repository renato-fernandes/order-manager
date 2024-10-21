package org.aubay.challenge.service;

import org.aubay.challenge.dto.ItemDto;
import org.aubay.challenge.dto.OrderDto;
import org.aubay.challenge.dto.StockMovementDto;
import org.aubay.challenge.entity.StockMovements;
import org.aubay.challenge.mapper.StockMovementMapper;
import org.aubay.challenge.repository.StockMovementsRepository;
import org.aubay.challenge.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StockMovementsService {

    Logger logger = LoggerFactory.getLogger(StockMovementsService.class);

    private final StockMovementsRepository stockMovementsRepository;
    private final StockMovementMapper stockMovementMapper;

    private final OrdersService ordersService;
    private final ItemsService itemsService;

    @Autowired
    public StockMovementsService(StockMovementsRepository stockMovementsRepository,
                                 StockMovementMapper stockMovementMapper, OrdersService ordersService,
                                 ItemsService itemsService){
        this.stockMovementsRepository = stockMovementsRepository;
        this.stockMovementMapper = stockMovementMapper;
        this.ordersService = ordersService;
        this.itemsService = itemsService;
    }

    public StockMovementDto createStockMovement(StockMovementDto stockMovementDto){

        if(stockMovementDto.getQuantity() < 0){
            logger.error(String.format(Constants.CANNOT_BE_LESS_THAN_ZERO, Constants.STOCK_MOVEMENT));
            throw new IllegalArgumentException(String.format(Constants.CANNOT_BE_LESS_THAN_ZERO, Constants.STOCK_MOVEMENT));
        }

        ItemDto itemDto = itemsService.getItemDto(stockMovementDto.getItem().getId());
        if(Objects.isNull(itemDto)){
            logger.error(String.format(Constants.INVALID_ID_ERROR, Constants.ITEM, stockMovementDto.getItem().getId()));
            throw new IllegalArgumentException(String.format(Constants.INVALID_ID_ERROR, Constants.ITEM, stockMovementDto.getItem().getId()));
        }

        OrderDto oldestOrderByItem = ordersService.getOldestOrderByItem(itemDto.getId());

        if(Objects.isNull(oldestOrderByItem)){
            logger.error(String.format(Constants.NO_INCOMPLETE_ORDER_FOR_ITEM, itemDto.getName(), itemDto.getId()));
            throw new IllegalArgumentException(String.format(Constants.NO_INCOMPLETE_ORDER_FOR_ITEM, stockMovementDto.getItem().getName(), stockMovementDto.getItem().getId()));
        }

        stockMovementDto.setOrder(oldestOrderByItem);

        updateItemCurrentStock(itemDto, stockMovementDto);

        stockMovementDto.setCreationDate(Instant.now());

        StockMovementDto response = saveStockMovement(stockMovementDto);

        logger.info(String.format(Constants.STOCK_MOVEMENT_CREATED,
                itemDto.getName(),
                itemDto.getId(),
                stockMovementDto.getOrder().getId(),
                stockMovementDto.getQuantity()));

        return response;

    }

    private void updateItemCurrentStock(ItemDto itemDto, StockMovementDto stockMovementDto) {

        int newStockQuantity = itemDto.getCurrentStock() + stockMovementDto.getQuantity();

        if(newStockQuantity >= stockMovementDto.getOrder().getQuantity()){

            ordersService.updateOrderStatus(
                    stockMovementDto.getOrder(),
                    stockMovementDto.getOrder().getUser(),
                    itemDto);

            newStockQuantity = newStockQuantity - stockMovementDto.getOrder().getQuantity();
        }

        itemDto.setCurrentStock(newStockQuantity);
        itemsService.updateItem(itemDto);

    }

    @Transactional
    private StockMovementDto saveStockMovement(StockMovementDto stockMovementDto){
        return stockMovementMapper.smToSmDto(
                stockMovementsRepository.saveAndFlush(
                        stockMovementMapper.smDtoToSm(stockMovementDto)
                )
        );
    }

    public List<StockMovementDto> getAllStockMovements() {
        return stockMovementMapper.smToSmDto(stockMovementsRepository.findAll());
    }

    public List<StockMovementDto> getStockMovement(Long id) {
        List<StockMovementDto> response = new ArrayList<>();
        Optional<StockMovements> stockMovements = getStockMovementById(id);
        stockMovements.ifPresent(stockMovement -> response.add(stockMovementMapper.smToSmDto(stockMovement)));
        return response;
    }

    public Optional<StockMovements> getStockMovementById(Long id){
        return stockMovementsRepository.findById(id);
    }

    public StockMovementDto updateStockMovement(StockMovementDto stockMovementDto) {

        if(stockMovementDto.getQuantity() < 0){
            logger.error(String.format(Constants.CANNOT_BE_LESS_THAN_ZERO, Constants.STOCK_MOVEMENT));
            throw new IllegalArgumentException(String.format(Constants.CANNOT_BE_LESS_THAN_ZERO, Constants.STOCK_MOVEMENT));
        }

        if(!getStockMovementById(stockMovementDto.getId()).isPresent()){
            logger.error(String.format(Constants.INVALID_ID_ERROR, Constants.STOCK_MOVEMENT, stockMovementDto.getId()));
            throw new IllegalArgumentException(String.format(Constants.INVALID_ID_ERROR, Constants.ORDER, stockMovementDto.getId()));
        }

        if(stockMovementDto.getOrder().getIsComplete()){
            logger.error(String.format(Constants.CANNOT_ALTER_COMPLETED, Constants.STOCK_MOVEMENT));
            throw new IllegalArgumentException(String.format(Constants.CANNOT_ALTER_COMPLETED, Constants.STOCK_MOVEMENT));
        }

        ItemDto currentItemDto = itemsService.getItemDto(stockMovementDto.getId());

        if(!stockMovementDto.getItem().getId().equals(currentItemDto.getId())){
            logger.error(Constants.CANNOT_ALTER_ITEM);
            throw new IllegalArgumentException(Constants.CANNOT_ALTER_ITEM);
        }

        StockMovementDto response = saveStockMovement(stockMovementDto);

        updateItemCurrentStock(currentItemDto, stockMovementDto);

        return response;
    }

    public void deleteStockMovement(StockMovementDto stockMovementDto) {

        if(stockMovementDto.getOrder().getIsComplete()){
            logger.error(String.format(Constants.CANNOT_ALTER_COMPLETED, Constants.STOCK_MOVEMENT));
            throw new IllegalArgumentException(String.format(Constants.CANNOT_ALTER_COMPLETED, Constants.STOCK_MOVEMENT));
        }

        ItemDto currentItemDto = itemsService.getItemDto(stockMovementDto.getId());

        int newQuantity = stockMovementDto.getQuantity() - (stockMovementDto.getQuantity() * 2);
        stockMovementDto.setQuantity(newQuantity);

        updateItemCurrentStock(currentItemDto, stockMovementDto);

        stockMovementsRepository.delete(stockMovementMapper.smDtoToSm(stockMovementDto));
    }
}
