package ucg.core.xml;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class XMLParseTreeListener implements ParseTreeListener {

    private final Parser parser;
    private final StringBuilder xmlBuilder;

    public XMLParseTreeListener(Parser parser) {
        this.parser = parser;
        xmlBuilder = new StringBuilder();
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        String value = node.getText();
        if (!value.matches("\\s+")  && !value.equals("<EOF>")) {
            xmlBuilder.append(String.format("%s", XMLEscaper.escapeChars(value)));
        }
    }

    @Override
    public void visitErrorNode(ErrorNode node) {}

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {
        xmlBuilder.append(String.format("<%s>", parser.getRuleNames()[ctx.getRuleIndex()]));
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        xmlBuilder.append(String.format("</%s>", parser.getRuleNames()[ctx.getRuleIndex()]));
    }

    public String getXmlString() {
        return xmlBuilder.toString();
    }

    public Parser getParser() {
        return parser;
    }


}