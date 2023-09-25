package ucg;

import ucg.core.Source;
import ucg.core.SourceIO;
import ucg.core.SourceProcessor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class Main {

    public static void main(String[] args) throws Exception {
        Optional<Config> configOpt = Config.from(args);
        if (configOpt.isEmpty())
            return;
        Config config = configOpt.get();
        switch (config.mode()) {
            case SRC_TO_XML -> srcToXml(config);
            case XML_TO_SRC -> xmlToSrc(config);
            case CLASSIFY -> classify(config);
        }
        System.out.println("Gotowe!");
        System.in.read();
    }

    private static void srcToXml(Config config) throws Exception {
        List<Source> ogSrcItems = SourceIO.read(config.lang(), config.dir(), false);
        List<Source> xmlSrcItems = SourceProcessor.srcToXml(config.lang(), ogSrcItems);
        SourceIO.write(xmlSrcItems);
    }

    private static void xmlToSrc(Config config) throws Exception {
        List<Source> procSrcItems = SourceProcessor.xmlToSrc(config.lang(),
                SourceIO.read(config.lang(), config.dir(), true));
        SourceIO.write(procSrcItems);
    }

    private static void classify(Config config) throws Exception {
        List<Source> srcItems = SourceIO.read(config.lang(), config.dir(), false);
        Map<String, Integer> res = SourceProcessor.classify(config.lang(), srcItems);
        System.out.println("Rezultat klasyfikacji:");
        System.out.println(res);
    }

}