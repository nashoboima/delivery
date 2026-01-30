package ru.ddd.delivery.adapters.out.postgres;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.delivery.core.domain.model.order.OrderStatus;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

    @Query("""
        SELECT o 
        FROM Order o 
        WHERE o.status = 'CREATED' 
        ORDER BY RANDOM() 
        LIMIT 1
    """)
    Optional<Order> findRandomCreatedOrder();

    List<Order> findByStatus(OrderStatus status);

}
