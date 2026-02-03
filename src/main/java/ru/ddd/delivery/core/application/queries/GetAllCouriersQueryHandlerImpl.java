package ru.ddd.delivery.core.application.queries;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ddd.delivery.core.application.queries.dto.CourierDto;
import ru.ddd.delivery.core.application.queries.dto.LocationDto;
import ru.ddd.delivery.core.domain.model.courier.Courier;
import ru.ddd.delivery.core.ports.CourierRepository;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

@Service
public class GetAllCouriersQueryHandlerImpl implements GetAllCouriersQueryHandler {
    private final CourierRepository courierRepository;

    public GetAllCouriersQueryHandlerImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Transactional
    @Override
    public Result<List<CourierDto>, Error> handle() {
        var couriers = courierRepository.findAll();

        var response = mapToDto(couriers);

        return Result.success(response);

    }

    private List<CourierDto> mapToDto(List<Courier> couriers) {
        List<CourierDto> courierDtos = couriers.stream()
                .map(courier -> new CourierDto(
                        courier.getId(),
                        courier.getName(),
                        new LocationDto(courier.getLocation().getX(), courier.getLocation().getY())))
                .toList();

        return courierDtos;
    }

}