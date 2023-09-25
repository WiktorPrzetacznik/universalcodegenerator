# Projekt i obsługa
„Universal Code Generator” to program służący do konwersji kodu źródłowego w wybranych językach do formatu XML oraz konwersji kodu XML do w pełni działającego kodu źródłowego. Dodatkowo, istnieje możliwość analizy kodu źródłowego w wybranych językach pod względem występowania poszczególnych elementów składowych kodu (zwanych także tokenami). Program ten został stworzony z myślą o ułatwieniu pracy programistom oraz zapewnieniu wygodnego sposobu zarządzania kodem źródłowym. 
Program posiada trzy funkcje: konwersja plików źródłowych do formatu XML, konwersja plików XML do kodu źródłowego oraz analiza kodu źródłowego pod względem występowania poszczególnych elementów składowych kodu. Poniższa tabela opisuje każdą z tych funkcji.

| Funkcja                                                                               | Opis                                                                                                                                                                       | Dane wejściowe                                                              | Dane wyjściowe                                                                    |
|---------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------|-----------------------------------------------------------------------------------|
| Konwersja kodu źródłowego do uniwersalnego formatu                                    | Przeprowadza konwersję listy plików kodu źródłowego do uniwersalnego zapisu XML                                                                                            | Zbiór kodu źródłowego (lista kodu źródłowego odczytana z plików źródłowych) | Zbiór przetworzonego kodu w formacie XML (lista gotowa do zapisu do plików)       |
| Konwersja danych zapisanych w formacie uniwersalnym do kodu źródłowego                | Przeprowadza konwersję listy plików uniwersalnego zapisu XML do plików kodu źródłowego                                                                                     | Zbiór przetworzonego kodu w formacie XML (lista)                            | Zbiór kodu źródłowego (lista kodu źródłowego gotowego do zapisu do plików)        |
| Analiza kodu źródłowego pod względem występowania poszczególnych elementów składowych | Przeprowadza analizę wybranego zbioru kodu źródłowego pod względem występowania poszczególnych elementów składowych, takich jak na przykład instrukcje warunkowe lub pętle | Zbiór kodu źródłowego (lista kodu źródłowego odczytana z plików źródłowych) | Lista elementów składowych kodu źródłowego wraz liczbą wystąpień każdego elementu |

Program w wersji 1.0 oferuje wsparcie dla następujących języków programowania:
-	C#,
- Java,
-	JavaScript.

Obsługa programu sprowadza się do korzystania z interfejsu tekstowego. Interfejs tekstowy aplikacji to sposób prezentacji i interakcji użytkownika z programem za pomocą tekstu. Jest to tradycyjna forma interfejsu użytkownika, która nie korzysta z grafiki ani innych elementów wizualnych. Zamiast tego, użytkownik komunikuje się z aplikacją, wprowadzając polecenia i otrzymując odpowiedzi w formie tekstu. Interfejs tekstowy jest zazwyczaj prezentowany w konsoli lub terminalu, gdzie użytkownik może wprowadzać polecenia za pomocą klawiatury. Aplikacje z interfejsem tekstowym często wykorzystują komunikację opartą na linii poleceń (command-line interface, CLI), gdzie użytkownik wpisuje konkretne komendy lub polecenia, a aplikacja reaguje na nie odpowiednimi informacjami lub wykonuje określone działania. Przykład interakcji z programem jest przedstawiony na poniższym rysunku.

![image](https://github.com/WiktorPrzetacznik/universalcodegenerator/blob/main/image.gif)

Argumenty obsługiwane przez program są zapisywane w formacie „nazwa=wartość”. Aktualnie wspierane argumenty, które możemy przekazać aplikacji to:

-	mode – tryb pracy programu, możliwe wartości:
    -	src_xml – konwersja plików źródłowych do formatu XML,
    - xml_src – konwersja plików XML do kodu źródłowego,
-	classify – analiza kodu źródłowego pod względem występowania poszczególnych elementów kodu;
-	lang – język programowania powiązany z plikami źródłowymi, możliwe wartości:
    -	cs – C#,
    -	j – Java,
    -	js – JavaScript;
- dir – folder, który zawiera pliki źródłowe (włączając podfoldery), przykładowo „C:\Users\user\project”.


Po wykonaniu określonego zadania, rezultat zostanie wyświetlony użytkownikowi.
