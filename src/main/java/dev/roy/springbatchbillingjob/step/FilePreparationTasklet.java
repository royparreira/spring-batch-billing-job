package dev.roy.springbatchbillingjob.step;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.lang.NonNull;

public class FilePreparationTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(
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
