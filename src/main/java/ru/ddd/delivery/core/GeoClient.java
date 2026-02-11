package ru.ddd.delivery.core;

import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

public interface GeoClient {
    public Result<Location, Error> getLocation(String address);
}
