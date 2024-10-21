package org.aubay.challenge.controller;

import org.aubay.challenge.dto.UserDto;
import org.aubay.challenge.service.UsersService;
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
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UsersService usersService;

    @Autowired
    public UserController(UsersService usersService){
        this.usersService = usersService;
    }

    /**
     * Create entities
     */
    @PostMapping(path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto, HttpServletRequest request) {

        if(Objects.nonNull(userDto.getId())){
            logger.error(String.format(Constants.CREATE_ERROR + Constants.CHECK_PAYLOAD, Constants.USER));
            return ResponseEntity.badRequest().body(null);
        }

        UserDto response = usersService.createUser(userDto);
        URI location = null;

        if(!Objects.isNull(response)){
            location = ServletUriComponentsBuilder.fromRequestUri(request)
                    .path("/{email}")
                    .buildAndExpand(response.getEmail())
                    .toUri();
        }else{
            logger.error(String.format(Constants.CREATE_ERROR + Constants.CHECK_PAYLOAD, Constants.USER));
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieve entities
     */

    @GetMapping(
            path = {"/retrieve", "/retrieve/{email}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> getUser(@PathVariable(required = false) String email){
        List<UserDto> response;
        if(Objects.isNull(email)){
            response = usersService.getAllUsers();
        }else {
            response = usersService.getUser(email);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Update entities
     */

    @PutMapping(path = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto){

        if(Objects.isNull(userDto.getId())){
            logger.error(String.format(Constants.UPDATE_ERROR + Constants.CHECK_PAYLOAD, Constants.USER));
            return ResponseEntity.badRequest().body(null);
        }

        UserDto response = null;
        try{

            response = usersService.updateUser(userDto);

        }catch(IllegalArgumentException e){
            logger.error("{}\n{}", String.format(Constants.UPDATE_ERROR + Constants.CHECK_PAYLOAD, Constants.USER), e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * Delete entities
     */

    @DeleteMapping(path = "/delete",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteUser(@RequestBody UserDto userDto){

        if(Objects.isNull(userDto.getId())){
            logger.error(String.format(Constants.DELETE_ERROR + Constants.CHECK_PAYLOAD, Constants.USER));
            return HttpStatus.BAD_REQUEST;
        }

        try{
            usersService.deleteUser(userDto);

        }catch(Exception e){
            logger.error("{}\n{}", String.format(Constants.DELETE_ERROR + Constants.CHECK_PAYLOAD, Constants.USER), e.getMessage());
            return HttpStatus.BAD_REQUEST;
        }

        return HttpStatus.OK;
    }
}
