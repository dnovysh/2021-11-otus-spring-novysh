package com.anvacon.feacnabatch.actuator

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.actuate.health.Status
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class TnvedOkpd2FileExistsHealthIndicator(
    @Value("\${app-batch.data.tnved-okpd2:/data/tnved_okpd2.xlsx}") private val resourcePath: String
) : HealthIndicator {

    override fun health(): Health {
        return if (ClassPathResource(resourcePath).exists()) Health.up()
            .withDetail("message", "tnved_okpd2.xlsx file exists")
            .build();
        else Health.down().status(Status.DOWN)
            .withDetail("message", "File tnved_okpd2.xlsx does not exist")
            .build();
    }
}
