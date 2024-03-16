package dev.roy.springbatchbillingjob.config.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BillingJobConfiguration {

  @Bean
  public Job billingJob(
      JobRepository jobRepository, Step filePreparationStep, Step fileIngestionStep) {

    return new JobBuilder("BillingJob", jobRepository)
        .start(filePreparationStep)
        .next(fileIngestionStep)
        .build();
  }
}
