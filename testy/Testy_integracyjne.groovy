import org.example.parsers.JavaLikeParser
import org.example.parsers.PythonLikeParser


import static org.example.functions.GenJava.genJava

import static org.example.functions.GenPython.genPython

class Testy_integracyjne {
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
