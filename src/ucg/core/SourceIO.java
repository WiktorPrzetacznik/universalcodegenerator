package ucg.core;

import ucg.lang.Lang;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SourceIO {

    private SourceIO() {}

    public static List<Source> read(Lang lang, String dir, boolean xml) throws IOException {
        String fileExtension = getExtension(lang, xml);
        List<Source> srcItems = new ArrayList<>();
        for (File file : Objects.requireNonNull(new File(dir).listFiles())) {
            if (file.isDirectory()) {
                srcItems.addAll(read(lang, file.getAbsolutePath(), xml));
            } else {
                String extension = "";
                int i = file.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = file.getName().substring(i + 1);
                }
                if (!extension.equals(fileExtension)) {
                    continue;
                }

                srcItems.add(new Source(lang, file, Files.readString(file.toPath(), StandardCharsets.UTF_8)));
            }
        }
        return srcItems;
    }

    public static void write(List<Source> srcItems) throws IOException {
        for (Source src : srcItems) {
            boolean fileCreated = src.file().createNewFile();
            if (fileCreated) {
                Files.writeString(src.file().toPath(), src.content());
            } else {
                FileWriter fileWriter = new FileWriter(src.file(), false);
                fileWriter.write(src.content());
                fileWriter.close();
            }
        }
    }

    private static String getExtension(Lang lang, boolean xml) {
        return xml ? "xml" : switch (lang) {
            case J -> "java";
            case JS -> "js";
            case CS -> "cs";
        };
    }

}