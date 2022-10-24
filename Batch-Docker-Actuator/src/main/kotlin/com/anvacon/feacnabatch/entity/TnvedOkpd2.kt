package com.anvacon.feacnabatch.entity

import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(schema = "public", name = "tnved_okpd2")
class TnvedOkpd2(
    @Id
    var id: Long,

    @Column(name = "tnved_code_six")
    @NotNull
    var tnvedCodeSix: String,

    @Column(name = "okpd_kind")
    @NotNull
    var okpdKind: String,

    @Column(name = "tnved_name")
    var tnvedName: String?,

    @Column(name = "okpd_name")
    var okpdName: String?,

    @Column(name = "load_date")
    @NotNull
    var loadDate: OffsetDateTime
)
