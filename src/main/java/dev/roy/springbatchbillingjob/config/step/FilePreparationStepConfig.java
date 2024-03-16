package dev.roy.springbatchbillingjob.config.step;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.lang.NonNull;

@Configuration
public class FilePreparationStepConfig {

  @Bean
  public Step filePreparationStep(
      JobRepository jobRepository, JdbcTransactionManager transactionManager) {

    return new StepBuilder("filePreparation", jobRepository)
        .tasklet(this::prepareFile, transactionManager)
        .build();
  }

  private RepeatStatus prepareFile(
      @NonNull StepContribution contribution, @NonNull ChunkContext chunkContext) throws Exception {
    JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
    Path sourceFilePath = getInputFilePath(jobParameters);
    Path targetFilePath = getStagingFilePath(sourceFilePath);

    Files.copy(sourceFilePath, targetFilePath, REPLACE_EXISTING);

    return RepeatStatus.FINISHED;
  }

  private Path getInputFilePath(JobParameters jobParameters) throws IllegalArgumentException {

    String inputFilePath =
        Optional.ofNullable(jobParameters.getString("input.file"))
            .orElseThrow(() -> new IllegalArgumentException("'input.file' job parameter is null!"));

    return Paths.get(inputFilePath);
  }

  private Path getStagingFilePath(Path source) {
    return Paths.get("staging", source.toFile().getName());
  }
}
