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
uruchomienie compilacji testy 
## Dokumentacja API
## Scenariusze testowe dla testera manualnego
wprowadzanie testów skopiować i wklejić pojedynczo do pliku "INPUT.CC"
poczym odpalić Main.java

**1.Simple Sequence:**
Seq(x,y)

**2.Nested Sequences:**
Seq(Seq(a,b),Seq(c,d))

**3.Conditional with Sequences:**
Cond(p,Seq(q,r),Seq(s,t))

**4.Parallel Structure:**
Para(Para(a,b,c,d),e,f,g)

**5.Loop with Nested Sequence:**
Loop(a,Seq(b,c),d,e)

**6.Choice with Alternatives:**
Choice(a,Choice(a,b,c,d),e,f)

**7.Sequence of Conditions:**
Seq(Cond(a,b,c,x),Cond(d,e,f,s))

**8.Complex Nested Structure:**
Seq(a,Cond(b,Para(c,d,e,f),Loop(g,h,i,j),c))

**9.Alternative with Sequence and Loop:**
Alt(Seq(a,b),Loop(c,d,e,f),g)

**10.Repeat with Nested Parallelism and Conditions:**
Repeat(Para(a,b,Cond(c,d,e,j),f),Seq(g,h),i,j)
## Technologie użyte w projekcie

&copy; Akademia Tarnowska | Kacper Dworak
