package ru.ddd.delivery.core.application.queries;

import java.util.List;

import ru.ddd.delivery.core.application.queries.dto.CourierDto;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

public interface GetAllCouriersQueryHandler {
    Result<List<CourierDto>, Error> handle();
}
