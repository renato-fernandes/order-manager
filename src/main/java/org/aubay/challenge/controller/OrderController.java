package org.aubay.challenge.controller;

import org.aubay.challenge.dto.OrderDto;
import org.aubay.challenge.service.OrdersService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/order")
public class OrderController {

    Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrdersService ordersService;

    @Autowired
    public OrderController(OrdersService ordersService){
        this.ordersService = ordersService;
    }

    /**
     * Create entities
     */
    @PostMapping(path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto, HttpServletRequest request) {

        if(Objects.nonNull(orderDto.getId())){
            logger.error(String.format(Constants.CREATE_ERROR + Constants.CHECK_PAYLOAD, Constants.ORDER));
            return ResponseEntity.badRequest().body(null);
        }
        if(Objects.isNull(orderDto.getItem().getId()) && Objects.isNull(orderDto.getUser().getId())){
            logger.error(String.format(Constants.CREATE_ERROR + Constants.NO_ITEM, Constants.ORDER));
            return ResponseEntity.badRequest().body(null);
        }

        OrderDto response = null;
        try {

            response = ordersService.createOrder(orderDto);

        } catch (IllegalArgumentException e) {
            logger.error("{}\n{}", String.format(Constants.CREATE_ERROR + Constants.CHECK_PAYLOAD, Constants.ORDER), e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

        URI location = null;

        if(!Objects.isNull(response)){
            location = ServletUriComponentsBuilder.fromRequestUri(request)
                    .path("/{id}")
                    .buildAndExpand(response.getId())
                    .toUri();
        }else{
            logger.error(String.format(Constants.CREATE_ERROR + Constants.CHECK_PAYLOAD, Constants.ORDER));
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
    public ResponseEntity<List<OrderDto>> getOrder(@PathVariable(required = false) Long id){
        List<OrderDto> response;
        if(Objects.isNull(id)){
            response = ordersService.getAllOrders();
        }else {
            response = ordersService.getOrder(id);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Update entities
     */

    @PutMapping(path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto) {

        if(Objects.isNull(orderDto.getId())){
            logger.error(String.format(Constants.UPDATE_ERROR + Constants.CHECK_PAYLOAD, Constants.ORDER));
            return ResponseEntity.badRequest().body(null);
        }

        OrderDto response = null;
        try{

            response = ordersService.updateOrder(orderDto);

        }catch(IllegalArgumentException e){
            logger.error("{}\n{}", String.format(Constants.UPDATE_ERROR + Constants.CHECK_PAYLOAD, Constants.ORDER), e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }catch(Exception e){
            logger.error("{}\n{}", String.format(Constants.UPDATE_ERROR, Constants.ORDER), e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Delete entities
     */

    @DeleteMapping(path = "/delete",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteOrder(@RequestBody OrderDto orderDto){

        if(Objects.isNull(orderDto.getId())){
            logger.error(String.format(Constants.DELETE_ERROR + Constants.CHECK_PAYLOAD, Constants.ORDER));
            return HttpStatus.BAD_REQUEST;
        }

        try{
            ordersService.deleteOrder(orderDto);

        }catch(Exception e){
            logger.error("{}\n{}", String.format(Constants.DELETE_ERROR + Constants.CHECK_PAYLOAD, Constants.ORDER), e.getMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }
}
