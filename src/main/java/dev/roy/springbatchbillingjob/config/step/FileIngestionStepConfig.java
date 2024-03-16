package dev.roy.springbatchbillingjob.config.step;

import dev.roy.springbatchbillingjob.model.BillingData;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.support.JdbcTransactionManager;

import javax.sql.DataSource;

@Configuration
public class FileIngestionStepConfig {

  @Bean
  public FlatFileItemReader<BillingData> billingDataFileReader() {
    return new FlatFileItemReaderBuilder<BillingData>()
        .name("billingDataFileReader")
        .resource(new FileSystemResource("staging/billing-2023-01.csv"))
        .delimited()
        .names(
            "dataYear",
            "dataMonth",
            "accountId",
            "phoneNumber",
            "dataUsage",
            "callDuration",
            "smsCount")
        .targetType(BillingData.class)
        .build();
  }

  @Bean
  public JdbcBatchItemWriter<BillingData> billingDataTableWriter(DataSource dataSource) {
    String sql =
        "insert into BILLING_DATA values (:dataYear, :dataMonth, :accountId, :phoneNumber, :dataUsage, :callDuration, :smsCount)";

    return new JdbcBatchItemWriterBuilder<BillingData>()
        .dataSource(dataSource)
        .sql(sql)
        .beanMapped()
        .build();
  }

  @Bean
  public Step fileIngestionStep(
      JobRepository jobRepository,
      JdbcTransactionManager transactionManager,
      ItemReader<BillingData> billingDataFileReader,
      ItemWriter<BillingData> billingDataTableWriter) {

    final int fileIngestionChunkSize = 100;

    return new StepBuilder("fileIngestion", jobRepository)
        .<BillingData, BillingData>chunk(fileIngestionChunkSize, transactionManager)
        .reader(billingDataFileReader)
        .writer(billingDataTableWriter)
        .build();
  }
}
