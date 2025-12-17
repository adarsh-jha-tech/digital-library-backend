package com.jhaadarsh.digital_library.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProjectCodeExporter {

    private static final String JAVA_ROOT =
            "src/main/java/com/jhaadarsh/digital_library";

    private static final String RESOURCES_ROOT =
            "src/main/resources";

    private static final String POM_FILE = "pom.xml";

    private static final String OUTPUT_FILE = "digital-library-codebase.md";

    // Layer name -> folder
    private static final Map<String, String> LAYERS = new LinkedHashMap<>();

    static {
        LAYERS.put("Application Entry Point", "");
        LAYERS.put("Controller Layer", "controller");
        LAYERS.put("Service Layer", "service");
        LAYERS.put("Adapter Layer", "adapter");
        LAYERS.put("Repository Layer", "repository");
        LAYERS.put("Domain Model", "model");
        LAYERS.put("Entities (Input / Output)", "entity");
        LAYERS.put("Mappers", "mappers");
        LAYERS.put("Exception Handling", "exception");
        LAYERS.put("Cross-Cutting Concerns", "beans");
    }

    public static void main(String[] args) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(OUTPUT_FILE),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {

            for (Map.Entry<String, String> layer : LAYERS.entrySet()) {
                writeLayer(writer, layer.getKey(), layer.getValue());
            }

            writeConfigFile(writer, POM_FILE, "Build Configuration (pom.xml)");
            writeConfigFile(writer,
                    RESOURCES_ROOT + "/application.properties",
                    "Application Configuration (application.properties)"
            );
        }

        System.out.println("✅ Export completed: " + OUTPUT_FILE);
    }

    private static void writeLayer(
            BufferedWriter writer,
            String heading,
            String folder
    ) throws IOException {

        Path basePath = folder.isEmpty()
                ? Paths.get(JAVA_ROOT)
                : Paths.get(JAVA_ROOT, folder);

        if (!Files.exists(basePath)) return;

        writer.write("# " + heading);
        writer.newLine();
        writer.newLine();

        Files.walk(basePath)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(p -> writeJavaFile(writer, p));
    }

    private static void writeJavaFile(
            BufferedWriter writer,
            Path file
    ) {
        try {
            writer.write("## " + file.getFileName());
            writer.newLine();
            writer.write("```java");
            writer.newLine();
            writer.write(Files.readString(file));
            writer.newLine();
            writer.write("```");
            writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeConfigFile(
            BufferedWriter writer,
            String filePath,
            String heading
    ) throws IOException {

        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return;

        writer.write("# " + heading);
        writer.newLine();
        writer.newLine();
        writer.write("```");
        writer.newLine();
        writer.write(Files.readString(path));
        writer.newLine();
        writer.write("```");
        writer.newLine();
        writer.newLine();
    }
}
