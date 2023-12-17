## Nazwa kursu
 Testowanie i Jakość Oprogramowania (Projekt)  
## Autor
| Index | Student       |
|-------|---------------|
| 34280 | Kacper Dworak |
## Temat projektu
System generowania kodu źródłowego w języku Java/Python na podstawie predefiniowanych sieci działań
## Opis projektu
Testy generowania kodu źrudłowego
## Uruchomienie projektu
## Uruchomienie testów jednostkowych i integracyjnych
uruchomienie compilacji Testy 1 -jednostkowych<br>uruchomienie compilacji Testy 2 -integracyjnych
## Dokumentacja API
[a link](https://github.com/user/repo/blob/branch/other_file.md)
<a href="https://github.com/lortdemola/tijo/blob/main/doc/index.html#CollectionsFramework">
Javadoc aplikacji</a>.
## Scenariusze testowe dla testera manualnego
wprowadzanie testów skopiować i wklejić pojedynczo do pliku "INPUT.CC"
poczym odpalić Main.java

| Test Case ID | Opis | Kroki testowe | Oczekiwany wynik |
|---|---|---|---|
| TM_1 | Simple Sequence | 1. Wprowadz **Seq(x,y)** do pola input text <br>2. kliknij generate | Wynik końcowy:<br>true<br> |
| TM_2 | Nested Sequences incorrect | 1. Wprowadz **Seq(,Seq(c,d))** do pola input text<br>2. kliknij generate | Wynik końcowy:<br>false |
| TM_3 | Conditional with Sequences incorrect | 1. Wprowadz **Cond(p,Seq(q,r),Seq(t))** do pola input text<br>2. kliknij generate | Wynik końcowy:<br>false |
| TM_4 | Parallel Structure | 1. Wprowadz **Para(Para(a,b,c,d),e,f,g)** do pola input text<br>2. kliknij generate | Wynik końcowy:<br>true |
| TM_5 | Loop with Nested Sequence | 1. Wprowadz **Loop(a,Seq(b,c),d,e)** do pola input text<br>2. kliknij generate | Wynik końcowy:<br>true |
| TM_6 | Choice with Alternatives | 1. Wprowadz **Choice(a,Choice(a,b,c,d),e,f)** do pola input text<br>2. kliknij generate | Wynik końcowy:<br>true |
| TM_7 | Sequence of Conditions | 1. Wprowadz **Seq(Cond(a,b,c,x),Cond(d,e,f,s))** do pola input text<br>2. kliknij generate | Wynik końcowy:<br>true |
| TM_8 | Alternative with Sequence and Loop | 1. Wprowadz **Alt(Seq(a,b),Loop(c,d,e,f),g)** do pola input text<br>2. kliknij generate | Wynik końcowy:<br>true |
| TM_9 | Repeat with Nested Parallelism and Conditions | 1. Wprowadz **Repeat(Para(a,b,Cond(c,d,e,j),f),Seq(g,h),i,j)** do pola input text<br>2. kliknij generate | Wynik końcowy:<br>true |
| TM_10 | Complex Nested Structure | 1. Wprowadz **Seq(a,Cond(b,Para(c,d,e,f),Loop(g,h,i,j),c))** do pola input text<br>2. kliknij generate | Wynik końcowy:<br>true |

## Technologie użyte w projekcie
- Java 17
- antlr4
- groovy 3
- JavaDoc
- Fxml
- Maven

&copy; Akademia Tarnowska | Kacper Dworak
