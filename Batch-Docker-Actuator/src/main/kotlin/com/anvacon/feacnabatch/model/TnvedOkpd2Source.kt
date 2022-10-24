package com.anvacon.feacnabatch.model

import java.io.Serializable

class TnvedOkpd2Source : Serializable {
    var tnvedCodeSix: String? = null
    var tnvedName: String? = null
    var okpdKind: String? = null
    var okpdName: String? = null

    constructor(tnvedCodeSix: String?, tnvedName: String?, okpdKind: String?, okpdName: String?) {
        this.tnvedCodeSix = tnvedCodeSix
        this.tnvedName = tnvedName
        this.okpdKind = okpdKind
        this.okpdName = okpdName
    }

    constructor() {}
}
