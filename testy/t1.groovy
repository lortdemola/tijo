package groovy


import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.example.gen.JavaLexer;
import org.example.gen.JavaParser
import org.example.gen.PythonLexer
import org.example.gen.PythonParser;
import org.example.parsers.JavaLikeParser
import org.example.parsers.PythonLikeParser;
import spock.lang.Specification

import static org.example.functions.GenJava.genJava
import static org.example.functions.GenPython.genPython


class t1 extends Specification {

    def "testy jednostkowe "() {
        expect:
        1 - 1 == 0
    }
    def "1-Jednostkwy.  setUUID should correctly set the UUID JAVA"() {
        given: "An instance of JavaLikeParser and a UUID string"
        def uuid = "JAVA-testUUID"

        when: "setUUID is called"
        JavaLikeParser.SetUUID(uuid)

        then: "The UUID should be correctly set"
        JavaLikeParser.UUID == uuid
    }
    def "2-Jednostkwy. getResult should return the current FinalString JAVA"() {
        given: "An instance of JavaLikeParser and a set FinalString"
        JavaLikeParser.FinalString = "testResult"

        when: "getResult is called"
        def result = JavaLikeParser.getResult()

        then: "It should return the set FinalString"
        result == "testResult"
    }
    def "3-Jednostkwy. toFile should write data to file correctly JAVA"() {
        given: "An instance of JavaLikeParser with sample data and UUID"
        def parser = new JavaLikeParser()
        parser.stack.push("sample data")
        JavaLikeParser.SetUUID("JAVA-test3UUID")

        when: "toFile is called"
        parser.toFile()

        then: "It should write data to a file named 'codeJava_testUUID.txt' JAVA"
        // Assertion to check file creation and content
    }
    def "4-Jednostkwy. exitSeq method is called appropriately when parser.seq() is invoked JAVA"() {
        given: "A JavaLexer, CommonTokenStream, JavaParser, and JavaLikeParser with a specific UUID"
        def input = CharStreams.fromString("Seq(a,b)") // Replace with a string that leads to calling exitSeq
        def lexer = new JavaLexer(input)
        def tokens = new CommonTokenStream(lexer)
        def parser = new JavaParser(tokens)
        def UUID = "JAVA-test4-uuid" // Replace with a specific UUID if necessary
        JavaLikeParser.SetUUID(UUID)
        JavaLikeParser.SetTest(true)
        def listener = new JavaLikeParser()

        when: "Parsing sequence is triggered"
        def tree = parser.seq()
        ParseTreeWalker.DEFAULT.walk(listener, tree)
        def result = listener.stack.contains("a;\nb;\n")
        then: "exitSeq method is triggered with appropriate context"
        result
    }
    def "5-Jednostkwy. exitAlt method is called appropriately when parser.alt() is invoked JAVA"() {
        given: "A JavaLexer, CommonTokenStream, JavaParser, and JavaLikeParser with a specific UUID"
        def input = CharStreams.fromString("Alt(a,b,c)") // Replace with a string that leads to calling exitSeq
        def lexer = new JavaLexer(input)
        def tokens = new CommonTokenStream(lexer)
        def parser = new JavaParser(tokens)
        def UUID = "JAVA-test5-uuid" // Replace with a specific UUID if necessary
        JavaLikeParser.SetUUID(UUID)
        JavaLikeParser.SetTest(true)
        def listener = new JavaLikeParser()

        when: "Parsing sequence is triggered"
        def tree = parser.alt()
        ParseTreeWalker.DEFAULT.walk(listener, tree)
        def result = listener.stack.contains("if(a) {\nb;\n}c;\n")
        then: "exitSeq method is triggered with appropriate context"
        result
    }
    def "6-Jednostkwy. exitSeq method is called appropriately when parser.seq() is invoked Python"() {
        given: "A PythonLexer, CommonTokenStream, PythonParser, and PythonLikeParser with a specific UUID"
        def input = CharStreams.fromString("Seq(a,b)") // Replace with a string that leads to calling exitSeq
        def lexer = new PythonLexer(input)
        def tokens = new CommonTokenStream(lexer)
        def parser = new PythonParser(tokens)
        def UUID = "Python-test6-uuid" // Replace with a specific UUID if necessary
        PythonLikeParser.SetUUID(UUID)
        PythonLikeParser.SetTest(true)
        def listener = new PythonLikeParser()

        when: "Parsing sequence is triggered"
        def tree = parser.seq()
        ParseTreeWalker.DEFAULT.walk(listener, tree)
        def result = listener.stack.contains("a\nb\n")
        then: "exitSeq method is triggered with appropriate context"
        result
    }
    def "7-Jednostkwy. exitLoop method is called appropriately when parser.loop() is invoked Python"() {
        given: "A PythonLexer, CommonTokenStream, PythonParser, and PythonLikeParser with a specific UUID"
        def input = CharStreams.fromString("Loop(a,b,c,d)") // Replace with a string that leads to calling exitSeq
        def lexer = new PythonLexer(input)
        def tokens = new CommonTokenStream(lexer)
        def parser = new PythonParser(tokens)
        def UUID = "Python-test7-uuid" // Replace with a specific UUID if necessary
        PythonLikeParser.SetUUID(UUID)
        PythonLikeParser.SetTest(true)
        def listener = new PythonLikeParser()

        when: "Parsing sequence is triggered"
        def tree = parser.loop()
        ParseTreeWalker.DEFAULT.walk(listener, tree)
        def result = listener.stack.contains("a\nwhile b:\n    c\nd")
        then: "exitSeq method is triggered with appropriate context"
        result
    }
    def "8-Jednostkwy. exitPara method is called appropriately when parser.para() is invoked Python"() {
        given: "A PythonLexer, CommonTokenStream, PythonParser, and PythonLikeParser with a specific UUID"
        def input = CharStreams.fromString("Para(a,b,c,d)") // Replace with a string that leads to calling exitSeq
        def lexer = new PythonLexer(input)
        def tokens = new CommonTokenStream(lexer)
        def parser = new PythonParser(tokens)
        def UUID = "Python-test8-uuid" // Replace with a specific UUID if necessary
        PythonLikeParser.SetUUID(UUID)
        PythonLikeParser.SetTest(true)
        def listener = new PythonLikeParser()

        when: "Parsing sequence is triggered"
        def tree = parser.para()
        ParseTreeWalker.DEFAULT.walk(listener, tree)
        def result = listener.stack.contains("a\nthread1 = threading.Thread(target=Thread0)\nthread2 = threading.Thread(target=Thread1)\nthread1.start()\nthread2.start()\nthread1.join()\nthread2.join()\nd")
        then: "exitSeq method is triggered with appropriate context"
        result
    }
    def "9-Jednostkwy. exitSeqSeq method is called appropriately when parser.seqSeq() is invoked Python"() {
        given: "A PythonLexer, CommonTokenStream, PythonParser, and PythonLikeParser with a specific UUID"
        def input = CharStreams.fromString("SeqSeq(a,b,c)") // Replace with a string that leads to calling exitSeq
        def lexer = new PythonLexer(input)
        def tokens = new CommonTokenStream(lexer)
        def parser = new PythonParser(tokens)
        def UUID = "Python-test9-uuid" // Replace with a specific UUID if necessary
        PythonLikeParser.SetUUID(UUID)
        PythonLikeParser.SetTest(true)
        def listener = new PythonLikeParser()

        when: "Parsing sequence is triggered"
        def tree = parser.seqSeq()
        ParseTreeWalker.DEFAULT.walk(listener, tree)
        def result = listener.stack.contains("a\nb\nc")
        then: "exitSeq method is triggered with appropriate context"
        result
    }
    def "10-Jednostkwy. exitChoice method is called appropriately when parser.choice() is invoked Python"() {
        given: "A PythonLexer, CommonTokenStream, PythonParser, and PythonLikeParser with a specific UUID"
        def input = CharStreams.fromString("Choice(a,b,c,d)") // Replace with a string that leads to calling exitSeq
        def lexer = new PythonLexer(input)
        def tokens = new CommonTokenStream(lexer)
        def parser = new PythonParser(tokens)
        def UUID = "Python-test10-uuid" // Replace with a specific UUID if necessary
        PythonLikeParser.SetUUID(UUID)
        PythonLikeParser.SetTest(true)
        def listener = new PythonLikeParser()

        when: "Parsing sequence is triggered"
        def tree = parser.choice()
        ParseTreeWalker.DEFAULT.walk(listener, tree)
        def result = listener.stack.contains("a\nif :\n   b\nelse:\n   c\nd")
        then: "exitSeq method is triggered with appropriate context"
        result
    }
    def "testy integracyjne"() {
        expect:
        1 - 1 == 0
    }
    def "1-integracyjny. Seq(a,b()) input gives correct result Java"() {
        given: "An instance of input and a specific UUID"
        def UUID = "JAVA-test1-uuid"
        def input = "Seq(a,b())"
        when: "getResult is called"
        genJava(input, UUID);
        def result = JavaLikeParser.getResult()
        then: "It should return the set FinalString"
        result == "a;\nb();\n\n\npublic void b() {\n     // Add code here\n   }\n"
    }
    def "2-integracyjny. Seq(Delay(10),b()) input gives correct result Java"() {
        given: "An instance of input and a specific UUID"
        def UUID = "JAVA-test1-uuid"
        def input = "Seq(Wait(10),b())"
        when: "getResult is called"
        genJava(input, UUID);
        def result = JavaLikeParser.getResult()
        then: "It should return the set FinalString"
        result == "Thread.sleep(10);\nb();\n\n\npublic void b() {\n     // Add code here\n   }\n"
    }
    def "3-integracyjny. Seq(Branch(a1,b1,c1),BranchRe(a2,b2,c2)) input gives correct result Java"() {
        given: "An instance of input and a specific UUID"
        def UUID = "JAVA-test1-uuid"
        def input = "Seq(Branch(a1,b1,c1),BranchRe(a2,b2,c2))"
        when: "getResult is called"
        genJava(input, UUID);
        def result = JavaLikeParser.getResult()
        then: "It should return the set FinalString"
        result == "if(a1) {\n   b1;\n   a2;\n} else {\n   c1;\n   b2;\n}\nc2;\n"
    }
    def "4-integracyjny. Seq(a,Cond(b,Seq(c,d),Para(e,f,Seq(g,h),i),j)) input gives correct result Java"() {
        given: "An instance of input and a specific UUID"
        def UUID = "JAVA-test1-uuid"
        def input = "Seq(a,Cond(b,Seq(c,d),Para(e,f,Seq(g,h),i),j))"
        when: "getResult is called"
        genJava(input, UUID);
        def result = JavaLikeParser.getResult()
        then: "It should return the set FinalString"
        result == "a;\nif(b) {\n   c;\nd;\n\n} else {\n   e;\nThread thread13_1 = new Thread(new Runnable() {\n   @Override\n   public void run() {\n   f;\n   }\n});\nThread thread13_2 = new Thread(new Runnable() {\n   @Override\n   public void run() {\n   g;\nh;\n\n   }\n});\nthread13_1.start();\nthread13_2.start();\nthread13_1.join()\nthread13_2.join();\ni;\n}\nj;\n\n"
    }
    def "5-integracyjny. Seq(int i = 0,Cond(i==10,Seq(print(i),i+=50),Seq(i = 10 % 2,i+1),return i)) input gives correct result Java"() {
        given: "An instance of input and a specific UUID"
        def UUID = "JAVA-test1-uuid"
        def input = "Seq(int i = 0,Cond(i==10,Seq(print(i),i+=50),Seq(i = 10 % 2,i+1),return i))"
        when: "getResult is called"
        genJava(input, UUID);
        def result = JavaLikeParser.getResult()
        then: "It should return the set FinalString"
        result == "int i = 0;\nif(i==10) {\n   print(i);\ni+=50;\n\n} else {\n   i = 10 % 2;\ni+1;\n\n}\nreturn i;\n\n\n\npublic void print(i) {\n     // Add code here\n   }\n"
    }
    def "6-integracyjny. SeqSeq(eat(amount),sleep(hours),repeat()) input gives correct result Python"() {
        given: "An instance of input and a specific UUID"
        def UUID = "Python-test1-uuid"
        def input = "SeqSeq(eat(amount),sleep(hours),repeat())"
        when: "getResult is called"
        genPython(input, UUID)
        def result = PythonLikeParser.getResult()
        then: "It should return the set FinalString"
        result == "eat(amount)\nsleep(hours)\nrepeat()\n\ndef eat(amount):\n     funcbody\n      // Add code here\n   \noutof_func \n\n\ndef sleep(hours):\n     funcbody\n      // Add code here\n   \noutof_func \n\n\ndef repeat():\n     funcbody\n      // Add code here\n   \noutof_func \n"
    }
    def "7-integracyjny. Seq(a,Sub(task1,Seq(a,b))) input gives correct result Python"() {
        given: "An instance of input and a specific UUID"
        def UUID = "JAVA-test1-uuid"
        def input = "Seq(a,Sub(task1,Seq(a,b)))"
        when: "getResult is called"
        genPython(input, UUID);
        def result = PythonLikeParser.getResult()
        then: "It should return the set FinalString"
        result == "a\ntask1()\ndef task1():\n     funcbody\n      a\nb\n\n\noutof_func \n"
    }
    def "8-integracyjny. Seq(Concur(a1,b1,c1),ConcurRe(a2,b2,c2)) input gives correct result Python"() {
        given: "An instance of input and a specific UUID"
        def UUID = "JAVA-test1-uuid"
        def input = "Seq(Concur(a1,b1,c1),ConcurRe(a2,b2,c2))"
        when: "getResult is called"
        genPython(input, UUID);
        def result = PythonLikeParser.getResult()
        then: "It should return the set FinalString"
        result == "a1\nthread1 = threading.Thread(target=Thread0)\nthread2 = threading.Thread(target=Thread1)\nthread1.start()\nthread2.start()\nthread1.join()\nthread2.join()\nc2\n\ndef Thread0():\n    b1\n    a2\n\ndef Thread1():\n    c1\n    b2\n"
    }
    def "9-integracyjny. Seq(a,#seq of seq+seq = SeqSeq#) input gives correct result Python"() {
        given: "An instance of input and a specific UUID"
        def UUID = "JAVA-test1-uuid"
        def input = "Seq(a,#seq of seq+seq = SeqSeq#)"
        when: "getResult is called"
        genPython(input, UUID);
        def result = PythonLikeParser.getResult()
        then: "It should return the set FinalString"
        result == "a\nseq of seq+seq = SeqSeq\n"
    }
    def "10-integracyjny. Seq(Cond(a,b,Loop(b,x,e,f),Choice(c,d,e,f)),Alt(Seq(Branch(a1,b1,c1),BranchRe(a2,b2,c2)),a,v)) input gives correct result Python"() {
        given: "An instance of input and a specific UUID"
        def UUID = "JAVA-test1-uuid"
        def input = "Seq(Cond(a,b,Loop(b,x,e,f),Choice(c,d,e,f)),Alt(Seq(Branch(a1,b1,c1),BranchRe(a2,b2,c2)),a,v))"
        when: "getResult is called"
        genPython(input, UUID);
        def result = PythonLikeParser.getResult()
        then: "It should return the set FinalString"
        result == "if a:\n   b\nelse:\n   b\nwhile x:\n    e\nf\nc\nif :\n   d\nelse:\n   e\nf\nif if a1:\n   b1\n   a2\nelse:\n   c1\n   b2\n\nc2\n: a\nv\n\n"
    }
}