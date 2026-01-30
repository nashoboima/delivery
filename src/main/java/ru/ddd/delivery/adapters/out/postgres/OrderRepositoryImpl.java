package ru.ddd.delivery.adapters.out.postgres;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.delivery.core.domain.model.order.OrderStatus;
import ru.ddd.delivery.core.ports.OrderRepository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpa;

    public OrderRepositoryImpl(OrderJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(Order order) {
        jpa.save(order);
    }


    @Override
    public Optional<Order> findById(UUID orderId) {
        return jpa.findById(orderId);
    }

    @Override
    public Optional<Order> findRandomWithCreatedStatus() {
        return jpa.findRandomCreatedOrder();
    }

    @Override
    public List<Order> findAllAssigned() {
        return jpa.findByStatus(OrderStatus.ASSIGNED);
    }

    @Override
    public void clean() {
        jpa.deleteAll();
    }

}
