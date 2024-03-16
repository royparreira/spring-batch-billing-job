package dev.roy.springbatchbillingjob.config.step;

import dev.roy.springbatchbillingjob.step.FilePreparationTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;

@Configuration
public class FilePreparationStepConfig {

  @Bean
  public Step filePreparationStep(
      JobRepository jobRepository, JdbcTransactionManager transactionManager) {

    return new StepBuilder("filePreparation", jobRepository)
        .tasklet(new FilePreparationTasklet(), transactionManager)
        .build();
  }
}
