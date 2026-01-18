package ru.ddd.delivery.core.domain.services;

import java.util.List;

import ru.ddd.delivery.core.domain.model.courier.Courier;
import ru.ddd.delivery.core.domain.model.order.Order;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

public interface OrderDispatcher {

    public Result<Courier, Error> dispatch(Order order, List<Courier> couriers);

}
