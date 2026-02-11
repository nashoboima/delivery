package ru.ddd.delivery.adapters.in.job;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail assignOrdersJobDetail() {
        return JobBuilder.newJob(AssignOrdersJob.class)
                .withIdentity("assignOrdersJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger assignOrdersTrigger(@Qualifier("assignOrdersJobDetail")JobDetail assignOrdersJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(assignOrdersJobDetail)
                .withIdentity("assignOrdersTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(1) // каждые 1 сек
                        .repeatForever())
                .build();
    }

    @Bean
    public JobDetail moveCouriersJobDetail() {
        return JobBuilder.newJob(MoveCouriersJob.class)
                .withIdentity("moveCouriersJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger moveCouriersTrigger(@Qualifier("moveCouriersJobDetail")JobDetail moveCouriersJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(moveCouriersJobDetail)
                .withIdentity("moveCouriersTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(2) // каждые 2 сек
                        .repeatForever())
                .build();
    }
}
