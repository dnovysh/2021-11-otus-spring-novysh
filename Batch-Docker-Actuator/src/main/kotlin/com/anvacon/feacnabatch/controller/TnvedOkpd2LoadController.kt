package com.anvacon.feacnabatch.controller

import com.anvacon.feacnabatch.model.JobExecutionResponseModel
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.OffsetDateTime
import java.util.*

@RestController
@RequestMapping("/api/TnvedOkpd2")
class TnvedOkpd2Controller(
    @Qualifier("jobLauncherAsync") private val jobLauncher: JobLauncher,
    @Qualifier("tnvedOkpd2LoadJob") private val job: Job
) {

    @PostMapping("/load")
    fun load(): JobExecutionResponseModel {
        val loadDate = OffsetDateTime.now().toString();
        val jobUuid = UUID.randomUUID().toString();
        val jobExecution: JobExecution = jobLauncher
            .run(
                job, JobParameters(
                    mapOf<String, JobParameter>(
                        "loadDate" to JobParameter(loadDate),
                        "jobUuid" to JobParameter(jobUuid)
                    )
                )
            );
        return JobExecutionResponseModel(jobExecution.id, jobUuid);
    }
}
