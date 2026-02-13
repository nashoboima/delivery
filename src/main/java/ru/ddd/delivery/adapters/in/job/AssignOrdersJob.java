package ru.ddd.delivery.adapters.in.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import ru.ddd.delivery.core.application.commands.AssignOrderCommandHandler;

@Component
public class AssignOrdersJob implements Job {
    private final AssignOrderCommandHandler handler;

    public AssignOrdersJob(AssignOrderCommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public void execute(JobExecutionContext context) {
        handler.handle();
    }
}

