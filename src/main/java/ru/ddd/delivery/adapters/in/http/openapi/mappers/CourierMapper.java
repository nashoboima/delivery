package ru.ddd.delivery.adapters.in.http.openapi.mappers;

import org.mapstruct.Mapper;

import ru.ddd.delivery.core.application.queries.dto.CourierDto;

@Mapper(componentModel = "spring", uses = { LocationMapper.class })
public interface CourierMapper {
    public ru.ddd.delivery.adapters.in.http.openapi.model.Courier toHttp(CourierDto dto);
}
