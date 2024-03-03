package dev.roy.springbatchbillingjob;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.lang.NonNull;

@Slf4j
@AllArgsConstructor
public class BillingJob implements Job {
  private JobRepository jobRepository;

  @NonNull
  @Override
  public String getName() {
    return "BillingJob";
  }

  @Override
  public void execute(@NonNull JobExecution execution) {
    log.info("Processing Billing Information");

    execution.setStatus(BatchStatus.COMPLETED);
    execution.setExitStatus(ExitStatus.COMPLETED);

    this.jobRepository.update(execution);
  }
}
