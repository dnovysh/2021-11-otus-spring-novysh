package com.anvacon.feacnabatch.batch

import com.anvacon.feacnabatch.model.TnvedOkpd2Source
import org.springframework.batch.extensions.excel.mapping.BeanWrapperRowMapper
import org.springframework.batch.extensions.excel.streaming.StreamingXlsxItemReader
import org.springframework.batch.extensions.excel.support.rowset.DefaultRowSetFactory
import org.springframework.batch.extensions.excel.support.rowset.StaticColumnNameExtractor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.core.io.ClassPathResource


@Configuration
class TnvedOkpd2ReaderConfig(
    @Value("\${app-batch.data.tnved-okpd2:/data/tnved_okpd2.xlsx}") private val resourcePath: String
) {

    @Bean
    @Scope(value = "job", proxyMode = ScopedProxyMode.TARGET_CLASS)
    fun tnvedOkpd2XlsxReader(): StreamingXlsxItemReader<TnvedOkpd2Source> {
        val factory = DefaultRowSetFactory()
        factory.setColumnNameExtractor(
            StaticColumnNameExtractor(
                arrayOf(
                    "tnvedCodeSix",
                    "tnvedName",
                    "okpdKind",
                    "okpdName"
                )
            )
        )

        val reader = StreamingXlsxItemReader<TnvedOkpd2Source>()
        reader.setName("TNVED-OKPD2-Xlsx-Reader")
        reader.setResource(ClassPathResource(resourcePath));
        reader.setLinesToSkip(1);
        reader.setRowSetFactory(factory);
        reader.setRowMapper(excelTnvedOkpd2RowMapper());
        return reader;
    }

    private fun excelTnvedOkpd2RowMapper(): BeanWrapperRowMapper<TnvedOkpd2Source> {
        val rowMapper = BeanWrapperRowMapper<TnvedOkpd2Source>();
        rowMapper.setTargetType(TnvedOkpd2Source::class.java);
        rowMapper.setStrict(false);
        return rowMapper;
    }
}
