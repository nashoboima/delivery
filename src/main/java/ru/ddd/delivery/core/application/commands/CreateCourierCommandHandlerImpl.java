package ru.ddd.delivery.core.application.commands;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ddd.delivery.core.domain.model.Location;
import ru.ddd.delivery.core.domain.model.Speed;
import ru.ddd.delivery.core.domain.model.courier.Courier;
import ru.ddd.delivery.core.ports.CourierRepository;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.Result;

@Service
public class CreateCourierCommandHandlerImpl implements CreateCourierCommandHandler {
    private final CourierRepository courierRepository;

    public CreateCourierCommandHandlerImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Transactional
    @Override
    public Result<UUID, Error> handle(CreateCourierCommand command) {
        int randomX = ThreadLocalRandom.current().nextInt(1, 11);
        int randomY = ThreadLocalRandom.current().nextInt(1, 11);
        var createLocationResult = Location.create(randomX, randomY);
        if (createLocationResult.isFailure()) {
            return Result.failure(createLocationResult.getError());
        }
        var createSpeedResult = Speed.create(command.getSpeed());
        if (createSpeedResult.isFailure()) {
            return Result.failure(createSpeedResult.getError());
        }
        var courierCreateResult = Courier.create(command.getName(), createSpeedResult.getValue(), createLocationResult.getValue());
        if (courierCreateResult.isFailure())
            return Result.failure(courierCreateResult.getError());
        var courier = courierCreateResult.getValue();

        courierRepository.save(courier);

        return Result.success(courier.getId());
    }
}