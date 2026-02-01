package ru.ddd.delivery.core.application.commands;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ddd.delivery.core.domain.model.Volume;
import ru.ddd.delivery.core.ports.CourierRepository;
import ru.ddd.libs.errs.Error;
import ru.ddd.libs.errs.GeneralErrors;
import ru.ddd.libs.errs.UnitResult;

@Service
public class AddStoragePlaceCommandHandlerImpl implements AddStoragePlaceCommandHandler {
    private final CourierRepository courierRepository;

    public AddStoragePlaceCommandHandlerImpl(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }

    @Transactional
    @Override
    public UnitResult<Error> handle(AddStoragePlaceCommand command) {
        // Если нет такого курьера, возвращаем ошибку
        var courierOpt = courierRepository.findById(command.getCourierId());
        if (courierOpt.isEmpty()) {
            return UnitResult.failure(GeneralErrors.notFound("courier", command.getCourierId()));
        }
        
        var createVolumeResult = Volume.create(command.getTotalVolume());
        if (createVolumeResult.isFailure()) {
            return UnitResult.failure(createVolumeResult.getError());
        }

        var courier = courierOpt.get();
        var addStoragePlaceResult = courier.addStoragePlace(command.getName(), createVolumeResult.getValue());
        if (addStoragePlaceResult.isFailure()) {
            return UnitResult.failure(addStoragePlaceResult.getError());
        }
        courierRepository.save(courier);
        return UnitResult.success();
        
    }

}