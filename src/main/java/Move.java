public class Move {
    private final Position start;
    private final Position end;
    private final Piece movingPiece;
    private final Piece attackedPiece;
    private final boolean castle;
    private int guaranteedScore = 0;

    /**
     * Represents a starting and ending position of a moving piece and stores the attacking
     * and attacked piece. In the event of a castle, the moving king is stored as the attacking
     * piece and the rook as the attacked.
     * @param p1 Starting position of attacking piece
     * @param p2 Ending position of attacking piece
     * @param attacking Move piece (king if castling)
     * @param attacked Piece that was removed from board (rook if castling)
     * @param castle True if this is a castling move; false otherwise
     */
    public Move(Position p1, Position p2, Piece attacking, Piece attacked, boolean castle) {
        start = p1;
        end = p2;
        movingPiece = attacking;
        attackedPiece = attacked;
        this.castle = castle;
    }


    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public boolean isCastle() {
        return castle;
    }

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public Piece getAttackedPiece() {
        return attackedPiece;
    }

    public Move guarantee(int guaranteedScore) {
        this.guaranteedScore = guaranteedScore;
        return this;
    }

    public int getGuaranteedScore() {
        return guaranteedScore;
    }

    public static Move maxMove(Move m1, Move m2) {
        if (m1 == null) return m2;
        if (m2 == null || m1.guaranteedScore > m2.guaranteedScore) return m1;
        return m2;
    }

    public static Move minMove(Move m1, Move m2) {
        if (m1 == null) return m2;
        if (m2 == null || m1.guaranteedScore < m2.guaranteedScore) return m1;
        return m2;
    }
}