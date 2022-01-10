package ru.otus.loader;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@ConstructorBinding
@ConfigurationProperties(prefix = "exam.header-loader")
public class ResourceFileAsStringLoaderImpl implements ResourceFileAsStringLoader {

    private final String fileName;

    public ResourceFileAsStringLoaderImpl(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String load() {
        try (var bis = new BufferedInputStream(
                Objects.requireNonNull(getClass().getResourceAsStream(fileName)))) {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();

            for (int result = bis.read(); result != -1; result = bis.read()) {
                buf.write((byte) result);
            }

            return buf.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
