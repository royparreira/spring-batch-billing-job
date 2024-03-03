package dev.roy.springbatchbillingjob.jobs;

import dev.roy.springbatchbillingjob.service.BillingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.lang.NonNull;

@Slf4j
@AllArgsConstructor
public class BillingJob implements Job {
  private JobRepository jobRepository;
  private BillingService billingService;

  @NonNull
  @Override
  public String getName() {
    return "BillingJob";
  }

  @Override
  public void execute(@NonNull JobExecution execution) {

    try {

      billingService.processBill();
      execution.setStatus(BatchStatus.COMPLETED);
      execution.setExitStatus(ExitStatus.COMPLETED);

    } catch (Exception exception) {
      log.error(exception.getLocalizedMessage());

      execution.addFailureException(exception);
      execution.setStatus(BatchStatus.FAILED);
      execution.setExitStatus(
          ExitStatus.FAILED.addExitDescription(exception.getLocalizedMessage()));

    } finally {
      this.jobRepository.update(execution);
    }
  }
}
