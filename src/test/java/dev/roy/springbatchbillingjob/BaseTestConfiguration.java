package dev.roy.springbatchbillingjob;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@SpringBatchTest
@TestPropertySource(
    properties = {
      "spring.batch.job.enabled=false",
      "spring.batch.job.names=BillingJob",
    })
// These properties above disable BillingJob automatic execution letting job execution being
// configured as per need inside each test
public abstract class BaseTestConfiguration {
  @Autowired protected ApplicationContext applicationContext;
  @Autowired protected JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired protected JdbcTemplate jdbcTemplate;
}
