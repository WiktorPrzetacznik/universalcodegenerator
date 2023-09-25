package ucg;

import ucg.lang.Lang;

import java.util.Optional;

public record Config(Mode mode, String dir, Lang lang) {

    public static Optional<Config> from(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("help")) {
            System.out.println("Format argumentow: nazwa=wartosc");
            System.out.println("Dostepne argumenty:");
            System.out.println("mode:wartosc [dostepne: src_xml;xml_src;classify]");
            System.out.println("dir:wartosc [przyklad: C:\\Users\\abc\\Desktop\\project]");
            System.out.println("lang:wartosc [dostepne: cs;j;js]");
            System.in.read();
            return Optional.empty();
        } else if (args.length < 3) {
            System.err.println("Za malo argumentow, wymagane: mode;dir;lang");
            System.in.read();
            return Optional.empty();
        }

        Mode m = null;
        String d = null;
        Lang l = null;
        for (String arg : args) {
            String[] keyValuePair = arg.split("=");
            if (keyValuePair.length < 2) {
                System.err.println("Brak wartosci dla argumentu: " + keyValuePair[0]);
                System.in.read();
                return Optional.empty();
            }
            String key = keyValuePair[0];
            String val = keyValuePair[1];
            switch (key) {
                case "mode" -> m = parseMode(val);
                case "dir" -> d = val;
                case "lang" -> l = parseLang(val);
            }
        }
        if (m == null || d == null || l == null) {
            System.err.println("Niepoprawne wartosci argumentow!");
            System.in.read();
            return Optional.empty();
        }
        return Optional.of(new Config(m, d, l));
    }

    private static Lang parseLang(String langStr) {
        return switch (langStr) {
            case "cs" -> Lang.CS;
            case "j" -> Lang.J;
            case "js" -> Lang.JS;
            default -> null;
        };
    }

    private static Mode parseMode(String modeStr) {
        return switch (modeStr) {
            case "src_xml" -> Mode.SRC_TO_XML;
            case "xml_src" -> Mode.XML_TO_SRC;
            case "classify" -> Mode.CLASSIFY;
            default -> null;
        };
    }

    public enum Mode {
        CLASSIFY, SRC_TO_XML, XML_TO_SRC;
    }

}