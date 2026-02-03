package ru.ddd.delivery.core.application.queries;

import java.util.List;

import ru.ddd.delivery.core.application.queries.dto.OrderDto;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

public interface GetAllIncompleteOrdersQueryHandler {
    Result<List<OrderDto>, Error> handle();
}
