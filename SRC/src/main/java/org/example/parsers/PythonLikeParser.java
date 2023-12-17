package org.example.parsers;




import org.example.gen.PythonBaseListener;
import org.example.gen.PythonParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
/**
 * Klasa PythonLikeParser odpowiada za parsowanie kodu w stylu jezyka Python.
 *
 * <p>
 * Implementuje interfejs PythonBaseListener z biblioteki ANTLR4.
 * Interpretuje strukture kodu zrodlowego i generuje kod w jezyku Python.
 * </p>
 *
 *
 * @author Kacper Dworak
 * @version 2.4
 */
public class PythonLikeParser extends PythonBaseListener {
    List<String> functions = new ArrayList<String>();
    List<String> Treads = new ArrayList<String>();
    Stack<String> stack = new Stack<>();
    Map<String, String> subProcess = new HashMap<String, String>();
    static String FinalString = "";
    static String UUID = "";
    static String DIR = "";
    boolean isBranch= false;
    boolean isConcur= false;
    static boolean notTest = false;
    /**
     * Metoda setUUID sluzy do ustawienia identyfikatora.
     */
    public static void SetUUID(String uuid) {
        UUID = uuid;
    }
    /**
     * Metoda SetDir sluzy do ustawienia katalogu docelowego.
     */
    public static void SetDir(String Dir) {
        DIR = Dir;
    }
    /**
     * Metoda SetTest sluzy do ustawienia flagi testowej.
     */
    public static void SetTest(boolean test) {
        notTest = test;
    }
    /**
     * Metoda toFile() zapisuje wynikowy kod do plikow tekstowego i Python.
     *
     * Kod wynikowy jest przechowywany na stosie, a nastepnie zapisywany
     * do plikow o nazwach zależnych od identyfikatora (UUID) oraz
     * kierunku (DIR). Nazwy plikow tworzone sa poprzez dodanie odpowiednich
     * rozszerzen "_P.txt" dla pliku tekstowego i "_P.py" dla pliku Python.
     *
     * Jesli flaga notTest nie jest ustawiona, kod zostanie wydrukowany
     * na standardowe wyjscie za pomoca System.out.println().
     *
     * @throws IOException Wystepuje, gdy wystapi problem z zapisem do plikow.
     *
     * @see #getResult()
     * @see PythonLikeParser#SetUUID(String)
     * @see PythonLikeParser#SetDir(String)
     * @see PythonLikeParser#SetTest(boolean)
     * @see PythonLikeParser#FinalString
     */
    public void toFile(String string) {
        StringBuilder data = new StringBuilder();
        data.append(string);
        for (String s : stack) {

            data.append(s);
        }
        String txtFile = UUID + "_P.txt";
        String pythonFile = UUID + "_P.py";
        FinalString = data.toString();
        if(!notTest){
        try {
            FileWriter writerTxt = new FileWriter(new File(DIR, txtFile));
            //FileWriter writerTxt = new FileWriter(txtFile);
            writerTxt.write(FinalString);
            writerTxt.close();
            FileWriter writerPython = new FileWriter(new File(DIR, pythonFile));
           // FileWriter writerPython = new FileWriter(pythonFile);
            writerPython.write(FinalString);
            writerPython.close();
            System.out.println("The data has been saved to files " + txtFile + " and " + pythonFile);
        } catch (IOException e) {
            System.out.println("An error occurred while saving to files.");
            e.printStackTrace();
            getResult();
        }
        }
    }

    public static String getResult() {
        return FinalString;
    }
    @Override
    public void exitDelay(PythonParser.DelayContext ctx){
        StringBuilder sb = new StringBuilder();
        String s1 = stack.pop();
        sb.append("time.sleep(").append(s1).append(")");
        stack.push(sb.toString());
    }

