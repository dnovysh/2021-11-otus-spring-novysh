package com.anvacon.feacnabatch.service

import com.anvacon.feacnabatch.entity.TnvedOkpd2

interface TnvedOkpd2Service {

    fun batchWrite(items: MutableList<out TnvedOkpd2>): MutableList<TnvedOkpd2>;
}
