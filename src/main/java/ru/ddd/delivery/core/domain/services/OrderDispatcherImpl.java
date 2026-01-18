package ru.ddd.delivery.core.domain.services;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.delivery.core.domain.model.courier.Courier;
import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Except;
import ru.ddd.libs.errs.Result;
import ru.ddd.libs.errs.UnitResult;

@Service
public class OrderDispatcherImpl implements OrderDispatcher {

    @Override
    public Result<Courier, Error> dispatch(Order order, List<Courier> couriers) {
        Except.againstNull(order, "order");
        Except.againstNullOrEmpty(couriers, "couriers");

        OptionalInt firstAbleToTakeOrderIndex = IntStream.range(0, couriers.size())
            .filter(i -> {
                Courier currentCourier = couriers.get(i);
                Result<Boolean, Error> canTakeOrderResult = currentCourier.canTakeOrder(order);
                return canTakeOrderResult.isSuccess();
            })
            .findFirst();

        if (firstAbleToTakeOrderIndex.isEmpty()) {
            return Result.failure(Errors.nobodyCanTakeOrder());
        }

        Courier fastestCourier = couriers.get(firstAbleToTakeOrderIndex.getAsInt());
        Location orderLocation = order.getLocation();
        for (int i = firstAbleToTakeOrderIndex.getAsInt() + 1; i < couriers.size(); i++) {
            Courier currentCourier = couriers.get(i);
            Result<Boolean, Error> canTakeOrderResult = currentCourier.canTakeOrder(order);
            if (canTakeOrderResult.isSuccess()) {
                Result<Double, Error> result1 = currentCourier.calculateTimeToLocation(orderLocation);
                if (result1.isFailure()) {
                    return Result.failure(result1.getError());
                }
                Result<Double, Error> result2 = fastestCourier.calculateTimeToLocation(orderLocation);
                if (result2.isFailure()) {
                    return Result.failure(result2.getError());
                }

                if (Double.compare(result1.getValue(), result2.getValue()) < 0) {
                    fastestCourier = currentCourier;
                }
            }
        }

        UnitResult<Error> orderAssignResult = order.assign(fastestCourier);
        if (orderAssignResult.isFailure()) {
            return Result.failure(orderAssignResult.getError());
        }

        UnitResult<Error> takeOrderResult =  fastestCourier.takeOrder(order);
        if (takeOrderResult.isFailure()) {
            return Result.failure(takeOrderResult.getError());
        }

        return Result.success(fastestCourier);
    }

    public static class Errors {
        public static Error nobodyCanTakeOrder() {
            return Error.of("nobody.can.take.order",
                            "Никто не может взять заказ");
        }
    }
    
}
