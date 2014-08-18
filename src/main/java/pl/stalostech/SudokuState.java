package pl.stalostech;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class represents Sudoku state.
 *
 * @author Bartosz Wysocki
 */
public class SudokuState implements Comparable<SudokuState> {

    private SudokuState parent = null;

    //heuristic value for current state
    private double h = Double.POSITIVE_INFINITY;

    //matrix representing Sudoku
    private Integer[][] matrix;

    //dimension of Sudoku matrix
    private Integer dimension = null;

    /** public methods **/

    //Create Sudoku state specifing matrix with values
    public SudokuState(Integer[][] matrix, SudokuState parent) {
        this.matrix = matrix;
        this.dimension = matrix.length;
        h = computeHeuristic();
        this.parent = parent;
    }

    //Create Sudoku state from xml file
    public SudokuState(File xmlFile, int dimension) throws SudokuException {

        //sets dimension of sudoku (4x4 or 9x9)
        this.dimension = dimension;
        matrix = new Integer[dimension][dimension];

        //from xml file, create new sudoku map
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {

                private int rowIterator = -1;
                private int columnIterator = -1;
                private boolean elActive = false;

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

                    if (qName.equalsIgnoreCase("row")) {
                        rowIterator++;
                        columnIterator = -1;
                    } else if (qName.equalsIgnoreCase("e")) {
                        elActive = true;
                        columnIterator++;
                    }

                }

                public void endElement(String uri, String localName, String qName) throws SAXException {

                    if (qName.equalsIgnoreCase("e")) {
                        elActive = false;
                    }
                }

                public void characters(char ch[], int start, int length) throws SAXException {

                    if (elActive) {
                        String field = new String(ch, start, length);

                        if (field.equalsIgnoreCase("*")) {
                            matrix[rowIterator][columnIterator] = null;
                        } else {
                            matrix[rowIterator][columnIterator] = Integer.valueOf(field);
                        }
                    }

                }
            };
            saxParser.parse(xmlFile, handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new SudokuException(SudokuException.PARSER_EXCEPTION);
        }
        h = computeHeuristic();
    }

    public SudokuState getParent() {
        return parent;
    }

    /**
     * @return heuristic for current state
     */
    public double getH() {
        return h;
    }

    /**
     * Returns list of all children of current state.
     * For Sudoku the function returns all possible states
     * constructed by inserting new (single) value.
     * @return list of children
     */
    public List<SudokuState> getChildren() {
        List<SudokuState> children = new ArrayList<SudokuState>();

        double lowestH = Double.POSITIVE_INFINITY;

        int chosenCellRow = -1;
        int chosenCellColumn = -1;

        //get cell with lowest heuristic
        for (int row = 0; row < dimension; row++) {
            for (int column = 0; column < dimension; column++) {
                if (matrix[row][column] == null) {

                    double cellH = computeHeuristicForCell(row, column);

                    if (lowestH > cellH) {
                        lowestH = cellH;
                        chosenCellRow = row;
                        chosenCellColumn = column;
                    }

                }
            }
        }

        children.addAll(createChildrenForCell(chosenCellRow, chosenCellColumn));

        return children;
    }

    @Override
    public int compareTo(SudokuState otherState) {
        return (getH() > otherState.getH()) ? 1 : -1;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();

        for (int row = 0; row < dimension; row++) {
            for (int column = 0; column < dimension; column++) {
                String el = (matrix[row][column] == null) ? "*" : String.valueOf(matrix[row][column]);
                if (column == dimension - 1) {
                    strBuilder.append(el);
                } else {
                    strBuilder.append(el + " - ");
                }
            }
            strBuilder.append("\n");
        }

        return strBuilder.toString();
    }

    @Override
    public boolean equals(Object other) {
        return getHashCode().equals(((SudokuState) other).getHashCode());
    }

    public String getHashCode() {
        StringBuilder hash = new StringBuilder("");
        for (int row = 0; row < dimension; row++) {
            for (int column = 0; column < dimension; column++) {
                Integer el = matrix[row][column];
                if (el != null) {
                    hash.append(el.toString());
                } else {
                    hash.append("-");
                }
            }
        }
        return hash.toString();
    }

    /** private methods **/

    /**
     * Deep clone matrix
     * @return new matrix
     */
    private Integer[][] cloneMatrix() {
        Integer[][] newMatrix = new Integer[dimension][dimension];
        for (int row = 0; row < dimension; row++) {
            newMatrix[row] = matrix[row].clone();
        }
        return newMatrix;
    }

    /**
     * Create children for cell (in practice with lowest heuristic)
     * @param row
     * @param column
     * @return children for cell
     */
    private List<SudokuState> createChildrenForCell(int row, int column) {
        List<SudokuState> response = new ArrayList<SudokuState>();

        List<Integer> valuesInColumn = getRestrictedValuesForColumn(column);
        List<Integer> valuesInRow = getRestrictedValuesForRow(row);
        List<Integer> valuesInSquare = getRestrictedValuesForSquare(row, column);

        for (int value = 1; value <= dimension; value++) {
            if (!valuesInColumn.contains(value) && !valuesInRow.contains(value) && !valuesInSquare.contains(value)) {

                Integer[][] newMatrix = cloneMatrix();
                newMatrix[row][column] = value;

                response.add(new SudokuState(newMatrix, this));

            }
        }

        return response;
    }

    /**
     * Computes heuristic for current state
     * @return h
     */
    private double computeHeuristic() {
        double h = 0d;
        for (int row = 0; row < dimension; row++) {
            for (int column = 0; column < dimension; column++) {
                if (matrix[row][column] == null) {
                    h += computeHeuristicForCell(row, column);
                }
            }
        }
        return h;
    }

    /**
     * Computes heuristic for specified cell in matrix
     * @return h for cell
     */
    private double computeHeuristicForCell(int row, int column) {
        double h = 0;

        List<Integer> valuesInColumn = getRestrictedValuesForColumn(column);
        List<Integer> valuesInRow = getRestrictedValuesForRow(row);
        List<Integer> valuesInSquare = getRestrictedValuesForSquare(row, column);

        for (int value = 1; value <= dimension; value++) {
            if (!valuesInColumn.contains(value) && !valuesInRow.contains(value) && !valuesInSquare.contains(value)) {
                h++;
            }
        }

        return h;
    }

    /**
     * Gets list of values in column
     * @param column
     * @return list of values being used in specified column
     */
    private List<Integer> getRestrictedValuesForColumn(int column) {
        List<Integer> list = new ArrayList<Integer>();
        for (int row = 0; row < dimension; row++) {
            if (matrix[row][column] != null) {
                list.add(matrix[row][column]);
            }
        }
        return list;
    }

    /**
     * Gets list of values in row
     * @param row
     * @return list of values being used in specified row
     */
    private List<Integer> getRestrictedValuesForRow(int row) {
        List<Integer> list = new ArrayList<Integer>();
        for (int column = 0; column < dimension; column++) {
            if (matrix[row][column] != null) {
                list.add(matrix[row][column]);
            }
        }
        return list;
    }

    /**
     * Gets list of values in 'closest square' for cell
     * @param row
     * @param column
     * @return list of used values in 'closest square'
     */
    private List<Integer> getRestrictedValuesForSquare(int row, int column) {
        List<Integer> list = new ArrayList<Integer>();

        int n = (int) Math.sqrt(dimension);

        int startRow = (int) Math.floor((row) / n) * n;
        int startColumn = (int) Math.floor((column) / n) * n;

        for (int sX = startRow; sX < startRow + n; sX++) {
            for (int sY = startColumn; sY < startColumn + n; sY++) {
                list.add(matrix[sX][sY]);
            }
        }

        return list;
    }

}
