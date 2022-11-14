package com.anvacon.feacnabatch.batch

import com.anvacon.feacnabatch.entity.TnvedOkpd2
import com.anvacon.feacnabatch.service.TnvedOkpd2Service
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class TnvedOkpd2Writer(
    private val tnvedOkpd2Service: TnvedOkpd2Service
) : ItemWriter<TnvedOkpd2> {
    override fun write(items: MutableList<out TnvedOkpd2>) {
        tnvedOkpd2Service.batchWrite(items);
    }
}
