package ru.ddd.delivery.core.ports;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ru.ddd.delivery.core.domain.model.courier.Courier;

public interface CourierRepository {

    public void save(Courier courier);

    public Optional<Courier> findById(UUID courierId);

    public List<Courier> findAllAssignableCouriers();

    public void clean();

    public List<Courier> findAll();

}
