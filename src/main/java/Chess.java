public interface Chess {
    void setBoard();

    void setBoard(String[][] board);

    boolean movePiece(Position p1, Position p2);

    Move castle(boolean isWhite, boolean queenSize);

    String getBoard();

    boolean whiteToMove();
}
