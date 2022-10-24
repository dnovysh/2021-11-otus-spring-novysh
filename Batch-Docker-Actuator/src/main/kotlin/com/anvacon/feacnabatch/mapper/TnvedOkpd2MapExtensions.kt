package com.anvacon.feacnabatch.mapper

import com.anvacon.feacnabatch.dto.TnvedOkpd2ProcessorStateDto
import com.anvacon.feacnabatch.entity.TnvedOkpd2

fun TnvedOkpd2.toTnvedOkpd2ProcessorStateDto() = TnvedOkpd2ProcessorStateDto(
    tnvedCodeSix = tnvedCodeSix,
    okpdKind = okpdKind,
    tnvedName = tnvedName,
    okpdName = okpdName
)
