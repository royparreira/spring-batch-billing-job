package dev.roy.springbatchbillingjob.config;

import static org.assertj.core.api.Assertions.assertThat;

import dev.roy.springbatchbillingjob.BaseTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.batch.core.step.tasklet.TaskletStep;

class BillingJobConfigurationTest extends BaseTestConfiguration {

  @ParameterizedTest
  @ValueSource(strings = {"filePreparationStep", "fileIngestionStep"})
  void contextShouldContainFilePreparationStepBean(String stepName) {
    assertThat(applicationContext.containsBean(stepName))
        .as(stepName + " should be loaded in the context")
        .isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"filePreparationStep", "fileIngestionStep"})
  void filePreparationStepShouldBeOfTypeTaskletStep(String stepName) {
    assertThat(applicationContext.getBean(stepName))
        .as(stepName + " should be instance of TaskletStep")
        .isInstanceOf(TaskletStep.class);
  }

  @Test
  void contextShouldContainBillingJobBean() {
    assertThat(applicationContext.containsBean("billingJob"))
        .as("billingJob should be loaded in the context")
        .isTrue();
  }
}