    @Override
    public void exitSub(PythonParser.SubContext ctx) {
        StringBuilder sb = new StringBuilder();
        String p_rule = stack.pop();
        if(ctx.string().size() > 1){
            sb.append("(");
            for (int i = 0; i < ctx.string().size()-1; i++) {
                sb.append(stack.pop().replace(";", "")).append(",");
            }
        }

        String name = stack.pop().replace(";", "");
        if (ctx.string().size() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }else{
            sb.append("(");
        }
        sb.append(")");
        StringBuilder sb_Func = new StringBuilder();
        sb_Func.append(name);
        sb_Func.append(sb.toString());
        stack.push(sb_Func.toString());
        if (!functions.contains(sb_Func.toString())) {
            functions.add(sb_Func.toString());
        }
        if (!subProcess.containsKey(name)) {
            subProcess.put(name, p_rule);
        }

    }
    @Override
    public void exitCout(PythonParser.CoutContext ctx) {
        StringBuilder sb = new StringBuilder();
        String s1 = stack.pop();
        if(ctx.arg_cout().string() != null || ctx.arg_cout().special_String() != null){
            sb.append("\nprint('").append(s1.replace(";", "")).append("')");
        }else{
            sb.append("\nprint(").append(s1.replace(";", "")).append(")");
        }
        stack.push(sb.toString());

    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `seq` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Python, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `seq` w parserze.
     *
     * @see PythonParser#seq()
     */
    @Override
    public void exitSeq(PythonParser.SeqContext ctx) {
        StringBuilder sb = new StringBuilder();

        if(isBranch||isConcur){

            String s1 = stack.pop();

            sb.append(s1).append("\n");
            if(isBranch){
                isBranch= false;
            }else{
                isConcur= false;
            }

        }else {
            String s2 = stack.pop();
            String s1 = stack.pop();

            sb.append(s1).append("\n").append(s2).append("\n");
        }
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            StringBuilder finalSB = new StringBuilder();
            while (Treads.size() > 0) {
                finalSB.append(Treads.get(0));
                Treads.remove(0);
            }
            if (Treads.size() == 0) {
                //stack.push(sb.toString());
                //sb.setLength(0);
            }
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            finalSB.append("def ").append(functions.get(0)).append(":\n     funcbody\n      ").append(s.getValue()).append("\n\n     outof_func \n");
                            subProcess.remove(s.getKey());
                        } else {
                            finalSB.append("def ").append(functions.get(0)).append(" :\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                        }
                    }
                    functions.remove(0);
                } else {
                    finalSB.append("\n\ndef ").append(functions.get(0)).append(":\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {

            }
            toFile(finalSB.toString());
        }

    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `alt` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Python, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `alt` w parserze.
     *
     * @see PythonParser#alt()
     */
    @Override
    public void exitAlt(PythonParser.AltContext ctx) {
        StringBuilder sb = new StringBuilder();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append("if ").append(s1).append(": \n").append(s2).append("\n").append(s3).append("\n");

        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            StringBuilder finalSB = new StringBuilder();
            while (Treads.size() > 0) {
                finalSB.append(Treads.get(0));
                Treads.remove(0);
            }
            if (Treads.size() == 0) {
                //stack.push(sb.toString());
                //sb.setLength(0);
            }
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            finalSB.append("def ").append(functions.get(0)).append(":\n     funcbody\n      ").append(s.getValue()).append("\n\n     outof_func \n");
                            subProcess.remove(s.getKey());
                        } else {
                            finalSB.append("def ").append(functions.get(0)).append(" :\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                        }
                    }
                    functions.remove(0);
                } else {
                    finalSB.append("\n\ndef ").append(functions.get(0)).append(":\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                finalSB.append(stack);
                stack.clear();
                stack.push(finalSB.toString());
            }
            toFile(finalSB.toString());
        }
    }


