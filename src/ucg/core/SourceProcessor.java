package ucg.core;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ucg.core.xml.XMLEscaper;
import ucg.core.xml.XMLParseTreeListener;
import ucg.lang.Lang;
import ucg.lang.cs.CSharpLexer;
import ucg.lang.cs.CSharpParser;
import ucg.lang.java.JavaLexer;
import ucg.lang.java.JavaParser;
import ucg.lang.js.JavaScriptLexer;
import ucg.lang.js.JavaScriptParser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SourceProcessor {

    private SourceProcessor() {}

    public static Map<String, Integer> classify(Lang lang, List<Source> srcItems) {
        Map<String, Integer> res = new HashMap<>();
        for (Source srcItem : srcItems) {
            String src = srcItem.content();
            CharStream srcStream = CharStreams.fromString(src);
            Lexer lexer = switch (lang) {
                case CS -> new CSharpLexer(srcStream);
                case J -> new JavaLexer(srcStream);
                case JS -> new JavaScriptLexer(srcStream);
            };
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            tokenStream.fill();
            List<Token> tokens = tokenStream.getTokens();

            Classifier.classifyIf(lang, tokens, res);
            Classifier.classifyFor(lang, tokens, res);
            Classifier.classifyWhile(lang, tokens, res);
            Classifier.classifyAssignment(lang, tokens, res);
            Classifier.classifyArithOp(lang, tokens, res);
            Classifier.classifyLogOp(lang, tokens, res);
            Classifier.classifyCompOp(lang, tokens, res);
            Classifier.classifyBitOp(lang, tokens, res);
            Classifier.classifyNumberLit(lang, tokens, res);
            Classifier.classifyStringLit(lang, tokens, res);
            Classifier.classifyBoolLit(lang, tokens, res);
        }
        return res;
    }

    public static List<Source> srcToXml(Lang lang, List<Source> srcItems) {
        List<Source> xmlSrcItems = new ArrayList<>();
        for (Source srcItem : srcItems) {
            String src = srcItem.content();
            if (lang == Lang.JS)
                src = minifyJs(src);
            CharStream srcStream = CharStreams.fromString(src);
            Lexer lexer = switch (lang) {
                case CS -> new CSharpLexer(srcStream);
                case J -> new JavaLexer(srcStream);
                case JS -> new JavaScriptLexer(srcStream);
            };
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            ParserWithTree parserWithTree = switch (lang) {
                case CS -> {
                    CSharpParser parser = new CSharpParser(tokenStream);
                    yield new ParserWithTree(parser, parser.compilation_unit());
                }
                case J -> {
                    JavaParser parser = new JavaParser(tokenStream);
                    yield new ParserWithTree(parser, parser.compilationUnit());
                }
                case JS -> {
                    JavaScriptParser parser = new JavaScriptParser(tokenStream);
                    yield new ParserWithTree(parser, parser.program());
                }
            };
            XMLParseTreeListener listener = new XMLParseTreeListener(parserWithTree.parser());
            ParseTreeWalker.DEFAULT.walk(listener, parserWithTree.tree());
            xmlSrcItems.add(new Source(lang, new File(srcItem.file().toPath() + ".xml"),
                    listener.getXmlString()));
        }
        return xmlSrcItems;
    }

    public static List<Source> xmlToSrc(Lang lang, List<Source> xmlSrcItems) throws ParserConfigurationException, IOException, SAXException {
        List<Source> srcItems = new ArrayList<>();
        for (Source xmlSrcItem : xmlSrcItems) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlSrcItem.content().getBytes(StandardCharsets.UTF_8)));
            doc.getDocumentElement().normalize();

            StringBuilder sb = new StringBuilder();
            processNodes(doc.getChildNodes(), sb, lang, SpecialAction.NONE);

            String newFile = xmlSrcItem.file().toPath().toString().substring(0,
                    xmlSrcItem.file().toPath().toString().length() - 4);
            srcItems.add(new Source(lang, new File(newFile), sb.toString()));
        }
        return srcItems;
    }

    private static void processNodes(NodeList nodeList, StringBuilder sb, Lang lang, SpecialAction specialAction) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                String text = XMLEscaper.unescapeChars(node.getTextContent());
                if (lang == Lang.J && specialAction == SpecialAction.JAVA_IMPORT_STATIC_FIX) {
                    text = text.replace("importstatic", "import static");
                }
                sb.append(text).append(" ");
            }
            SpecialAction newSpecialAction;
            if (node.getNodeName().equals("importDeclaration")) {
                newSpecialAction = SpecialAction.JAVA_IMPORT_STATIC_FIX;
            } else {
                newSpecialAction = SpecialAction.NONE;
            }
            if (node.hasChildNodes()) {
                processNodes(node.getChildNodes(), sb, lang, newSpecialAction);
            }
        }
    }

    //---HELPERS

    private record ParserWithTree(Parser parser, ParseTree tree) {}

    private enum SpecialAction {
        NONE, JAVA_IMPORT_STATIC_FIX
    }

    private static String minifyJs(String jsSrc) {
        Compiler compiler = new Compiler();
        CompilerOptions options = new CompilerOptions();
        options.setEmitUseStrict(false);
        CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
        SourceFile extern = SourceFile.fromCode("externs.js", "function alert(x) {}");
        SourceFile input = SourceFile.fromCode("input.js", jsSrc);
        compiler.compile(extern, input, options);
        return compiler.toSource();
    }

}