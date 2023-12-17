package org.example.parsers;


import org.example.gen.JavaBaseListener;
import org.example.gen.JavaParser;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
/**
 * Klasa JavaLikeParser odpowiada za parsowanie kodu w stylu jezyka Java.
 *
 * <p>
 * Implementuje interfejs JavaBaseListener z biblioteki ANTLR4.
 * Interpretuje strukture kodu zrodlowego i generuje kod w jezyku Java.
 * </p>
 *
 *
 * @author Kacper Dworak
 * @version 13.9
 */
public class JavaLikeParser extends JavaBaseListener {
    static String FinalString = "";
    static String UUID = "";
    static String DIR = "";
    List<String> functions = new ArrayList<>();
    Stack<String> stack = new Stack<>();
    Map<String, String> subProcess = new HashMap<String, String>();
    boolean isBranch = false;
    static boolean notTest = false;
    boolean isConcur = false;
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
    public static String getResult() {
        return FinalString;
    }
    /**
     * Metoda toFile() zapisuje wynikowy kod do plikow tekstowego i Java.
     *
     * Kod wynikowy jest przechowywany na stosie, a nastepnie zapisywany
     * do plikow o nazwach zależnych od identyfikatora (UUID) oraz
     * kierunku (DIR). Nazwy plikow tworzone sa poprzez dodanie odpowiednich
     * rozszerzen "_J.txt" dla pliku tekstowego i "_J.java" dla pliku Java.
     *
     * Jesli flaga notTest nie jest ustawiona, kod zostanie wydrukowany
     * na standardowe wyjscie za pomoca System.out.println().
     *
     * @throws IOException Wystepuje, gdy wystapi problem z zapisem do plikow.
     *
     * @see #getResult()
     * @see JavaLikeParser#SetUUID(String)
     * @see JavaLikeParser#SetDir(String)
     * @see JavaLikeParser#SetTest(boolean)
     * @see JavaLikeParser#FinalString
     */
    public void toFile() {

            StringBuilder data = new StringBuilder();
            for (String s : stack) {
                if(!notTest){ //System.out.println(s);
                    //
                }
                data.append(s);
            }

            String txtFile =   UUID + "_J.txt";
            String javaFile =  UUID + "_J.java";
            FinalString = data.toString();
            if(!notTest){
            try {
                FileWriter writerTxt = new FileWriter(new File(DIR, txtFile));
                //FileWriter writerTxt = new FileWriter(txtFile);
                writerTxt.write(FinalString);
                writerTxt.close();
                FileWriter writerJava = new FileWriter(new File(DIR, javaFile));
                //FileWriter writerJava = new FileWriter(javaFile);
                writerJava.write(FinalString);
                writerJava.close();
                System.out.println("The data has been saved to files " + txtFile + " and " + javaFile);
            } catch (IOException e) {
                System.out.println("An error occurred while saving to files.");
                e.printStackTrace();
                getResult();
            }
        }
    }
    /**
     * Metoda wywolywana przy opuszczeniu produkcji `delay` w parserze.
     * Tworzy instrukcje opóźnienia w kodzie wynikowym przy użyciu
     * wartolci czasu pobranej ze stosu.
     *
     * @param ctx Kontekst produkcji `delay` w parserze.
     *
     * @see JavaParser#delay()
     */
    @Override
    public void exitDelay(JavaParser.DelayContext ctx){
        StringBuilder sb = new StringBuilder();
        String s1 = stack.pop();
        sb.append("Thread.sleep(").append(s1).append(");");
        stack.push(sb.toString());
    }
    @Override
    public void exitSub(JavaParser.SubContext ctx) {
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
        stack.push(sb_Func.toString() + ";");
        if (!functions.contains(sb_Func.toString())) {
            functions.add(sb_Func.toString());
        }
        if (!subProcess.containsKey(name)) {
            subProcess.put(name, p_rule);
        }

    }
    @Override
    public void exitCout(JavaParser.CoutContext ctx) {
        StringBuilder sb = new StringBuilder();
        String s1 = stack.pop();
        sb.append("\nSystem.out.println(").append(s1.replace(";", "")).append(")");
        stack.push(sb.toString() + ";");

    }
    @Override
    public void enterSeq(JavaParser.SeqContext ctx){
        if (ctx.depth() == 1) {
            StringBuilder firstSB = new StringBuilder();
            firstSB.append("public class ").append(UUID.replace(" ","_")).append("{\n\n public static void main(String[] args) {\n");
            stack.push(firstSB.toString());
        }
    }
    @Override
    public void enterAlt(JavaParser.AltContext ctx){
        if (ctx.depth() == 1) {
            StringBuilder firstSB = new StringBuilder();
            firstSB.append("public class ").append(UUID.replace(" ","_")).append("{\n\n public static void main(String[] args) {\n");
            stack.push(firstSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `seq` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `seq` w parserze.
     *
     * @see JavaParser#seq()
     */
    @Override
    public void exitSeq(JavaParser.SeqContext ctx) {

        StringBuilder sb = new StringBuilder();
        if (isBranch || isConcur) {

            String s1 = stack.pop();

            sb.append(s1).append("\n");
            if (isBranch) {
                isBranch = false;
            } else {
                isConcur = false;
            }
        } else {
            String s2 = stack.pop();
            String s1 = stack.pop();

            sb.append(s1).append("\n").append(s2).append("\n");
        }
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            sb.append("\n}\n");
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n").append(s.getValue()).append("\n}\n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                sb.append("\n}\n");
                stack.push(sb.toString());

            }
            toFile();
        }

    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `alt` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `alt` w parserze.
     *
     * @see JavaParser#alt()
     */
    @Override
    public void exitAlt(JavaParser.AltContext ctx) {
        StringBuilder sb = new StringBuilder();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append("if(").append(s1.replace(";", "")).append(") {\n").append(s2).append("\n}").append(s3).append("\n");

        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            sb.append("\n}\n");
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n").append(s.getValue()).append("\n}\n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                sb.append("\n}\n");
                stack.push(sb.toString());
            }
            toFile();
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `function` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `function` w parserze.
     *
     * @see JavaParser#function()
     */
    @Override
    public void exitFunction(JavaParser.FunctionContext ctx) {
        StringBuilder sb = new StringBuilder();
        if(ctx.CharArray().getText().contains(" ")){
            stack.push(ctx.CharArray().getText().replace("{","(").replace("}",")")+"();");
        }else {
            sb.append(ctx.CharArray().getText()).append("(");
            for (JavaParser.Arg_javaContext arg : ctx.arg_java()) {
                sb.append(stack.pop().replace(";", "")).append(",");
            }
            if (ctx.arg_java().size() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(")");
            stack.push(sb.toString() + ";");
            if (!functions.contains(sb.toString())) {
                functions.add(sb.toString());
            }
        }
    }
    @Override
    public void exitString(JavaParser.StringContext ctx) {
        String str = ctx.CharArray().getText();
        String decimalPattern = "([0-9]+.[0-9]+)";
        String floatPattern = "([0-9]+.[0-9]+f)";
        String intPattern = "([0-9]+)";
        String stringPattern = "([a-zA-Z0-9 ]+)";
        if(str.contains("==")){
            String[] result = str.split("==", 2);
            if(Pattern.matches(stringPattern, result[1])){
                str = result[0]  + " == "+'"' +result[1]+ '"';
            }
        }
        if(str.contains("=")&&!str.contains("==")&&!str.contains(">")&&!str.contains("<")){
            String[] result = str.split("=", 2);
            if(Objects.equals(result[1].replace(" ", ""), "false")||Objects.equals(result[1].replace(" ", ""), "true")||Objects.equals(result[1].replace(" ", ""), "False")||Objects.equals(result[1].replace(" ", ""), "True")){
                str = "Boolean "+str;
            }else if(Pattern.matches(decimalPattern, result[1].replace(" ", ""))){
                str = "Double "+str;
            }else if(Pattern.matches(floatPattern, result[1].replace(" ", ""))){
                str = "float "+str;
            }else if(Pattern.matches(intPattern, result[1].replace(" ", ""))){
                str = "int "+str;
            }else if(Pattern.matches(stringPattern, result[1])){
                str = "String "+result[0]  + " = "+'"' +result[1]+ '"';
            }

            stack.push(str.replace("{","(").replace("}",")") + ";");
        }else {
            if( !str.contains(" ")){
                str = "var "+str;
            }
            stack.push(str.replace("{","(").replace("}",")") + ";");
        }
    }
    @Override
    public void exitNum(JavaParser.NumContext ctx) {

        stack.push(ctx.NumArray().toString());
    }
    @Override
    public void exitSpecial_String(JavaParser.Special_StringContext ctx) {

        stack.push(ctx.CharArray().getText().replace("#", "").replace(";", ";\n"));
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `branchRe` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `branchRe` w parserze.
     *
     * @see JavaParser#branchRe()
     */
    @Override
    public void exitBranchRe(JavaParser.BranchReContext ctx) {
        isBranch = true;
        StringBuilder sb = new StringBuilder();
        String s6 = stack.pop();
        String s5 = stack.pop();
        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append("if(").append(s1.replace(";", ""))
                .append(") {\n   ")
                .append(s2)
                .append("\n   ")
                .append(s4)
                .append("\n} else {\n   ")
                .append(s3)
                .append("\n   ")
                .append(s5)
                .append("\n}\n")
                .append(s6);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            sb.append("\n}\n");
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n").append(s.getValue()).append("\n}\n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                sb.append("\n}\n");
                stack.push(sb.toString());
            }
            toFile();
        }

    }

    @Override
    public void enterSeqSeq(JavaParser.SeqSeqContext ctx){
        if (ctx.depth() == 1) {
            StringBuilder firstSB = new StringBuilder();
            firstSB.append("public class ").append(UUID.replace(" ","_")).append("{\n\n public static void main(String[] args) {\n");
            stack.push(firstSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `seqSeq` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `seqSeq` w parserze.
     *
     * @see JavaParser#seqSeq()
     */
    @Override
    public void exitSeqSeq(JavaParser.SeqSeqContext ctx) {
        StringBuilder sb = new StringBuilder();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append(s1).append("\n   ").append(s2).append("\n   ").append(s3);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            sb.append("\n}\n");
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n").append(s.getValue()).append("\n}\n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                sb.append("\n}\n");
                stack.push(sb.toString());
            }
            toFile();
        }

    }

    @Override
    public void enterCond(JavaParser.CondContext ctx){
        if (ctx.depth() == 1) {
            StringBuilder firstSB = new StringBuilder();
            firstSB.append("public class ").append(UUID.replace(" ","_")).append("{\n\n public static void main(String[] args) {\n");
            stack.push(firstSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `cond` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `cond` w parserze.
     *
     * @see JavaParser#cond()
     */
    @Override
    public void exitCond(JavaParser.CondContext ctx) {
        StringBuilder sb = new StringBuilder();

        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append("if(").append(s1.replace(";", "")).append(") {\n   ").append(s2).append("\n} else {\n   ").append(s3).append("\n}\n").append(s4).append("\n");
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            sb.append("\n}\n");
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n").append(s.getValue()).append("\n}\n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                sb.append("\n}\n");
                stack.push(sb.toString());
            }
            toFile();
        }

    }
    @Override
    public void enterChoice(JavaParser.ChoiceContext ctx){
        if (ctx.depth() == 1) {
            StringBuilder firstSB = new StringBuilder();
            firstSB.append("public class ").append(UUID.replace(" ","_")).append("{\n\n public static void main(String[] args) {\n");
            stack.push(firstSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `choice` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `choice` w parserze.
     *
     * @see JavaParser#choice()
     */
    @Override
    public void exitChoice(JavaParser.ChoiceContext ctx) {
        StringBuilder sb = new StringBuilder();

        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append(s1).append("\nif() {\n   ").append(s2).append("\n} else {\n   ").append(s3).append("\n}\n").append(s4);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            sb.append("\n}\n");
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n").append(s.getValue()).append("\n}\n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                sb.append("\n}\n");
                stack.push(sb.toString());
            }
            toFile();
        }
    }
    @Override
    public void enterLoop(JavaParser.LoopContext ctx){
        if (ctx.depth() == 1) {
            StringBuilder firstSB = new StringBuilder();
            firstSB.append("public class ").append(UUID.replace(" ","_")).append("{\n\n public static void main(String[] args) {\n");
            stack.push(firstSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `loop` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `loop` w parserze.
     *
     * @see JavaParser#loop()
     */
    @Override
    public void exitLoop(JavaParser.LoopContext ctx) {
        StringBuilder sb = new StringBuilder();

        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append(s1).append("\n").append("while(").append(s2.replace(";", "")).append(") {\n").append("    ").append(s3).append("\n").append("}\n").append(s4);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            sb.append("\n}\n");
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n").append(s.getValue()).append("\n}\n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                sb.append("\n}\n");
                stack.push(sb.toString());
            }
            toFile();
        }
    }
    @Override
    public void enterRepeat(JavaParser.RepeatContext ctx){
        if (ctx.depth() == 1) {
            StringBuilder firstSB = new StringBuilder();
            firstSB.append("public class ").append(UUID.replace(" ","_")).append("{\n\n public static void main(String[] args) {\n");
            stack.push(firstSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `repeat` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `repeat` w parserze.
     *
     * @see JavaParser#repeat()
     */
    @Override
    public void exitRepeat(JavaParser.RepeatContext ctx) {
        StringBuilder sb = new StringBuilder();

        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append(s1).append("\ndo {\n").append("    ").append(s2).append("\n").append("}while(").append(s3.replace(";", "")).append(")\n").append(s4);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            sb.append("\n}\n");
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n").append(s.getValue()).append("\n}\n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                sb.append("\n}\n");
                stack.push(sb.toString());
            }
            toFile();
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `concurRe` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `concurRe` w parserze.
     *
     * @see JavaParser#concurRe()
     */
    @Override
    public void exitConcurRe(JavaParser.ConcurReContext ctx) {
        isConcur = true;
        StringBuilder sb = new StringBuilder();
        String s6 = stack.pop();
        String s5 = stack.pop();
        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append(s1)
                .append("\nThread thread1 = new Thread(new Runnable() {\n")
                .append("   @Override\n")
                .append("   public void run() {\n   ")
                .append(s2)
                .append("\n   ")
                .append(s4)
                .append("\n   }\n")
                .append("});\n")
                .append("Thread thread2 = new Thread(new Runnable() {\n")
                .append("   @Override\n")
                .append("   public void run() {\n   ")
                .append(s3)
                .append("\n   ")
                .append(s5)
                .append("\n   }\n")
                .append("});\n")
                .append("thread1.start();\nthread2.start();\n")
                .append("thread1.join()\nthread2.join();\n")
                .append(s6);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            sb.append("\n}\n");
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n").append(s.getValue()).append("\n}\n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                sb.append("\n}\n");
                stack.push(sb.toString());
            }
            toFile();
        }
    }
    @Override
    public void enterPara(JavaParser.ParaContext ctx){
        if (ctx.depth() == 1) {
            StringBuilder firstSB = new StringBuilder();
            firstSB.append("public class ").append(UUID.replace(" ","_")).append("{\n\n public static void main(String[] args) {\n");
            stack.push(firstSB.toString());
        }
    }
    /**
     * Metoda wywolywana przy wyjlciu z produkcji `para` w parserze.
     * Tworzy koncowa strukture kodu wynikowego dla klasy Java, zamykajac
     * cialo metody main i dodajac ewentualne zdefiniowane funkcje.
     *
     * @param ctx Kontekst produkcji `para` w parserze.
     *
     * @see JavaParser#para()
     */
    @Override
    public void exitPara(JavaParser.ParaContext ctx) {
        StringBuilder sb = new StringBuilder();

        String s4 = stack.pop();
        String s3 = stack.pop();
        String s2 = stack.pop();
        String s1 = stack.pop();
        sb.append(s1).append("\nThread thread").append(ctx.depth()).append("_1 = new Thread(new Runnable() {\n").append("   @Override\n").append("   public void run() {\n   ").append(s2).append("\n   }\n").append("});\n").append("Thread thread").append(ctx.depth()).append("_2 = new Thread(new Runnable() {\n").append("   @Override\n").append("   public void run() {\n   ").append(s3).append("\n   }\n").append("});\n").append("thread").append(ctx.depth()).append("_1.start();\nthread").append(ctx.depth()).append("_2.start();\n").append("thread").append(ctx.depth()).append("_1.join()\nthread").append(ctx.depth()).append("_2.join();\n").append(s4);
        stack.push(sb.toString());
        sb.setLength(0);
        if (ctx.depth() == 1) {
            sb.append("\n}\n");
            while (functions.size() > 0) {
                if (subProcess.size() > 0) {
                    for (Map.Entry<String, String> s : subProcess.entrySet()) {
                        String[] result = functions.get(0).split("[(]", 2);
                        if (Objects.equals(result[0], s.getKey())) {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n").append(s.getValue()).append("\n}\n");
                            subProcess.remove(s.getKey());
                        } else {
                            sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                        }
                    }
                    functions.remove(0);
                } else {
                    sb.append("\n\nstatic void ").append(functions.get(0)).append(" {\n     // Add code here\n   }\n");
                    functions.remove(0);
                }
            }
            if (functions.size() == 0) {
                sb.append("\n}\n");
                stack.push(sb.toString());
            }
            toFile();
        }
    }

}
