package ru.ddd.delivery.adapters.in.http.openapi.mappers;

import org.mapstruct.Mapper;

import ru.ddd.delivery.core.application.queries.dto.OrderDto;

@Mapper(componentModel = "spring", uses = { LocationMapper.class })
public interface OrderMapper {
    public ru.ddd.delivery.adapters.in.http.openapi.model.Order toHttp(OrderDto dto);
}
