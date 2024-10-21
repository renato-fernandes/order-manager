package org.aubay.challenge.controller;

import org.aubay.challenge.dto.StockMovementDto;
import org.aubay.challenge.service.StockMovementsService;
import org.aubay.challenge.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/stock")
public class StockMovementController {

    Logger logger = LoggerFactory.getLogger(StockMovementController.class);

    private final StockMovementsService stockMovementsService;

    @Autowired
    public StockMovementController(StockMovementsService stockMovementsService){
        this.stockMovementsService = stockMovementsService;
    }

    /**
     * Create entities
     */


    @PostMapping(path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockMovementDto> createStockMovement(@RequestBody StockMovementDto stockMovementDto, HttpServletRequest request) {

        if(Objects.nonNull(stockMovementDto.getId())){
            logger.error(String.format(Constants.CREATE_ERROR + Constants.CHECK_PAYLOAD, Constants.STOCK_MOVEMENT));
            return ResponseEntity.badRequest().body(null);
        }
        if(Objects.isNull(stockMovementDto.getItem().getId())){
            logger.error(String.format(Constants.CREATE_ERROR + Constants.NO_ITEM, Constants.STOCK_MOVEMENT));
            return ResponseEntity.badRequest().body(null);
        }

        StockMovementDto response = null;
        try {

            response = stockMovementsService.createStockMovement(stockMovementDto);

        } catch (IllegalArgumentException e) {
            logger.error("{}\n{}", String.format(Constants.CREATE_ERROR + Constants.CHECK_PAYLOAD, Constants.STOCK_MOVEMENT), e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

        URI location = null;

        if(!Objects.isNull(response)){
            location = ServletUriComponentsBuilder.fromRequestUri(request)
                    .path("/{id}")
                    .buildAndExpand(response.getId())
                    .toUri();
        }else{
            logger.error(String.format(Constants.CREATE_ERROR + Constants.CHECK_PAYLOAD, Constants.STOCK_MOVEMENT));
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieve entities
     */

    @GetMapping(
            path = {"/retrieve", "/retrieve/{id}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StockMovementDto>> getStockMovement(@PathVariable(required = false) Long id){
        List<StockMovementDto> response;
        if(Objects.isNull(id)){
            response = stockMovementsService.getAllStockMovements();
        }else {
            response = stockMovementsService.getStockMovement(id);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Update entities
     */

    @PutMapping(path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockMovementDto> updateStockMovement(@RequestBody StockMovementDto stockMovementDto) {

        if(Objects.isNull(stockMovementDto.getId())){
            logger.error(String.format(Constants.UPDATE_ERROR + Constants.CHECK_PAYLOAD, Constants.ORDER));
            return ResponseEntity.badRequest().body(null);
        }

        StockMovementDto response = null;
        try{

            response = stockMovementsService.updateStockMovement(stockMovementDto);

        }catch(IllegalArgumentException e){
            logger.error("{}\n{}", String.format(Constants.UPDATE_ERROR + Constants.CHECK_PAYLOAD, Constants.STOCK_MOVEMENT), e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }catch(Exception e){
            logger.error("{}\n{}", String.format(Constants.UPDATE_ERROR, Constants.STOCK_MOVEMENT), e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Delete entities
     */

    @DeleteMapping(path = "/delete",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteStockMovement(@RequestBody StockMovementDto stockMovementDto){

        if(Objects.isNull(stockMovementDto.getId())){
            logger.error(String.format(Constants.DELETE_ERROR + Constants.CHECK_PAYLOAD, Constants.STOCK_MOVEMENT));
            return HttpStatus.BAD_REQUEST;
        }

        try{
            stockMovementsService.deleteStockMovement(stockMovementDto);

        }catch(Exception e){
            logger.error("{}\n{}", String.format(Constants.DELETE_ERROR + Constants.CHECK_PAYLOAD, Constants.STOCK_MOVEMENT), e.getMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }

}
