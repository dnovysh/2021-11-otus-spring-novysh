package com.anvacon.feacnabatch.batch

import com.anvacon.feacnabatch.entity.TnvedOkpd2
import com.anvacon.feacnabatch.model.TnvedOkpd2Source
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

internal class TnvedOkpd2ProcessorTest {

    private val loadDate = OffsetDateTime.now().toString();
    private val processor = TnvedOkpd2Processor(loadDate);
    private val testSeed = listOf<TnvedOkpd2Source>(
        TnvedOkpd2Source("010203", "tnved 1", "07.08.11", "okpd A"),
        TnvedOkpd2Source(null, null, "07.08.12", "okpd B"),
        TnvedOkpd2Source(null, null, "07.08.13", "okpd C"),
        TnvedOkpd2Source("", "", "27.28.13", "okpd D"),
        TnvedOkpd2Source("011213", "tnved 2", "07.18.15", "okpd E"),
        TnvedOkpd2Source("031313", "tnved 3", "", ""),
        TnvedOkpd2Source("051515", "tnved 5", null, null),
        TnvedOkpd2Source("123456", "tnved 123456", "17.18.21", "okpd F"),
        TnvedOkpd2Source("654321", "tnved 654321", "21.18.17", "okpd G")
    );

    @Test
    fun process() {
        val loadDate = OffsetDateTime.parse(loadDate);
        val expected = listOf<TnvedOkpd2>(
            TnvedOkpd2(10203070811L, "010203", "07.08.11", "tnved 1", "okpd A", loadDate),
            TnvedOkpd2(10203070812L, "010203", "07.08.12", "tnved 1", "okpd B", loadDate),
            TnvedOkpd2(10203070813L, "010203", "07.08.13", "tnved 1", "okpd C", loadDate),
            TnvedOkpd2(10203272813L, "010203", "27.28.13", "tnved 1", "okpd D", loadDate),
            TnvedOkpd2(11213071815L, "011213", "07.18.15", "tnved 2", "okpd E", loadDate),
            TnvedOkpd2(31313071815L, "031313", "07.18.15", "tnved 3", "okpd E", loadDate),
            TnvedOkpd2(51515071815L, "051515", "07.18.15", "tnved 5", "okpd E", loadDate),
            TnvedOkpd2(123456171821L, "123456", "17.18.21", "tnved 123456", "okpd F", loadDate),
            TnvedOkpd2(654321211817L, "654321", "21.18.17", "tnved 654321", "okpd G", loadDate),
        )
        val actual = testSeed.map(processor::process);
        Assertions.assertThat(expected)
            .usingRecursiveComparison()
            .isEqualTo(actual);
    }
}