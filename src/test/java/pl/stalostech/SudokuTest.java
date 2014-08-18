package pl.stalostech;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.junit.Test;

public class SudokuTest {

    @Test
    public void test() throws SudokuException {

        URL url = SudokuTest.class.getResource("/sudoku-1.xml");
        File xmlFile2 = new File(url.getPath());
        SudokuState sudoku = new SudokuState(xmlFile2, 4);

        System.out.println("Sudoku representation: ");
        System.out.print(sudoku);

        assertEquals(18d, sudoku.getH(), 0.0001d);
        System.out.println("Heuristic for initial state is : 18");

        assertEquals(1d, sudoku.getChildren().size(), 0.0001d);
        System.out.println("New created sudoku map:");
        System.out.print(sudoku.getChildren().get(0));

        assertEquals(14d, sudoku.getChildren().get(0).getH(), 0.0001d);
        System.out.println("Heuristic for child is : 14");

    }

}
