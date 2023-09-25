package ucg.core;

import org.antlr.v4.runtime.Token;
import ucg.lang.Lang;
import ucg.lang.cs.CSharpLexer;
import ucg.lang.java.JavaLexer;
import ucg.lang.js.JavaScriptLexer;

import java.util.List;
import java.util.Map;

final class Classifier {

    private Classifier() {}

    public static void classifyIf(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens, JavaLexer.IF, JavaScriptLexer.If, CSharpLexer.IF, "if", res);
    }

    public static void classifyFor(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens, JavaLexer.FOR, JavaScriptLexer.For, CSharpLexer.FOR, "for", res);
    }

    public static void classifyWhile(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens, JavaLexer.WHILE, JavaScriptLexer.While, CSharpLexer.WHILE, "while", res);
    }

    public static void classifyAssignment(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens,
                List.of(JavaLexer.ASSIGN, JavaLexer.ADD_ASSIGN, JavaLexer.AND_ASSIGN, JavaLexer.OR_ASSIGN, JavaLexer.LSHIFT_ASSIGN, JavaLexer.DIV_ASSIGN, JavaLexer.MOD_ASSIGN, JavaLexer.MUL_ASSIGN, JavaLexer.RSHIFT_ASSIGN, JavaLexer.SUB_ASSIGN, JavaLexer.URSHIFT_ASSIGN, JavaLexer.XOR_ASSIGN),
                List.of(14, 58, 56, 49, 50, 57, 53, 52, 48, 51, 59, 54, 55),
                List.of(145, 165, 160, 166, 163, 170, 169, 164, 162, 161, 167),
                "assignment", res);
    }

    public static void classifyArithOp(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens,
                List.of(JavaLexer.ADD, JavaLexer.SUB, JavaLexer.MUL, JavaLexer.DIV, JavaLexer.MOD, JavaLexer.INC, JavaLexer.DEC),
                List.of(JavaScriptLexer.Plus, JavaScriptLexer.Minus, JavaScriptLexer.Multiply, JavaScriptLexer.Divide, JavaScriptLexer.Modulus, JavaScriptLexer.PlusPlus, JavaScriptLexer.MinusMinus),
                List.of(CSharpLexer.PLUS, CSharpLexer.MINUS, CSharpLexer.STAR, CSharpLexer.DIV, CSharpLexer.PERCENT, CSharpLexer.OP_INC, CSharpLexer.OP_DEC),
                "arithop", res);
    }

    public static void classifyLogOp(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens,
                List.of(JavaLexer.AND, JavaLexer.OR),
                List.of(JavaScriptLexer.And, JavaScriptLexer.Or, JavaScriptLexer.Not),
                List.of(CSharpLexer.OP_AND, CSharpLexer.OP_OR),
                "logOp", res);
    }

    public static void classifyCompOp(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens,
                List.of(JavaLexer.EQUAL, JavaLexer.NOTEQUAL, JavaLexer.GT, JavaLexer.LT, JavaLexer.GE, JavaLexer.LE),
                List.of(JavaScriptLexer.Equals_, JavaScriptLexer.IdentityEquals, JavaScriptLexer.NotEquals, JavaScriptLexer.IdentityNotEquals, JavaScriptLexer.MoreThan, JavaScriptLexer.LessThan, JavaScriptLexer.GreaterThanEquals, JavaScriptLexer.LessThanEquals, JavaScriptLexer.QuestionMark),
                List.of(CSharpLexer.OP_EQ, CSharpLexer.GT, CSharpLexer.LT, CSharpLexer.OP_GE, CSharpLexer.OP_LE, CSharpLexer.OP_NE),
                "compOp", res);
    }

    public static void classifyBitOp(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens,
                List.of(JavaLexer.BITOR, JavaLexer.BITAND, JavaLexer.TILDE),
                List.of(JavaScriptLexer.BitOr, JavaScriptLexer.BitAnd, JavaScriptLexer.BitNot, JavaScriptLexer.BitXOr),
                List.of(CSharpLexer.BITWISE_OR, CSharpLexer.TILDE),
                "bitOp", res);
    }

    public static void classifyNumberLit(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens,
                List.of(JavaLexer.INT, JavaLexer.BINARY_LITERAL, JavaLexer.HEX_LITERAL, JavaLexer.DECIMAL_LITERAL, JavaLexer.FLOAT_LITERAL, JavaLexer.HEX_FLOAT_LITERAL, JavaLexer.OCT_LITERAL),
                List.of(70, 68, 71, 67, 64, 69, 65, 66, 63),
                List.of(CSharpLexer.BIN_INTEGER_LITERAL, CSharpLexer.INTEGER_LITERAL, CSharpLexer.HEX_INTEGER_LITERAL, CSharpLexer.FLOAT, CSharpLexer.DECIMAL),
                "numberLit", res);
    }

    public static void classifyStringLit(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens,
                List.of(JavaLexer.STRING_LITERAL),
                List.of(JavaScriptLexer.StringLiteral),
                List.of(CSharpLexer.STRING),
                "stringLit", res);
    }

    public static void classifyBoolLit(Lang lang, List<Token> tokens, Map<String, Integer> res) {
        classify(lang, tokens,
                List.of(JavaLexer.BOOL_LITERAL),
                List.of(JavaScriptLexer.BooleanLiteral),
                List.of(CSharpLexer.BOOL),
                "boolLit", res);
    }

    //---HELPER

    private static void classify(Lang lang, List<Token> tokens,
                                 List<Integer> jWantedTokens, List<Integer> jsWantedTokens, List<Integer> csWantedTokens,
                                 String resName, Map<String, Integer> res) {
        for (Token token : tokens) {
            List<Integer> wantedTokens = switch (lang) {
                case J -> jWantedTokens;
                case JS -> jsWantedTokens;
                case CS -> csWantedTokens;
            };
            for (Integer wantedToken : wantedTokens) {
                if (token.getType() == wantedToken) {
                    if (res.containsKey(resName)) {
                        res.put(resName, res.get(resName) + 1);
                    } else {
                        res.put(resName, 1);
                    }
                    break;
                }
            }
        }
    }

    private static void classify(Lang lang, List<Token> tokens,
                                 int jWantedToken, int jsWantedToken, int csWantedToken,
                                 String resName, Map<String, Integer> res) {
        classify(lang, tokens, List.of(jWantedToken), List.of(jsWantedToken), List.of(csWantedToken), resName, res);
    }

}