package ru.ddd.delivery.adapters.in.http.openapi.mappers;

import org.mapstruct.Mapper;

import ru.ddd.delivery.core.application.queries.dto.LocationDto;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    public ru.ddd.delivery.adapters.in.http.openapi.model.Location toHttp(LocationDto dto);
}
