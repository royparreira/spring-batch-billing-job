package dev.roy.springbatchbillingjob.jobs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

import dev.roy.springbatchbillingjob.service.BillingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@SpringBatchTest
@ExtendWith(SpringExtension.class)
class BillingJobTest {

  @Autowired private JobLauncherTestUtils jobLauncherTestUtils;

  @MockBean BillingService billingService;

  @Test
  void execute_shouldCompleteJobWithoutErrors() throws Exception {

    // given
    JobParameters jobParameters =
        jobLauncherTestUtils
            .getUniqueJobParametersBuilder()
            .addString("input.file", "mock/path")
            .toJobParameters();

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
    // then
    assertAll(
        () -> {
          assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
          assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        });
  }

  @Test
  void execute_shouldFailIfThrowException() throws Exception {
    final String failMessage = "Unable to process billing information";

    // given
    ExitStatus failExitStatus = ExitStatus.FAILED.addExitDescription(failMessage);
    doThrow(new RuntimeException(failMessage)).when(billingService).processBill();

    // when
    JobExecution jobExecution = jobLauncherTestUtils.launchJob();

    // then
    assertAll(
        () -> {
          assertEquals(failExitStatus, jobExecution.getExitStatus());
          assertEquals(BatchStatus.FAILED, jobExecution.getStatus());
        });
  }
}
