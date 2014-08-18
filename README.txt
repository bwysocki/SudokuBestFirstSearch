Sudoku to popularna na całym świecie gra logiczna. Jej celem jest wypełnienie diagramu [math]n^2[/math] na [math]n^2[/math] (najczęściej 9x9) w taki sposób, aby w każdym wierszu, w każdej kolumnie i w każdym z 'n' pogrubionych kwadratów o wymiarach 'n' na 'n' (najczęściej 3x3), znalazło się po jednej cyfrze od 1 do n (najczęściej od 1 do 9) [1]. Na całym świecie rozgrywane są mistrzostwa w rozwiązywaniu tej łamigłówki. Polacy odnoszą w tej dziedzinie duże sukcesy. Jan Mrozowski jest trzykrotnym mistrzem świata w tej dyscyplinie (lata 2009, 2010, 2012). Ze względu na matematyczny charakter łamigłówki, może być ona z powodzeniem rozwiązywana przez komputery. Niniejszy artykuł przedstawia algorytm przeszukiwania grafów Best First Search jako jedno z narzędzi do rozwiązania Sudoku.

<img class="alignnone size-medium wp-image-195" style="float: left; margin-right: 20px;" alt="sudoku" src="http://kohonen.pl/wp-content/uploads/2013/01/sudoku-300x300.jpg" width="200" height="200" />

Liczba wszystkich możliwych rozwiązań planszy 9x9 Sudoku wynosi 6 670 903 752 021 072 936 960 [2]. W przypadku, gdy część pól w stanie początkowym jest wypełniona, pozostała liczba możliwych iteracji prowadzących do całkowitego rozwiązania planszy jest na tyle duża, by wykluczyć możliwość użycia techniki brute-force. W tego typu sytuacjach z pomocą przychodzą algorytmy heurystyczne. Heurystyka jest to dziedzina algorytmiki polegająca na rozwiązywaniu problemów, które nie mają gwarancji znalezienia rozwiązania. Heurystyczny algorytm, który został użyty w prezentowanym w tym artykule programie nosi nazwę Best First Search (BFS).

BFS jest w zasadzie modyfikacją zachłannego algorytmu Breadth First Search (algorytmu zachłannego - czyli takiego który wykonuje działanie, które w danej chwili wydaje się najkorzystniejsze). BFS występuje w literaturze często obok takich algorytmów jak A* czy Dijkstry.

<strong>Algorytm : </strong>

[text]
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
[/text]
<ol>
    <li>Dodaj początkowy stan do kolejki priorytetowej "open". W kolejce tej, największy priorytet mają stany o najkorzystniejszej heurystyce.</li>
    <li>Zainicjuj zbiór "closed" jako pusty. W zbiorze tym znajdują się stany, które zostały sprawdzone, a nie są rozwiązaniem.</li>
    <li>Dopóki zbiór open pozostaje niepusty powtarzaj:</li>
    <li>Pobierz kolejny (najlepszy pod względem heurystyki) stan ze zbioru open.</li>
    <li>Jeżeli pobrany stan jest rozwiązaniem, zwróć ścieżkę prowadzącą do tego stanu.</li>
    <li>Wygeneruj stany potomne aktualnego stanu i dla każdego z nich wyknuj:</li>
    <li>Jeżeli stan dziecko występuje w zbiorze closed idź do kolejnego wywołania pętli.</li>
    <li>Jeżeli stan dziecko nie występuje w kolejce open, dodaj go do tej kolejki.</li>
    <li>W przeciwnym wypadku (dany stan już jest w zbiorze open):</li>
    <li>Policz heurystykę dziecka</li>
    <li>Pobierz heurystykę dziecka, z kolejki open</li>
    <li>Jeżeli nowo obliczona wartość heurystyki jest korzystniejsza, uaktualnij pozycję dziecka w zbiorze open</li>
    <li>Włóż stan do zbioru closed</li>
</ol>
W przypadku załączonego programu posłużyliśmy się uproszczoną wersją tego algorytmu. Spowodowane to zostało tym, że dwa identyczne stany Sudoku nie mogą mieć różnej heurystyki, a zatem nie ma potrzeby podmieniania stanów w kolejce open.

[java]
List children = currentState.getChildren();
for (SudokuState state : children) {
 if (closed.contains(state.getHashCode())) {
  continue;
 } else if (!open.contains(state)) {
  open.add(state);
 }
}[/java]

Plansza sudoku w zamieszczonym programie została zaimplementowana jako dwuwymiarowa tablica (macierz). Każdemu elementowi tej tablicy odpowiada pewna wartość. Mając określony stan początkowy planszy 9x9 (n wierszy na n kolumn), podzielonej na [math]n=9[/math] bloków o wymiarach 3x3 [math]\sqrt n[/math]x[math]\sqrt n[/math], należy wypełnić planszę w taki sposób, aby w każdej kolumnie, wierszu i podkwadracie były wykorzystane wszystkie liczby z przedziału 1...n. Poprzez "nowy stan k + 1" rozumiemy stan k uzupełniony dodatkowo o jedną cyfrę w dowolnym pasującym miejscu.

Każdy stan Sudoku, jak i każdy element stanu (wyraz w macierzy), posiada swoją heurystykę. Heurystyka pojedynczego elementu h(x,y) wyrażona jest jako wszystkie <em>pozostałe możliwe wartości</em> elementu o współrzędnych x i y. Przykładowo jeżeli w dany wyraz macierzy możemy wstawić dwie różne cyfry, to heurystyka tej komórki wynosi 2. Heurystykę stanu Sudoku obliczamy na podstawie wzoru :

[math] H(stan) = \sum\limits_{x=1}^n \sum\limits_{y=1}^n h(x,y)[/math]

Heurystyka algorytmu opiera się na założeniu, że najpierw powinniśmy sprawdzać stany, które mają najmniej możliwych wartości. Spośród stanów znajdujących się w kolejce priorytetowej 'open' wybieramy w pierwszej kolejności te, które mają najmniejszą heurystykę stanu. Heurystyka elementu przydaje się do konstrukcji dzieci aktualnego stanu (nowych stanów), albowiem element, który ma najmniejszą heurystykę służy do utworzenia nowych stanów. Innymi słowy, nowe stany różnią się od stanu rodzica wartością dodaną do wyrazu macierzy o najmniejszej heurystyce.

<a href="http://kohonen.pl/wp-content/uploads/2013/01/sudoku_best_first_search.zip" rel="attachment wp-att-223">sudoku_best_first_search.zip - program do pobrania</a>

[1] <a href="http://en.wikipedia.org/wiki/Mathematics_of_Sudoku">http://en.wikipedia.org/wiki/Mathematics_of_Sudoku</a>
[2] <a title="http://www.afjarvis.staff.shef.ac.uk/sudoku/" href="http://www.afjarvis.staff.shef.ac.uk/sudoku/">http://www.afjarvis.staff.shef.ac.uk/sudoku/</a>