package org.example.functions;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.example.gen.PythonLexer;
import org.example.gen.PythonParser;
import org.example.io.MyANTLRErrorListener;
import org.example.parsers.PythonLikeParser;

import java.io.IOException;
/**
 * Klasa narzędziowa do generowania kodu Java przy użyciu ANTLR.
 *
 * <p>
 * Ta klasa dostarcza metody do generowania kodu Java na podstawie ciagu wejsciowego przy użyciu ANTLR.
 * </p>
 *
 * @author Kacper Dworak
 * @version 1.2
 */
public class GenPython {
    /**
     * Generuje kod Python na podstawie ciagu wejsciowego, katalogu wyjsciowego i UUID.
     *
     * @param input ciag wejsciowy zawierajacy kod w okreslonym formacie
     * @param dir   katalog wyjsciowy dla wygenerowanego kodu Python
     * @param UUID  UUID skojarzone z procesem generowania
     * @return wygenerowany kod Python jako ciag znaksw
     * @throws IOException jesli wystapi blad podczas generowania kodu
     */
    public static String genPython(String input,String dir, String UUID) throws IOException {
        try {
            CharStream in;
            String[] split;
            if (input != null) {
                in = CharStreams.fromString(input);
            } else {
                in = CharStreams.fromFileName("src/INPUT.cc");
            }
            split = in.toString().split("[(]", 2);

            PythonLexer lexerPython = new PythonLexer(in);
            lexerPython.removeErrorListeners();
            lexerPython.addErrorListener(MyANTLRErrorListener.INSTANCE);
            CommonTokenStream tokensPython = new CommonTokenStream(lexerPython);
            PythonParser parserPython = new PythonParser(tokensPython);
            parserPython.removeErrorListeners();
            parserPython.addErrorListener(MyANTLRErrorListener.INSTANCE);
            PythonLikeParser.SetUUID(UUID);
            PythonLikeParser.SetDir(dir);
            ParseTree treePython = switch (split[0]) {
                case "Seq" -> parserPython.seq();
                case "Alt" -> parserPython.alt();
                case "Cond" -> parserPython.cond();
                case "Para" -> parserPython.para();
                case "Loop" -> parserPython.loop();
                case "Choice" -> parserPython.choice();
                case "SeqSeq" -> parserPython.seqSeq();
                case "Repeat" -> parserPython.repeat();
                default -> throw new IllegalStateException("Unexpected value: " + split[0]);
            };
            ParseTreeWalker walkerPython = new ParseTreeWalker();
            PythonLikeParser listenerPython = new PythonLikeParser();
            walkerPython.walk(listenerPython, treePython);
            //Return string
            return "\n\n\n\nPython\n\n\n\n"+PythonLikeParser.getResult();
        }catch (RecognitionException e){

            return MyANTLRErrorListener.INSTANCE.toString();
        }
    }
}
