package ru.ddd.delivery.core.application.commands;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ddd.delivery.core.domain.services.OrderDispatcher;
import ru.ddd.delivery.core.ports.CourierRepository;
import ru.ddd.delivery.core.ports.OrderRepository;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.UnitResult;

@Service
public class AssignOrderCommandHandlerImpl implements AssignOrderCommandHandler {
    private final CourierRepository courierRepository;
    private final OrderRepository orderRepository;
    private final OrderDispatcher orderDispatcher;

    public AssignOrderCommandHandlerImpl(CourierRepository courierRepository, OrderRepository orderRepository, OrderDispatcher orderDispatcher) {
        this.courierRepository = courierRepository;
        this.orderRepository = orderRepository;
        this.orderDispatcher = orderDispatcher;
    }

    @Transactional
    @Override
    public UnitResult<Error> handle() {
        var assignableCouriers = courierRepository.findAllAssignableCouriers();
        if (assignableCouriers.isEmpty()) {
            UnitResult.success();
        }

        var orderOpt = orderRepository.findRandomWithCreatedStatus();
        if (orderOpt.isEmpty()) {
            UnitResult.success();
        }

        var order = orderOpt.get();
        var dispatchResult = orderDispatcher.dispatch(order, assignableCouriers);
        if (dispatchResult.isFailure()) {
            UnitResult.failure(dispatchResult.getError());
        }

        var courier = dispatchResult.getValue();
        courierRepository.save(courier);

        orderRepository.save(order);

        return UnitResult.success();
        
    }

}