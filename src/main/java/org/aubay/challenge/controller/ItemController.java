package org.aubay.challenge.controller;

import org.aubay.challenge.dto.ItemDto;
import org.aubay.challenge.service.ItemsService;
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
@RequestMapping("/item")
public class ItemController {

    Logger logger = LoggerFactory.getLogger(ItemController.class);

    private final ItemsService itemsService;

    @Autowired
    public ItemController(ItemsService itemsService){
        this.itemsService = itemsService;
    }

    /**
     * Create entities
     */
    @PostMapping(path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto, HttpServletRequest request){

        if(Objects.nonNull(itemDto.getId())){
            logger.error(String.format(Constants.CREATE_ERROR + Constants.CHECK_PAYLOAD, Constants.ITEM));
            return ResponseEntity.badRequest().body(null);
        }

        ItemDto response = itemsService.createItem(itemDto);
        URI location = null;

        if(!Objects.isNull(response)){
            location = ServletUriComponentsBuilder.fromRequestUri(request)
                    .path("/{name}")
                    .buildAndExpand(response.getName())
                    .toUri();
        }else{
            logger.error(String.format(Constants.CREATE_ERROR + Constants.CHECK_PAYLOAD, Constants.ITEM));
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieve entities
     */

    @GetMapping(
            path = {"/retrieve", "/retrieve/{name}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDto>> getItem(@PathVariable(required = false) String name){
        List<ItemDto> response;
        if(Objects.isNull(name)){
            response = itemsService.getAllItems();
        }else {
            response = itemsService.getItem(name);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Update entities
     */

    @PutMapping(path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> updateItem(@RequestBody ItemDto itemDto) {

        if(Objects.isNull(itemDto.getId())){
            logger.error(String.format(Constants.UPDATE_ERROR + Constants.CHECK_PAYLOAD, Constants.ITEM));
            return ResponseEntity.badRequest().body(null);
        }

        ItemDto response = null;
        try{

            response = itemsService.updateItem(itemDto);

        }catch(IllegalArgumentException e){
            logger.error("{}\n{}", String.format(Constants.UPDATE_ERROR + Constants.CHECK_PAYLOAD, Constants.ITEM), e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Delete entities
     */

    @DeleteMapping(path = "/delete",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteItem(@RequestBody ItemDto itemDto) {

        if(Objects.isNull(itemDto.getId())){
            logger.error(String.format(Constants.DELETE_ERROR + Constants.CHECK_PAYLOAD, Constants.ITEM));
            return HttpStatus.BAD_REQUEST;
        }

        try{
            itemsService.deleteItem(itemDto);

        }catch(Exception e){
            logger.error("{}\n{}", String.format(Constants.DELETE_ERROR + Constants.CHECK_PAYLOAD, Constants.ITEM), e.getMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }
}
