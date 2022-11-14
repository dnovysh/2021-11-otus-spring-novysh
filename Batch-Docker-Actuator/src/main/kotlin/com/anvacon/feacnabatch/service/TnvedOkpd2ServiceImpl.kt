package com.anvacon.feacnabatch.service

import com.anvacon.feacnabatch.entity.TnvedOkpd2
import com.anvacon.feacnabatch.repository.TnvedOkpd2Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TnvedOkpd2ServiceImpl(
    private val tnvedOkpd2Repository: TnvedOkpd2Repository
) : TnvedOkpd2Service {

    @Transactional
    override fun batchWrite(items: MutableList<out TnvedOkpd2>): MutableList<TnvedOkpd2> {
        tnvedOkpd2Repository.findAllById(items.map { it.id });
        return tnvedOkpd2Repository.saveAllAndFlush(items);
    }
}
