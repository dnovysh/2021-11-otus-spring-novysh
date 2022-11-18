package ru.otus.productflux.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Price {

    @Field(targetType = DECIMAL128)
    private BigDecimal value;

    private String currency;
}
