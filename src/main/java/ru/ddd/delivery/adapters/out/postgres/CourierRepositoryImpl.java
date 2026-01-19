package ru.ddd.delivery.adapters.out.postgres;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import ru.ddd.delivery.core.domain.model.courier.Courier;
import ru.ddd.delivery.core.ports.CourierRepository;

@Repository
public class CourierRepositoryImpl implements CourierRepository {

    private final CourierJpaRepository jpa;

    public CourierRepositoryImpl(CourierJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(Courier courier) {
        jpa.save(courier);
    }

    @Override
    public Optional<Courier> findById(UUID courierId) {
        return jpa.findById(courierId);
    }

    @Override
    public List<Courier> findAllAssignableCouriers() {
        return jpa.findAllCouriersWithEmptyStoragePlaces();
    }

    @Override
    public void clean() {
        jpa.deleteAll();
    }

}
