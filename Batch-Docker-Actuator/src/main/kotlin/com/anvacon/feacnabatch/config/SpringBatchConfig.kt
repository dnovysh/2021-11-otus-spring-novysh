package com.anvacon.feacnabatch.config

import com.anvacon.feacnabatch.entity.TnvedOkpd2
import com.anvacon.feacnabatch.model.TnvedOkpd2Source
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor


@Configuration
@EnableBatchProcessing
class SpringBatchConfig {

    @Bean
    fun jobLauncherAsync(jobRepository: JobRepository): JobLauncher? {
        val jobLauncher = SimpleJobLauncher()
        jobLauncher.setJobRepository(jobRepository)
        jobLauncher.setTaskExecutor(SimpleAsyncTaskExecutor())
        jobLauncher.afterPropertiesSet()
        return jobLauncher
    }

    @Bean
    fun tnvedOkpd2LoadJob(
        jobBuilderFactory: JobBuilderFactory,
        stepBuilderFactory: StepBuilderFactory,
        itemReader: ItemReader<TnvedOkpd2Source>,
        itemProcessor: ItemProcessor<TnvedOkpd2Source, TnvedOkpd2>,
        itemWriter: ItemWriter<TnvedOkpd2>
    ): Job {

        val tnvedOkpd2FileLoadStep: Step = stepBuilderFactory.get("TNVED-OKPD2-File-load")
            .chunk<TnvedOkpd2Source, TnvedOkpd2>(500)
            .reader(itemReader)
            .processor(itemProcessor)
            .writer(itemWriter)
            .build();

        return jobBuilderFactory.get("TNVED-OKPD2-Load")
            .incrementer(RunIdIncrementer())
            .start(tnvedOkpd2FileLoadStep)
            .build()
    }
}
