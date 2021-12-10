package ru.otus.annotation;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.support.AnnotationConsumer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayArgumentsProvider implements ArgumentsProvider, AnnotationConsumer<ArraySources> {
    private List<int[]> arguments;

    @Override
    public void accept(ArraySources source) {
        List<ArraySource> arrays = Arrays.asList(source.arrays());

        this.arguments = arrays.stream().map(ArraySource::array).collect(Collectors.toList());
    }

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return this.arguments.stream().map(Arguments::of);
    }
}
