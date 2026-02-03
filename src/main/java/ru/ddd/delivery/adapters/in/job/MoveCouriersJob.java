package ru.ddd.delivery.adapters.in.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import ru.ddd.delivery.core.application.commands.MoveCommandHandler;

@Component
public class MoveCouriersJob implements Job {
    private final MoveCommandHandler handler;

    public MoveCouriersJob(MoveCommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute(JobExecutionContext context) {
        handler.handle();
    }
}
