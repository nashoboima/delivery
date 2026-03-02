package ru.ddd.delivery.core.application.commands;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ddd.delivery.core.GeoClient;
import ru.ddd.delivery.core.domain.model.Volume;
import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.delivery.core.ports.OrderRepository;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

@Service
public class CreateOrderCommandHandlerImpl implements CreateOrderCommandHandler {
    private final OrderRepository orderRepository;
    private final GeoClient geoClient;

    public CreateOrderCommandHandlerImpl(OrderRepository orderRepository, GeoClient geoClient) {
        this.orderRepository = orderRepository;
        this.geoClient = geoClient;
    }

    @Transactional
    @Override
    public Result<UUID, Error> handle(CreateOrderCommand command) {
        // Восстанавливаем агрегат, если нет, то создаем
        var orderOpt = orderRepository.findById(command.getOrderId());
        if (orderOpt.isEmpty()) {
            var getLocationResult = geoClient.getLocation(command.getStreet());
            if (getLocationResult.isFailure()) {
                return Result.failure(getLocationResult.getError());
            }
            var createVolumeResult = Volume.create(command.getVolume());
            if (createVolumeResult.isFailure()) {
                return Result.failure(createVolumeResult.getError());
            }
            var orderCreateResult = Order.create(command.getOrderId(), getLocationResult.getValue(), createVolumeResult.getValue());
            if (orderCreateResult.isFailure())
                return Result.failure(orderCreateResult.getError());
            var order = orderCreateResult.getValue();

            orderRepository.save(order);

            return Result.success(order.getId());
        }

        // Если заказ уже есть, просто возвращаем его id
        return Result.success(orderOpt.get().getId());
    }
}