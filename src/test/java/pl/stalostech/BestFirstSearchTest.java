package pl.stalostech;

import java.io.File;
import java.net.URL;

import org.junit.Test;

public class BestFirstSearchTest {

    @Test
    public void test() throws SudokuException {

        URL url = SudokuTest.class.getResource("/sudoku-1.xml");
        File xmlFile1 = new File(url.getPath());
        SudokuState sudoku = new SudokuState(xmlFile1, 4);

        BestFirstSearch bfs = new BestFirstSearch(sudoku);
        System.out.println("Sollution for sudoku 1 :");
        System.out.print(bfs.search());

        url = SudokuTest.class.getResource("/sudoku-2.xml");
        File xmlFile2 = new File(url.getPath());
        sudoku = new SudokuState(xmlFile2, 9);

        bfs = new BestFirstSearch(sudoku);
        System.out.println("Sollution for sudoku 2 :");
        System.out.print(bfs.search());

        url = SudokuTest.class.getResource("/sudoku-3.xml");
        File xmlFile3 = new File(url.getPath());
        sudoku = new SudokuState(xmlFile3, 9);

        bfs = new BestFirstSearch(sudoku);
        System.out.println("Sollution for sudoku 3 :");
        System.out.print(bfs.search());

    }

}
