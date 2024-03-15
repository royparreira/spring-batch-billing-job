package dev.roy.springbatchbillingjob.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import dev.roy.springbatchbillingjob.BaseTestConfiguration;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;

class FilePreparationTaskletTest extends BaseTestConfiguration {

  public static final String STEP_NAME = "filePreparation";

  @Test
  void execute_shouldCopyInputFileToStagingDirectory() {
    // given
    JobParameters jobParameters =
        jobLauncherTestUtils
            .getUniqueJobParametersBuilder()
            .addString("input.file", "src/main/resources/billing-2023-01.csv")
            .toJobParameters();

    // when
    AtomicReference<JobExecution> jobExecution = new AtomicReference<>();

    // then
    assertAll(
        () -> {
          assertThatCode(() -> jobExecution.set(jobLauncherTestUtils.launchJob(jobParameters)))
              .doesNotThrowAnyException();
          assertThat(jobExecution.get().getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
          assertThat(Paths.get("staging", "billing-2023-01.csv")).exists();
        });
  }

  @Test
  void execute_failIfJobParameterInputFileIsNull() throws Exception {
    // given
    JobParameters jobParameters = new JobParameters();

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    StepExecution stepExecution =
        jobExecution.getStepExecutions().stream()
            .filter(step -> step.getStepName().equalsIgnoreCase(STEP_NAME))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Step not found: " + STEP_NAME));

    String exitStatusCode = stepExecution.getExitStatus().getExitCode();
    String exitDescription = stepExecution.getExitStatus().getExitDescription();

    // then
    assertAll(
        () -> {
          assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.FAILED);
          assertThat(exitStatusCode).isEqualTo(ExitStatus.FAILED.getExitCode());
          assertThat(exitDescription).contains("'input.file' job parameter is null!");
        });
  }
}
