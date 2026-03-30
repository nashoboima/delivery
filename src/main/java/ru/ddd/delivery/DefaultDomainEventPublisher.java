package ru.ddd.delivery;

import org.springframework.context.ApplicationEventPublisher;

import lombok.RequiredArgsConstructor;
import ru.ddd.libs.ddd.Aggregate;
import ru.ddd.libs.ddd.DomainEvent;
import ru.ddd.libs.ddd.DomainEventPublisher;

@RequiredArgsConstructor
public class DefaultDomainEventPublisher implements DomainEventPublisher {
    private final ApplicationEventPublisher publisher;

    @Override
    public void publish(Iterable<? extends Aggregate<?>> aggregates) {
        for (Aggregate<?> aggregate : aggregates) {
            for (DomainEvent event : aggregate.getDomainEvents()) {
                publisher.publishEvent(event);
            }
        }
    }

}
