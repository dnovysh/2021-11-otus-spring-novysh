package ru.otus.loader;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

@ConstructorBinding
@ConfigurationProperties(prefix = "exam.header-loader")
public class ResourceFileAsStringLoaderImpl implements ResourceFileAsStringLoader {

    private final String fileName;

    public ResourceFileAsStringLoaderImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String load() {
        try (var br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(fileName)), StandardCharsets.UTF_8))) {

            return br.lines().collect(Collectors.joining(System.lineSeparator()));

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
