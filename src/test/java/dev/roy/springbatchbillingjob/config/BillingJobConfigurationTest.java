package dev.roy.springbatchbillingjob.config;

import static org.assertj.core.api.Assertions.assertThat;

import dev.roy.springbatchbillingjob.BaseTestConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.step.tasklet.TaskletStep;

class BillingJobConfigurationTest extends BaseTestConfiguration {

  @Test
  void contextShouldContainFilePreparationStepBean() {
    assertThat(applicationContext.containsBean("filePreparationStep"))
        .as("filePreparationStep should be loaded in the context")
        .isTrue();
  }

  @Test
  void filePreparationStepShouldBeOfTypeTaskletStep() {
    assertThat(applicationContext.getBean("filePreparationStep"))
        .as("filePreparationStep should be instance of TaskletStep")
        .isInstanceOf(TaskletStep.class);
  }

  @Test
  void contextShouldContainBillingJobBean() {
    assertThat(applicationContext.containsBean("billingJob"))
        .as("billingJob should be loaded in the context")
        .isTrue();
  }
}