    @Override
    public void exitFunction(PythonParser.FunctionContext ctx) {
        StringBuilder sb = new StringBuilder();
        sb.append(ctx.CharArray().getText()).append("(");
        for (PythonParser.Arg_pythonContext arg : ctx.arg_python()) {
            sb.append(stack.pop()).append(",");
        }
        if (ctx.arg_python().size() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(")");
        stack.push(sb.toString());
        if (!functions.contains(sb.toString())) {
            functions.add(sb.toString());
        }
    }

    @Override
    public void exitString(PythonParser.StringContext ctx) {

        stack.push(ctx.CharArray().getText().replace("{","(").replace("}",")"));
    }
    @Override
    public void exitNum(PythonParser.NumContext ctx) {

        stack.push(ctx.NumArray().toString());
    }
    @Override
    public void exitSpecial_String(PythonParser.Special_StringContext ctx) {

        stack.push(ctx.CharArray().getText().replace("#", "").replace(";", "\n"));
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `branchRe` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Python, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `branchRe` w parserze.
     *
     * @see PythonParser#branchRe()
     */
    @Override
    public void exitBranchRe(PythonParser.BranchReContext ctx) {
        isBranch = true;
        StringBuilder sb = new StringBuilder();
        String s6 = stack.pop();
        String s5 = stack.pop();
        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append("if ").append(s1)
                .append(":\n   ")
                .append(s2)
                .append("\n   ")
                .append(s4)
                .append("\nelse:\n   ")
                .append(s3)
                .append("\n   ")
                .append(s5)
                .append("\n\n")
                .append(s6);stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            StringBuilder finalSB = new StringBuilder();
            while (Treads.size() > 0) {
                finalSB.append(Treads.get(0));
                Treads.remove(0);
            }
            if (Treads.size() == 0) {
                //stack.push(sb.toString());
                //sb.setLength(0);
            }
            while (functions.size() > 0) {
                 if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            finalSB.append("def ").append(functions.get(0)).append(":\n     funcbody\n      ").append(s.getValue()).append("\n\n     outof_func \n");
                            subProcess.remove(s.getKey());
                        } else {
                            finalSB.append("def ").append(functions.get(0)).append(" :\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                        }
                    }
                    functions.remove(0);
                } else {
                     finalSB.append("\n\ndef ").append(functions.get(0)).append(":\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                finalSB.append(stack);
                stack.clear();
                stack.push(finalSB.toString());
            }
            toFile(finalSB.toString());
        }

    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `seqSeq` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Python, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `seqSeq` w parserze.
     *
     * @see PythonParser#seqSeq()
     */
    @Override
    public void exitSeqSeq(PythonParser.SeqSeqContext ctx) {
        StringBuilder sb = new StringBuilder();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append(s1).append("\n").append(s2).append("\n").append(s3);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            StringBuilder finalSB = new StringBuilder();
            while (Treads.size() > 0) {
                finalSB.append(Treads.get(0));
                Treads.remove(0);
            }
            if (Treads.size() == 0) {
                //stack.push(sb.toString());
                //sb.setLength(0);
            }
            while (functions.size() > 0) {
                 if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("def ").append(functions.get(0)).append(":\n     funcbody\n      ").append(s.getValue()).append("\n\n     outof_func \n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("def ").append(functions.get(0)).append(" :\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\ndef ").append(functions.get(0)).append(":\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                stack.push(sb.toString());
            }
            toFile(finalSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `cond` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Python, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `cond` w parserze.
     *
     * @see PythonParser#cond()
     */
    @Override
    public void exitCond(PythonParser.CondContext ctx) {
        StringBuilder sb = new StringBuilder();

        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append("if ").append(s1.replace(";", "")).append(":\n   ").append(s2).append("\nelse:\n   ").append(s3).append("\n").append(s4);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            StringBuilder finalSB = new StringBuilder();
            while (Treads.size() > 0) {
                finalSB.append(Treads.get(0));
                Treads.remove(0);
            }
            if (Treads.size() == 0) {
                //stack.push(sb.toString());
                //sb.setLength(0);
            }
            while (functions.size() > 0) {
                 if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("def ").append(functions.get(0)).append(":\n     funcbody\n      ").append(s.getValue()).append("\n\n     outof_func \n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("def ").append(functions.get(0)).append(" :\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\ndef ").append(functions.get(0)).append(":\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                stack.push(sb.toString());
            }
            toFile(finalSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `choice` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Python, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `choice` w parserze.
     *
     * @see PythonParser#choice()
     */
    @Override
    public void exitChoice(PythonParser.ChoiceContext ctx) {
        StringBuilder sb = new StringBuilder();

        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append(s1).append("\nif :\n   ").append(s2).append("\nelse:\n   ").append(s3).append("\n").append(s4);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            StringBuilder finalSB = new StringBuilder();
            while (Treads.size() > 0) {
                finalSB.append(Treads.get(0));
                Treads.remove(0);
            }
            if (Treads.size() == 0) {
                //stack.push(sb.toString());
                //sb.setLength(0);
            }
            while (functions.size() > 0) {
                 if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("def ").append(functions.get(0)).append(":\n     funcbody\n      ").append(s.getValue()).append("\n\n     outof_func \n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("def ").append(functions.get(0)).append(" :\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\ndef ").append(functions.get(0)).append(":\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                stack.push(sb.toString());
            }
            toFile(finalSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `loop` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Python, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `loop` w parserze.
     *
     * @see PythonParser#loop()
     */
    @Override
    public void exitLoop(PythonParser.LoopContext ctx) {
        StringBuilder sb = new StringBuilder();

        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append(s1).append("\n").append("while ").append(s2).append(":\n    ").append(s3).append("\n").append(s4);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            StringBuilder finalSB = new StringBuilder();
            while (Treads.size() > 0) {
                finalSB.append(Treads.get(0));
                Treads.remove(0);
            }
            if (Treads.size() == 0) {
                //stack.push(sb.toString());
                //sb.setLength(0);
            }
            while (functions.size() > 0) {
                 if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("def ").append(functions.get(0)).append(":\n     funcbody\n      ").append(s.getValue()).append("\n\n     outof_func \n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("def ").append(functions.get(0)).append(" :\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\ndef ").append(functions.get(0)).append(":\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                stack.push(sb.toString());
            }
            toFile(finalSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `repeat` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Python, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `repeat` w parserze.
     *
     * @see PythonParser#repeat()
     */
    @Override
    public void exitRepeat(PythonParser.RepeatContext ctx) {
        StringBuilder sb = new StringBuilder();

        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append(s1).append("\n").append(s3).append("\nwhile ").append(s2).append(":\n    ").append(s3).append("\n").append(s4);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            StringBuilder finalSB = new StringBuilder();
            while (Treads.size() > 0) {
                finalSB.append(Treads.get(0));
                Treads.remove(0);
            }
            if (Treads.size() == 0) {
                //stack.push(sb.toString());
                //sb.setLength(0);
            }
            while (functions.size() > 0) {
                 if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("def ").append(functions.get(0)).append(":\n     funcbody\n      ").append(s.getValue()).append("\n\n     outof_func \n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("def ").append(functions.get(0)).append(" :\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\ndef ").append(functions.get(0)).append(":\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                stack.push(sb.toString());
            }
            toFile(finalSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `concurRe` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Python, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `concurRe` w parserze.
     *
     * @see PythonParser#concurRe()
     */
    @Override
    public void exitConcurRe(PythonParser.ConcurReContext ctx) {
        isConcur = true;
        StringBuilder sb = new StringBuilder();
        String s6 = stack.pop();
        String s5 = stack.pop();
        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        int num = Treads.size() * 2;
        Treads.add("\ndef Thread" + num + "():\n    " + s2 +"\n    " + s4 + "\n\ndef Thread" + (num + 1) + "():\n    " + s3 +"\n    " + s5 + "\n");
        sb.append(s1)
                .append("\nthread1 = threading.Thread(target=Thread")
                .append(num)
                .append(")")
                .append("\nthread2 = threading.Thread(target=Thread")
                .append(num + 1)
                .append(")")
                .append("\nthread1.start()\nthread2.start()\n")
                .append("thread1.join()\nthread2.join()\n")
                .append(s6);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            StringBuilder finalSB = new StringBuilder();
            while (Treads.size() > 0) {
                finalSB.append(Treads.get(0));
                Treads.remove(0);
            }
            if (Treads.size() == 0) {
                //stack.push(sb.toString());
                //sb.setLength(0);
            }
            while (functions.size() > 0) {
                 if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("def ").append(functions.get(0)).append(":\n     funcbody\n      ").append(s.getValue()).append("\n\n     outof_func \n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("def ").append(functions.get(0)).append(" :\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\ndef ").append(functions.get(0)).append(":\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                stack.push(sb.toString());
            }
            toFile(finalSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `para` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Python, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `para` w parserze.
     *
     * @see PythonParser#para()
     */
    @Override
    public void exitPara(PythonParser.ParaContext ctx) {
        StringBuilder sb = new StringBuilder();

        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        int num = Treads.size() * 2;
        Treads.add("\ndef Thread" + num + "():\n    " + s2 + "\n\ndef Thread" + (num + 1) + "():\n    " + s3 + "\n");
        sb.append(s1).append("\nthread1 = threading.Thread(target=Thread").append(num).append(")").append("\nthread2 = threading.Thread(target=Thread").append(num + 1).append(")").append("\nthread1.start()\nthread2.start()\n").append("thread1.join()\nthread2.join()\n").append(s4);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            StringBuilder finalSB = new StringBuilder();
            while (Treads.size() > 0) {
                finalSB.append(Treads.get(0));
                Treads.remove(0);
            }
            if (Treads.size() == 0) {
                //stack.push(sb.toString());
                //sb.setLength(0);
            }
            while (functions.size() > 0) {
                 if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("def ").append(functions.get(0)).append(":\n     funcbody\n      ").append(s.getValue()).append("\n\n     outof_func \n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("def ").append(functions.get(0)).append(" :\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\ndef ").append(functions.get(0)).append(":\n     funcbody\n      // Add code here\n   \n     outof_func \n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                stack.push(sb.toString());
            }
            toFile(finalSB.toString());
        }

    }

}
