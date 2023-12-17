package org.example.io;


import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
/**
 * Klasa implementujaca interfejs ANTLRErrorListener do obslugi bledow ANTLR.
 *
 * <p>
 * Ta klasa obsluguje bledy zwiazane z parsowaniem gramatyki ANTLR.
 * </p>
 *
 * <p>
 * Wartosc REPORT_SYNTAX_ERRORS kontroluje, czy metoda syntaxError ma wyswietlac bledy.
 * </p>
 *
 * @author Kacper Dworak
 * @version 1.0
 */
public class MyANTLRErrorListener implements ANTLRErrorListener {

    public static MyANTLRErrorListener INSTANCE = new MyANTLRErrorListener();

    //When the value is false, the syntaxError method returns without displaying errors.
    private static final boolean REPORT_SYNTAX_ERRORS = true;

    private String errorMsg = "";
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException re) {

        if (!REPORT_SYNTAX_ERRORS) {
            return;
        }

        String sourceName = recognizer.getInputStream().getSourceName();
        if (!sourceName.isEmpty()) {
            sourceName = String.format("%s:%d:%d: ", sourceName, line, charPositionInLine);
        }
        errorMsg = "";
        System.err.println(sourceName+"line "+line+":"+charPositionInLine+" "+msg);
        errorMsg = errorMsg + "\n" +"line "+line+":"+charPositionInLine+" "+msg;
    }

    @Override
    public void reportAmbiguity(Parser parser, DFA dfa, int i, int i1, boolean b, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportAttemptingFullContext(Parser parser, DFA dfa, int i, int i1, BitSet bitSet, ATNConfigSet atnConfigSet) {

    }

    @Override
    public void reportContextSensitivity(Parser parser, DFA dfa, int i, int i1, int i2, ATNConfigSet atnConfigSet) {

    }
    /**
     * Zwraca reprezentacje tekstowa bledu.
     *
     * @return tekstowa reprezentacja bledu
     */
    @Override
    public String toString() {
        return errorMsg;
    }
}
