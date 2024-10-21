package org.aubay.challenge.repository;

import org.aubay.challenge.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT o FROM orders o JOIN o.item i WHERE i.id = :itemId AND o.isComplete = false ORDER BY o.creationDate DESC")
    List<Orders> findAllIncompleteOrdersByItem(@Param("itemId") Long itemId);
}
