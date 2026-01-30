package ru.ddd.delivery.core.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.ddd.delivery.core.domain.model.order.Order;

public interface OrderRepository {

    public void save(Order order);

    public Optional<Order> findById(UUID orderId);

    public Optional<Order> findRandomWithCreatedStatus();

    public List<Order> findAllAssigned();

    public void clean();

}
