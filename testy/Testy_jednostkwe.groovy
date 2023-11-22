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


class Testy_jednostkwe extends Specification {


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
}