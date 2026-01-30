package ru.ddd.delivery.adapters.out.postgres;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.ddd.delivery.core.domain.model.courier.Courier;

public interface CourierJpaRepository extends JpaRepository<Courier, UUID> {

    @Query("""
        SELECT c 
        FROM Courier c 
        WHERE c NOT IN (
            SELECT DISTINCT c2 
            FROM Courier c2 
            JOIN c2.storagePlaces sp 
            WHERE sp.orderId IS NOT NULL
        )
        """)
    List<Courier> findAllCouriersWithEmptyStoragePlaces();
    
}
