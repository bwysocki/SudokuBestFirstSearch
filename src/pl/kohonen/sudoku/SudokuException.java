package pl.kohonen.sudoku;

public class SudokuException extends Exception{
    
    private static final long serialVersionUID = 1L;
    
    public static final String PARSER_EXCEPTION = "PARSER_EXCEPTION";
    public static final String COLUMNS_ORDER = "COLUMNS_ORDER";
    public static final String ROWS_ORDER = "ROWS_ORDER";
    public static final String MATRIX_UGLY = "MATRIX_UGLY";
    
    public SudokuException(String message) {
        super(message);
    }
    
}
