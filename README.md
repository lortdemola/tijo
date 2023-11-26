| ![Logo Uniwersytetu](./img/at_logo_thumb.png) | <h2>WydziaÅ‚ Politechniczny</h2><h5>Katedra Informatyki</h5>                            |
| --------------------------------------------- | -------------------------------------------------------------------------------------- |
| **ProwadzÄ…cy**                                | Tomasz GÄ…dek                                                                           |
| **Kurs**                                      | Testowanie i JakoÅ›Ä‡ Oprogramowania (Projekt)                                           |
| **Temat**                                     | Implementacja dowolnego projektu zawierajÄ…cego testy                                   |

Data ostatniej modyfikacji: **07-10-2023**

## WybÃ³r projektu

Zanim przystÄ…piÄ… PaÅ„stwo do tworzenia pracy inÅ¼ynierskiej, naleÅ¼y udostÄ™pniÄ‡ mi plik (startosta), ktÃ³ry bÄ™dzie zawieraÅ‚ informacje 
o autorze i temacie projektu.

**PrzykÅ‚adowy format**:

| Index  | Student     | Temat  | Opis                                                                                                                     | Grupa |
|-------|--------------|--------|--------------------------------------------------------------------------------------------------------------------------|-------|
| 34280 | Kacper Dworak | System generowania kodu źródłowego w języku Java/Python na podstawie predefiniowanych sieci działań | Testy logiki ruchu goÅ„cem | PO1   |

Tematyka projektu jest dowolna. MoÅ¼e to byÄ‡ wycinek funkcjonalnoÅ›ci z PaÅ„stwa pracy inÅ¼ynierskiej. Nie moÅ¼e to byÄ‡ caÅ‚a praca dyplomowa. MoÅ¼e to
byÄ‡ fragment funkcjonalnoÅ›ci, ktÃ³ry powinien byÄ‡ solidnie przetestowany. **Bardzo proszÄ™ nie wybieraÄ‡ projektÃ³w, ktÃ³re realizowaliÅ›my na laboratorium z kursu TiJO!**

## Projekt

**Lokalizacja**

ProszÄ™ korzystaÄ‡ z systemu kontroli wersji Git. Pliki ÅºrÃ³dÅ‚owe projektu umieszczamy na platformie **Bitbucket** / **GitHub** / **GitLab**.

**Struktura**

- Dokumentacja projektu powinna znaleÅºÄ‡ siÄ™ w pliku **README.md** (**Wymagane nagÅ‚Ã³wki**: *Nazwa kursu*, *Autor*, *Temat projektu*, *Opis projektu*, *Uruchomienie projektu*, *Uruchomienie testÃ³w jednostkowych i integracyjnych*, *Dokumentacja API*, *Scenariusze testowe dla testera manualnego*, *Technologie uÅ¼yte w projekcie*),
- Projekt powinien zawieraÄ‡ interfejs uÅ¼ytkownika,
- Projekt powinien zawieraÄ‡ minimum 10 testÃ³w jednostkowych,
- Projekt powinien zawieraÄ‡ minimum 10 testÃ³w integracyjnych,
- Projekt powinien zawieraÄ‡ minimum 10 scenariuszy dla testera manualnego,
- **Kod testÃ³w powinien byÄ‡ ustandaryzowany wedÅ‚ug konwencji AAA (Arrange-Act-Assert) lub GWT (Given-When-Then)**.

**JakoÅ›Ä‡**

Podczas implementacji nie zapominamy o jakoÅ›ci oprogramowania. Staramy siÄ™:

- UsuwaÄ‡ nieuÅ¼ywane importy,
- StosowaÄ‡ wzorce projektowe (jeÅ›li zauwaÅ¼ymy takÄ… potrzebÄ™),
- StosowaÄ‡ zasadÄ™ SOLID (jeÅ›li to moÅ¼liwe),
- UnikaÄ‡ nazw zmiennych, klas, funkcji w jÄ™zyku polskim,
- UnikaÄ‡ nazw zmiennych, klas, funkcji, ktÃ³re nie niosÄ… znaczenia,
- UnikaÄ‡ ogromnych klas i funkcji,
- TworzyÄ‡ czysty kod, ktÃ³ry bÄ™dzie Å‚atwy do testowania.

**Obrona**

Ostateczna wersja aplikacji powinna zostaÄ‡ umieszczona w zdalnym repozytorium **git** na branchu **master**.

## Harmonogram prac

**Bardzo proszÄ™ dotrzymywaÄ‡ terminÃ³w!**

**Niedotrzymanie 1 terminu powoduje obniÅ¼enie oceny koÅ„cowej projektu o 1 stopieÅ„!**

| Termin     | Zakres prac                                                                                                   |
| ---------- | ------------------------------------------------------------------------------------------------------------- |
| 16-10-2023 | Etap 0: Starosta / Przedstawiciel grup przesyÅ‚a prowadzÄ…cemu listÄ™ proponowanych tematÃ³w (zgodnie z formatem) |
| 20-11-2023 | Etap 1: Testy (jednostkowe, integracyjne, scenariusze testowe dla testera manualnego).                        |
| 18-12-2023 | Etap 2: Projekt (Frontent / Interfejs uÅ¼ytkownika + Backend / Logika).                                        |
| 15-01-2024 | Etap 3: Dokumentacja projektu + obrona.                                                                       |

**Etap 1 i 2 - kolejnoÅ›Ä‡ dowolna.**

## Kontakt z prowadzÄ…cym

- Email <t_gadek@atar.edu.pl>,
- MS Teams,
- Konsultacje (zgodnie z harmonogramem zajÄ™Ä‡).

## PrzykÅ‚adowa dokumentacja projektu "chess"

Dokumentacja projektu w formie pliku graficznego dostÄ™pna [tutaj](./chess-documentation/chess-documentation.png).

## Pomoc

- [ChatGPT](https://chat.openai.com/),
- [JUnit Framework User Guide](https://junit.org/junit5/docs/current/user-guide/),
- [Spock Framework Reference](https://spockframework.org/spock/docs/2.3/index.html),
- [CUnit Documentation](https://cunit.sourceforge.net/documentation.html),
- [Test REST API](https://www.baeldung.com/integration-testing-a-rest-api)

***

&copy; Akademia Tarnowska | Tomasz GÄ…dek
