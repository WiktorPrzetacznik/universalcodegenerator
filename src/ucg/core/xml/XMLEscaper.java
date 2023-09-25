package ucg.core.xml;

public final class XMLEscaper {

    private XMLEscaper() {}

    public static String escapeChars(String input) {
        return input.replaceAll("\"", "&quot;")
                .replaceAll("\'", "&apos;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;");
    }

    public static String unescapeChars(String input) {
        return input.replaceAll("&quot;", "\"")
                .replaceAll("&apos;", "\'")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&");
    }

}