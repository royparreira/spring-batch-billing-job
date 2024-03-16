package dev.roy.springbatchbillingjob.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import dev.roy.springbatchbillingjob.BaseTestConfiguration;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.test.jdbc.JdbcTestUtils;

class BillingJobTest extends BaseTestConfiguration {

  @Test
  void execute_failIfJobParameterInputFileIsNull() throws Exception {
    // given
    JobParameters jobParameters = new JobParameters();

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    StepExecution stepExecution =
        jobExecution.getStepExecutions().stream()
            .filter(step -> step.getStepName().equalsIgnoreCase("filePreparation"))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Step not found: filePreparation"));

    String exitStatusCode = stepExecution.getExitStatus().getExitCode();
    String exitDescription = stepExecution.getExitStatus().getExitDescription();

    Optional<StepExecution> optionalFileIngestionExecution =
        jobExecution.getStepExecutions().stream()
            .filter(step -> step.getStepName().equalsIgnoreCase("fileIngestion"))
            .findFirst();

    // then
    assertAll(
        () -> {
          assertThat(stepExecution.getStatus()).isEqualTo(BatchStatus.FAILED);
          assertThat(exitStatusCode).isEqualTo(ExitStatus.FAILED.getExitCode());
          assertThat(exitDescription).contains("'input.file' job parameter is null!");
          assertThat(optionalFileIngestionExecution).isEmpty();
        });
  }

  @Test
  void testBillingJob() {
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
              .as("Job should run without throw any exception")
              .doesNotThrowAnyException();

          assertThat(jobExecution.get().getExitStatus())
              .as("Job should be COMPLETED without any errors")
              .isEqualTo(ExitStatus.COMPLETED);

          assertThat(Paths.get("staging", "billing-2023-01.csv"))
              .as("filePreparation step should copy csv from resource to staging directory")
              .exists();

          assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "BILLING_DATA"))
              .as("fileIngestion step should copy from csv file into BILLING_DATA table")
              .isEqualTo(1_000);
        });
  }
}
