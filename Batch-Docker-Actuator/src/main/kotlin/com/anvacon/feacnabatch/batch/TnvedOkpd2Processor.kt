package com.anvacon.feacnabatch.batch

import com.anvacon.feacnabatch.dto.TnvedOkpd2ProcessorStateDto
import com.anvacon.feacnabatch.entity.TnvedOkpd2
import com.anvacon.feacnabatch.mapper.toTnvedOkpd2ProcessorStateDto
import com.anvacon.feacnabatch.model.TnvedOkpd2Source
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.OffsetDateTime

@Component
@StepScope
class TnvedOkpd2Processor(
    @Value("#{jobParameters['loadDate']}") val loadDateParameter: String,
) : ItemProcessor<TnvedOkpd2Source, TnvedOkpd2> {

    private var last: TnvedOkpd2ProcessorStateDto? = null;

    override fun process(item: TnvedOkpd2Source): TnvedOkpd2? {
        val loadDate = OffsetDateTime.parse(loadDateParameter);
        val tnvedCodeSix = (
                if (item.tnvedCodeSix.isNullOrBlank()) last!!.tnvedCodeSix
                else item.tnvedCodeSix
                )!!.trim();
        val okpdKind = (
                if (item.okpdKind.isNullOrBlank()) last!!.okpdKind
                else item.okpdKind
                )!!.trim();
        val id = tnvedCodeSix.toLong() * 1000000 + okpdKind.replace(".", "").toLong();
        val tnvedOkpd2 = TnvedOkpd2(
            id = id,
            tnvedCodeSix = tnvedCodeSix,
            tnvedName = if (item.tnvedCodeSix.isNullOrBlank()) last?.tnvedName else item.tnvedName,
            okpdKind = okpdKind,
            okpdName = if (item.okpdKind.isNullOrBlank()) last?.okpdName else item.okpdName,
            loadDate = loadDate
        );
        last = tnvedOkpd2.toTnvedOkpd2ProcessorStateDto();
        return tnvedOkpd2;
    }
}
