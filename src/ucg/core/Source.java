package ucg.core;

import ucg.lang.Lang;

import java.io.File;

public record Source(Lang lang, File file, String content) {
}