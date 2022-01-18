package ru.otus.core.dto;

import java.math.BigDecimal;

public record ReviewUpdateDto(Integer id, String title, String text, BigDecimal rating) {
}
