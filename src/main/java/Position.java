/**
 * Represents a Chess board location where the column is a character A-H
 * and the row is an integer between 1 and 8 inclusive.
 */
public class Position {
    private final int column;
    private final int row;

    /**
     * Creates a position used to control movement on the board.
     * @param column Character A-H
     * @param row Integer 1-8
     */
    public Position(char column, int row) {
        column = Character.toUpperCase(column);
        if (column > 'H' || row < 1 || row > 8) throw new IllegalArgumentException();
        this.column = column - 65;
        this.row = row - 1;
    }

    /**
     * Creates a position using zero-indexed column and row values.
     * @param column Zero-indexed column
     * @param row Zero-indexed row
     */
    public Position(int row, int column) {
        if (!(column < 8 && column >= 0 && row < 8 && row >= 0)) throw new IllegalArgumentException();
        this.column = column;
        this.row = row;
    }

    /**
     * Returns zero indexed column value as an integer.
     * @return Column value as an integer
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns zero indexed column value as an integer.
     * @return Column value as an integer
     */
    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        char c = (char) (column + 65);
        return c + Integer.toString(row + 1);
    }
}
