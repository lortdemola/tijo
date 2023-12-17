package org.example.functions;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.example.gen.JavaLexer;
import org.example.gen.JavaParser;
import org.example.io.MyANTLRErrorListener;
import org.example.parsers.JavaLikeParser;

import java.io.IOException;
/**
 * Klasa narzędziowa do generowania kodu Java przy użyciu ANTLR.
 *
 * <p>
 * Ta klasa dostarcza metody do generowania kodu Java na podstawie ciagu wejsciowego przy użyciu ANTLR.
 * </p>
 *
 * @author Kacper Dworak
 * @version 9.15
 */
public class GenJava {
    /**
     * Generuje kod Java na podstawie ciagu wejsciowego, katalogu wyjsciowego i UUID.
     *
     * @param input ciag wejsciowy zawierajacy kod w okreslonym formacie
     * @param dir   katalog wyjsciowy dla wygenerowanego kodu Java
     * @param UUID  UUID skojarzone z procesem generowania
     * @return wygenerowany kod Java jako ciag znakow
     * @throws IOException jesli wystapi blad podczas generowania kodu
     */
    public static String genJava(String input, String dir, String UUID) throws IOException {
        try {


            CharStream in;
            String[] split;
            if (input != null) {
                in = CharStreams.fromString(input);
            } else {
                in = CharStreams.fromFileName("src/INPUT.cc");
            }
            split = in.toString().split("[(]", 2);

            JavaLexer lexer = new JavaLexer(in);
            lexer.removeErrorListeners();
            lexer.addErrorListener(MyANTLRErrorListener.INSTANCE);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);
            parser.removeErrorListeners();
            parser.addErrorListener(MyANTLRErrorListener.INSTANCE);
            JavaLikeParser.SetUUID(UUID);
            JavaLikeParser.SetDir(dir);
            ParseTree tree = switch (split[0]) {
                case "Seq" -> parser.seq();
                case "Alt" -> parser.alt();
                case "Cond" -> parser.cond();
                case "Para" -> parser.para();
                case "Loop" -> parser.loop();
                case "Choice" -> parser.choice();
                case "SeqSeq" -> parser.seqSeq();
                case "Repeat" -> parser.repeat();
                default -> throw new IllegalStateException("Unexpected value: " + split[0]);
            };
            ParseTreeWalker javaWalker = new ParseTreeWalker();
            JavaLikeParser listener = new JavaLikeParser();
            javaWalker.walk(listener, tree);

            return "Java\n\n\n\n"+JavaLikeParser.getResult();
        }catch (Exception e){
            return MyANTLRErrorListener.INSTANCE.toString();
        }
    }
}
