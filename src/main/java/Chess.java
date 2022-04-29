public interface Chess {
    void setBoard();

    void setBoard(String[][] board);

    void movePiece(Move move);

    boolean movePiece(Position p1, Position p2);

    Move castle(boolean isWhite, boolean queenSize);

    boolean isOver();

    String getBoard();

    boolean whiteToMove();

    Move alphaBetaMove(Move move, int depth, int alpha, int beta, boolean maximize);

    Move miniMaxMove(Move move, int depth, boolean maximize);
}
