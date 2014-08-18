Sudoku to popularna na całym świecie gra logiczna. Jej celem jest wypełnienie diagramu n^2 na n^2 (najczęściej 9x9) w taki sposób, aby w każdym wierszu, w każdej kolumnie i w każdym z 'n' pogrubionych kwadratów o wymiarach 'n' na 'n' (najczęściej 3x3), znalazło się po jednej cyfrze od 1 do n (najczęściej od 1 do 9) [1]. Na całym świecie rozgrywane są mistrzostwa w rozwiązywaniu tej łamigłówki. Polacy odnoszą w tej dziedzinie duże sukcesy. Jan Mrozowski jest trzykrotnym mistrzem świata w tej dyscyplinie (lata 2009, 2010, 2012). Ze względu na matematyczny charakter łamigłówki, może być ona z powodzeniem rozwiązywana przez komputery. Niniejszy README przedstawia algorytm przeszukiwania grafów Best First Search jako jedno z narzędzi do rozwiązania Sudoku.

Liczba wszystkich możliwych rozwiązań planszy 9x9 Sudoku wynosi 6 670 903 752 021 072 936 960 [2]. W przypadku, gdy część pól w stanie początkowym jest wypełniona, pozostała liczba możliwych iteracji prowadzących do całkowitego rozwiązania planszy jest na tyle duża, by wykluczyć możliwość użycia techniki brute-force. W tego typu sytuacjach z pomocą przychodzą algorytmy heurystyczne. Heurystyka jest to dziedzina algorytmiki polegająca na rozwiązywaniu problemów, które nie mają gwarancji znalezienia rozwiązania. Heurystyczny algorytm, który został użyty w prezentowanym w tym artykule programie nosi nazwę Best First Search (BFS).

BFS jest w zasadzie modyfikacją zachłannego algorytmu Breadth First Search (algorytmu zachłannego - czyli takiego który wykonuje działanie, które w danej chwili wydaje się najkorzystniejsze). BFS występuje w literaturze często obok takich algorytmów jak A* czy Dijkstry.

Algorytm :

open = [ stan początkowy ]
closed = []
while (not empty open):
  n = open.poll()
  if (isGoal(n)) return path_to_n
  foreach (n.children):
   if (child in closed) continue;
   else if (child not in open) open.add(child);
   else{
    var child_heuristic;
    var child_heuristic_from_open
    if (child_heuristic better than child_heuristic_from_open) change child_from_open with child;
   }
  closed.add(n)

1   Dodaj początkowy stan do kolejki priorytetowej "open". W kolejce tej, największy priorytet mają stany o najkorzystniejszej heurystyce.
2   Zainicjuj zbiór "closed" jako pusty. W zbiorze tym znajdują się stany, które zostały sprawdzone, a nie są rozwiązaniem.
3   Dopóki zbiór open pozostaje niepusty powtarzaj:
4   Pobierz kolejny (najlepszy pod względem heurystyki) stan ze zbioru open.
5   Jeżeli pobrany stan jest rozwiązaniem, zwróć ścieżkę prowadzącą do tego stanu.
6   Wygeneruj stany potomne aktualnego stanu i dla każdego z nich wyknuj:
7   Jeżeli stan dziecko występuje w zbiorze closed idź do kolejnego wywołania pętli.
8   Jeżeli stan dziecko nie występuje w kolejce open, dodaj go do tej kolejki.
9   W przeciwnym wypadku (dany stan już jest w zbiorze open):
10  Policz heurystykę dziecka
11  Pobierz heurystykę dziecka, z kolejki open
12  Jeżeli nowo obliczona wartość heurystyki jest korzystniejsza, uaktualnij pozycję dziecka w zbiorze open
13  Włóż stan do zbioru closed

W przypadku załączonego programu posłużyliśmy się uproszczoną wersją tego algorytmu. Spowodowane to zostało tym, że dwa identyczne stany Sudoku nie mogą mieć różnej heurystyki, a zatem nie ma potrzeby podmieniania stanów w kolejce open.


List children = currentState.getChildren();
for (SudokuState state : children) {
 if (closed.contains(state.getHashCode())) {
  continue;
 } else if (!open.contains(state)) {
  open.add(state);
 }


Plansza sudoku w zamieszczonym programie została zaimplementowana jako dwuwymiarowa tablica (macierz). Każdemu elementowi tej tablicy odpowiada pewna wartość. Mając określony stan początkowy planszy 9x9 (n wierszy na n kolumn), podzielonej na n=9 bloków o wymiarach 3x3 \sqrt n x \sqrt n, należy wypełnić planszę w taki sposób, aby w każdej kolumnie, wierszu i podkwadracie były wykorzystane wszystkie liczby z przedziału 1...n. Poprzez "nowy stan k + 1" rozumiemy stan k uzupełniony dodatkowo o jedną cyfrę w dowolnym pasującym miejscu.

Każdy stan Sudoku, jak i każdy element stanu (wyraz w macierzy), posiada swoją heurystykę. Heurystyka pojedynczego elementu h(x,y) wyrażona jest jako wszystkie <em>pozostałe możliwe wartości</em> elementu o współrzędnych x i y. Przykładowo jeżeli w dany wyraz macierzy możemy wstawić dwie różne cyfry, to heurystyka tej komórki wynosi 2. Heurystykę stanu Sudoku obliczamy na podstawie wzoru :

H(stan) = \sum\limits_{x=1}^n \sum\limits_{y=1}^n h(x,y)

Heurystyka algorytmu opiera się na założeniu, że najpierw powinniśmy sprawdzać stany, które mają najmniej możliwych wartości. Spośród stanów znajdujących się w kolejce priorytetowej 'open' wybieramy w pierwszej kolejności te, które mają najmniejszą heurystykę stanu. Heurystyka elementu przydaje się do konstrukcji dzieci aktualnego stanu (nowych stanów), albowiem element, który ma najmniejszą heurystykę służy do utworzenia nowych stanów. Innymi słowy, nowe stany różnią się od stanu rodzica wartością dodaną do wyrazu macierzy o najmniejszej heurystyce.

[1] <a href="http://en.wikipedia.org/wiki/Mathematics_of_Sudoku">http://en.wikipedia.org/wiki/Mathematics_of_Sudoku</a>
[2] <a title="http://www.afjarvis.staff.shef.ac.uk/sudoku/" href="http://www.afjarvis.staff.shef.ac.uk/sudoku/">http://www.afjarvis.staff.shef.ac.uk/sudoku/</a>