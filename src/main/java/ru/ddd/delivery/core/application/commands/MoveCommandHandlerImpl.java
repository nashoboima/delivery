package ru.ddd.delivery.core.application.commands;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.delivery.core.ports.CourierRepository;
import ru.ddd.delivery.core.ports.OrderRepository;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.GeneralErrors;
import ru.ddd.libs.errs.UnitResult;

@Service
public class MoveCommandHandlerImpl implements MoveCommandHandler {
    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;

    public MoveCommandHandlerImpl(CourierRepository courierRepository, OrderRepository orderRepository) {
        this.courierRepository = courierRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public UnitResult<Error> handle() {
        // Если нет доставляемых(назначенных) товаров, то не двигаем курьеров
        var assignedOrders = orderRepository.findAllAssigned();
        if (assignedOrders.isEmpty()) {
            UnitResult.success();
        }

        for(Order order: assignedOrders){
            UUID courierId = order.getCourierId();
            var courierOpt = courierRepository.findById(courierId);
            if (courierOpt.isEmpty()) {
                return UnitResult.failure(GeneralErrors.notFound("courier", courierId));
            }
            var courier = courierOpt.get();
            var moveResult = courier.move(order.getLocation());
            if (moveResult.isFailure()) {
                return UnitResult.failure(moveResult.getError());
            }
            if (courier.getLocation().equals(order.getLocation())) {
                var completeResult = order.complete();
                if (completeResult.isFailure()) {
                    return UnitResult.failure(completeResult.getError());
                }
                courier.completeOrder(order);
            }
        }
        return UnitResult.success();
        
    }

}