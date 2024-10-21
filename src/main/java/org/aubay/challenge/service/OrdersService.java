package org.aubay.challenge.service;

import org.aubay.challenge.dto.ItemDto;
import org.aubay.challenge.dto.OrderDto;
import org.aubay.challenge.dto.UserDto;
import org.aubay.challenge.entity.Orders;
import org.aubay.challenge.mapper.OrderMapper;
import org.aubay.challenge.repository.OrdersRepository;
import org.aubay.challenge.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrdersService {

    Logger logger = LoggerFactory.getLogger(OrdersService.class);

    @Value("${email.subject}")
    private String emailSubject;

    @Value("${email.body}")
    private String emailBody;

    private final OrdersRepository ordersRepository;
    private final OrderMapper orderMapper;

    private final EmailSenderService emailSenderService;
    private final ItemsService itemsService;
    private final UsersService usersService;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository, OrderMapper orderMapper,
                         EmailSenderService emailSenderService, ItemsService itemsService,
                         UsersService usersService){
        this.ordersRepository = ordersRepository;
        this.orderMapper = orderMapper;
        this.emailSenderService = emailSenderService;
        this.itemsService = itemsService;
        this.usersService = usersService;
    }

    public OrderDto createOrder(OrderDto orderDto) throws IllegalArgumentException {

        if(orderDto.getQuantity() < 0){
            logger.error(String.format(Constants.CANNOT_BE_LESS_THAN_ZERO, Constants.ORDER));
            throw new IllegalArgumentException(String.format(Constants.CANNOT_BE_LESS_THAN_ZERO, Constants.ORDER));
        }

        ItemDto itemDto = itemsService.getItemDto(orderDto.getItem().getId());
        if(Objects.isNull(itemDto)){
            logger.error(String.format(Constants.INVALID_ID_ERROR, Constants.ITEM, orderDto.getItem().getId()));
            throw new IllegalArgumentException(String.format(Constants.INVALID_ID_ERROR, Constants.ITEM, orderDto.getItem().getId()));
        }

        UserDto userDto = usersService.getUserDto(orderDto.getUser().getId());
        if(Objects.isNull(userDto)){
            logger.error(String.format(Constants.INVALID_ID_ERROR, Constants.USER, orderDto.getUser().getId()));
            throw new IllegalArgumentException(String.format(Constants.INVALID_ID_ERROR, Constants.USER, orderDto.getUser().getId()));
        }

        orderDto.setIsComplete(false);
        orderDto.setCreationDate(Instant.now());

        OrderDto responseOrder = saveOrder(orderDto);

        if(itemDto.getCurrentStock() >= orderDto.getQuantity()){
            updateOrderStatus(responseOrder, userDto, itemDto);
            itemDto.setCurrentStock(itemDto.getCurrentStock() - orderDto.getQuantity());
            itemsService.updateItem(itemDto);
        }

        orderDto.setItem(itemDto);
        orderDto.setUser(userDto);
        return responseOrder;
    }

    public void updateOrderStatus(OrderDto orderDto, UserDto userDto, ItemDto itemDto) {

        orderDto.setIsComplete(true);
        updateOrder(orderDto);

        String emailLogMessage = String.format(Constants.EMAIL_SENT, userDto.getName(), userDto.getEmail(), orderDto.getId());

        emailSenderService.sendEmail(userDto.getEmail(),
                String.format(emailSubject, orderDto.getId()),
                String.format(emailBody, itemDto.getName(), orderDto.getQuantity()),
                emailLogMessage);

        logger.info(String.format(Constants.ORDER_COMPLETED, orderDto.getId()));

    }

    @Transactional
    private OrderDto saveOrder(OrderDto orderDto){
            return orderMapper.orderToOrderDto(
                    ordersRepository.saveAndFlush(
                            orderMapper.orderDtoToOrder(orderDto)
                    )
            );
    }

    public List<OrderDto> getAllOrders() {
        return orderMapper.orderToOrderDto(ordersRepository.findAll());
    }

    public List<OrderDto> getOrder(Long id) {
        List<OrderDto> response = new ArrayList<>();
        Optional<Orders> order = getOrderById(id);
        order.ifPresent(orders -> response.add(orderMapper.orderToOrderDto(orders)));
        return response;
    }

    private Optional<Orders> getOrderById(Long id) {
        return ordersRepository.findById(id);
    }

    public OrderDto updateOrder(OrderDto orderDto) throws IllegalArgumentException {

        if(orderDto.getQuantity() < 0){
            logger.error(String.format(Constants.CANNOT_BE_LESS_THAN_ZERO, Constants.ORDER));
            throw new IllegalArgumentException(String.format(Constants.CANNOT_BE_LESS_THAN_ZERO, Constants.ORDER));
        }

        Optional<Orders> databaseOrder = getOrderById(orderDto.getId());

        if(!databaseOrder.isPresent()){
            logger.error(String.format(Constants.INVALID_ID_ERROR, Constants.ORDER, orderDto.getId()));
            throw new IllegalArgumentException(String.format(Constants.INVALID_ID_ERROR, Constants.ORDER, orderDto.getId()));
        }

        if(databaseOrder.get().getIsComplete()){
            logger.error(String.format(Constants.CANNOT_ALTER_COMPLETED, Constants.ORDER));
            throw new IllegalArgumentException(String.format(Constants.CANNOT_ALTER_COMPLETED, Constants.ORDER));
        }

        return saveOrder(orderDto);

    }

    public void deleteOrder(OrderDto orderDto) {
        ordersRepository.delete(orderMapper.orderDtoToOrder(orderDto));
    }

    public OrderDto getOldestOrderByItem(Long itemId){

        Optional<Orders> oldestOrderByItem = ordersRepository.findAllIncompleteOrdersByItem(itemId).stream()
                .min(Comparator.comparing(Orders::getCreationDate));

        return orderMapper.orderToOrderDto(oldestOrderByItem.orElse(null));
    }
}
