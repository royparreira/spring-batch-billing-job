package dev.roy.springbatchbillingjob.config;

import dev.roy.springbatchbillingjob.jobs.BillingJob;
import dev.roy.springbatchbillingjob.service.BillingService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BillingJobConfiguration {
  @Bean
  public Job job(JobRepository jobRepository, BillingService service) {
    return new BillingJob(jobRepository, service);
  }
}
