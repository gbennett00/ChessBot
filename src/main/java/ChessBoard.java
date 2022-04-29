import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * ChessBoard represents a physical chessboard and allows for gameplay (provided by client classes).
 *
 * @author Garrett Bennett (gab0041@auburn.edu)
 * @since 1.0
 */
public class ChessBoard implements Chess {

    // piece array
    private final Piece[][] board = new Piece[8][8];
    private final HashSet<Piece> whitePieces = new HashSet<>();
    private final HashSet<Piece> blackPieces = new HashSet<>();
    private final Piece whiteKing;
    private final Piece blackKing;

    private boolean queenBlackCastle = true;
    private boolean kingBlackCastle = true;
    private boolean queenWhiteCastle = true;
    private boolean kingWhiteCastle = true;

    private boolean whiteToMove = true;

    private boolean isOver = false;

    private int score = 0;


    public ChessBoard() {
        setBoard();
        whiteKing = locateKing(true);
        blackKing = locateKing(false);
    }

    public ChessBoard(String[][] boardIn) {
        setBoard(boardIn);
        whiteKing = locateKing(true);
        blackKing = locateKing(false);
    }

    public static void main(String[] args) {
        ChessBoard chess = new ChessBoard();
        System.out.println(chess.validPosition(1, -1));
    }

    ///////////////////
    // Board methods //
    ///////////////////

    private void updateScore(Piece p, boolean add) {
        char c = p.getType();
        int i = ((p.getColor() == 'w') == add) ? 1 : -1;
        switch (c) {
            case 'k': score += (i * 200);
            case 'q': score += (i * 10);
            case 'r': score += (i * 5);
            case 'n': score += (i * 3);
            case 'b': score += (i * 3);
            case 'p': score += i;
        }
    }


    @Override
    public void setBoard() {
        char[] order = {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'};
        Piece p;
        for (int c = 0; c < 8; c++) {
            p = new Piece('w', 'p');
            board[1][c] = p;
            p.setPosition(new Position(1, c));
            whitePieces.add(p);
            updateScore(p, true);

            p = new Piece('b', 'p');
            board[6][c] = p;
            p.setPosition(new Position(6, c));
            blackPieces.add(p);
            updateScore(p, true);

            p = new Piece('w', order[c]);
            board[0][c] = p;
            p.setPosition(new Position(0, c));
            whitePieces.add(p);
            updateScore(p, true);

            p = new Piece('b', order[c]);
            board[7][c] = p;
            p.setPosition(new Position(7, c));
            blackPieces.add(p);
            updateScore(p, true);
        }
    }

    public void setBoard(String[][] boardIn) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (boardIn[r][c] != null) {
                    String piece = boardIn[r][c];
                    Piece p = new Piece(piece.charAt(0), piece.charAt(1));
                    p.setPosition(new Position(7-r, c));
                    board[7-r][c] = p;
                    if (p.getColor() == 'w') whitePieces.add(p);
                    else blackPieces.add(p);
                    updateScore(p, true);
                }
            }
        }
    }

    public String getBoard() {
        StringBuilder out = new StringBuilder("  A   B   C   D   E   F   G   H  ");
        for (int i = 7; i >= 0; i--) {
            out.append("\n").append(i + 1);
            for (int j = 0; j < 8; j++) {
                Piece loc = board[i][j];
                out.append((loc == null) ? " -- " : " " + loc + " ");
            }
        }
        return out + "\n";
    }

    public String toString() {
        return getBoard();
    }

    /**
     * Executes a move given in the parameter. Moves piece on the board field and updates
     * position field of each affected piece. If necessary removes attacked piece from the
     * respective set of pieces.
     *
     * Note: If this is a castling move, a helper executeCastle method will be called.
     *
     * @param m Move to be executed
     */
    public void executeMove(Move m) {
        if (m == null) return;
        if (m.isCastle()) {
            executeCastle(m);
            return;
        }
        Position p1 = m.getStart();
        Position p2 = m.getEnd();
        if (pieceAt(p1) == null) {
            throw new IllegalArgumentException();
        }
        Piece attacking = pieceAt(p1);
        Piece attacked = pieceAt(p2);
        board[p2.getRow()][p2.getColumn()] = attacking;
        board[p1.getRow()][p1.getColumn()] = null;
        attacking.setPosition(p2);
        if (attacked != null) {
            if (attacked.getColor() == 'w') whitePieces.remove(attacked);
            else blackPieces.remove(attacked);
            attacked.setPosition(null);
            updateScore(attacked, false);
            if (attacked.getType() == 'k') isOver = true;
        }
    }

    /**
     * Executes a move from a starting position to an end position by creating a new move
     * and calling executeMove(move). Returns the move that was executed.
     * @param p1 Starting position
     * @param p2 Ending position
     * @return Move that was executed
     */
    public Move executeMove(Position p1, Position p2) {
        Move move = new Move(p1, p2, pieceAt(p1), pieceAt(p2), false);
        executeMove(move);
        return move;
    }

    private void executeCastle(Move m) {
        boolean isWhite = m.getStart().getRow() == 0;
        boolean queen = m.getStart().getColumn() == 0;
        castle(isWhite, queen);
    }

    public void undoMove(Move m) {
        if (m == null) return;
        if (m.isCastle()) {
            undoCastle(m);
            return;
        }
        board[m.getStart().getRow()][m.getStart().getColumn()] = m.getMovingPiece();
        board[m.getEnd().getRow()][m.getEnd().getColumn()] = m.getAttackedPiece();
        m.getMovingPiece().setPosition(new Position(m.getStart().getRow(), m.getStart().getColumn()));
        if (m.getAttackedPiece() != null) {
            m.getAttackedPiece().setPosition(new Position(m.getEnd().getRow(), m.getEnd().getColumn()));
            if (m.getAttackedPiece().getColor() == 'w') whitePieces.add(m.getAttackedPiece());
            else blackPieces.add(m.getAttackedPiece());
            updateScore(m.getAttackedPiece(), true);
            if (m.getAttackedPiece().getType() == 'k') isOver = false;
        }
    }

    public void undoCastle(Move m) {
        Position rookStart = m.getStart();
        Position rookEnd = m.getEnd();
        boolean queenSide = m.getStart().getColumn() == 0;
        int row = rookStart.getRow();
        int kingStartCol = (queenSide) ? 2 : 6;
        // move rook back to original location
        board[row][rookStart.getColumn()] = m.getAttackedPiece();
        board[row][rookEnd.getColumn()] = null;
        m.getAttackedPiece().setPosition(rookStart);
        // move king to original location
        board[row][4] = m.getMovingPiece();
        board[row][kingStartCol] = null;
        m.getMovingPiece().setPosition(new Position(row, 4));
        whiteToMove = !whiteToMove;
    }

    public Piece pieceAt(Position p) {
        return board[p.getRow()][p.getColumn()];
    }

    public Piece locateKing(boolean isWhite) {
        HashSet<Piece> pieces = (isWhite) ? whitePieces : blackPieces;
        for (Piece p : pieces) {
            if (p.getType() == 'k') return p;
        }
        System.out.println(getBoard());
        System.out.println();
        throw new IllegalStateException();
    }

    public Piece getKing(boolean isWhite) {
        return (isWhite) ? whiteKing : blackKing;
    }

    public boolean whiteToMove() {
        return whiteToMove;
    }

    public boolean verifyBoard() {
        for (Piece p : whitePieces) {
            if (!pieceAt(p.getPosition()).equals(p)) return false;
        }
        for (Piece p : blackPieces) {
            if (!pieceAt(p.getPosition()).equals(p)) return false;
        }
        return true;
    }

    public boolean isOver() {
        return isOver;
    }


    //////////////////////
    // Movement methods //
    //////////////////////

    @Override
    public boolean movePiece(Position p1, Position p2) {
        if ((!validMove(p1, p2, true)) ||  (whiteToMove != (pieceAt(p1).getColor() == 'w'))) return false;
        int r = p1.getRow();
        int c = p1.getColumn();
        if (kingWhiteCastle && r == 0 && (c == 4 || c == 7)) kingWhiteCastle = false;
        if (kingBlackCastle && r == 7 && (c == 4 || c == 7)) kingBlackCastle = false;
        if (queenWhiteCastle && r == 0 && (c == 4 || c == 0)) queenWhiteCastle = false;
        if (queenBlackCastle && r == 7 && (c == 4 || c == 0)) queenBlackCastle = false;
        executeMove(new Move(p1, p2, pieceAt(p1), pieceAt(p2), false));
        Piece p = pieceAt(p2);
        checkMate(p.getColor() == 'w');
        whiteToMove = !whiteToMove;
        return true;
    }

    public void movePiece(Move m) {
        Position p1 = m.getStart();
        Position p2 = m.getEnd();
        boolean correctMove = (whiteToMove != (pieceAt(p1).getColor() == 'w'));
        if ((!validMove(p1, p2, true)) ||  correctMove) return;
        int r = p1.getRow();
        int c = p1.getColumn();
        if (kingWhiteCastle && r == 0 && (c == 4 || c == 7)) kingWhiteCastle = false;
        if (kingBlackCastle && r == 7 && (c == 4 || c == 7)) kingBlackCastle = false;
        if (queenWhiteCastle && r == 0 && (c == 4 || c == 0)) queenWhiteCastle = false;
        if (queenBlackCastle && r == 7 && (c == 4 || c == 0)) queenBlackCastle = false;
        executeMove(m);
        checkMate(m.getMovingPiece().getColor() == 'w');
        whiteToMove = !whiteToMove;
    }

    /**
     * This method checks if a move created a checkmate. The generateMoves method is called
     * for the opponent and, if there is a checkmate, no moves will be generated.
     *
     * @param moveIsWhite Color that recently moved
     */
    private void checkMate(boolean moveIsWhite) {
        Set<Move> moves = generateMoves(!moveIsWhite);
        if (moves.size() == 0) isOver = true;
    }

    public boolean validMove(Position p1, Position p2, boolean check) {
        Piece piece = pieceAt(p1);
        if (piece == null || p1.equals(p2)) return false;
        switch (piece.getType()) {
            case 'p':
                if (!validPawn(p1, p2)) return false;
                break;
            case 'r':
                if (!validRook(p1, p2)) return false;
                break;
            case 'n':
                if (!validKnight(p1, p2)) return false;
                break;
            case 'b':
                if (!validBishop(p1, p2)) return false;
                break;
            case 'q':
                if (!validQueen(p1, p2)) return false;
                break;
            case 'k':
                if (!validKing(p1, p2)) return false;
                break;
            default:
                return false;
        }
        Move tempMove = new Move(p1, p2, pieceAt(p1), pieceAt(p2), false);
        executeMove(tempMove);
        boolean valid = !check || !isInCheck(getKing(pieceAt(p2).getColor() == 'w').getPosition());
        undoMove(tempMove);
        return valid;
    }


    private boolean validKing(Position p1, Position p2) {
        int xMove = p2.getColumn() - p1.getColumn();
        int yMove = p2.getRow() - p1.getRow();
        // return false for any invalid moves
        if (Math.abs(xMove) > 1 || Math.abs(yMove) > 1 || (xMove == 0 && yMove == 0)) return false;
        return validCapture(p1, p2);
    }

    private boolean validQueen(Position p1, Position p2) {
        int p1r = p1.getRow(), p1c = p1.getColumn();
        int yMove = p2.getRow() - p1r, yAbs = Math.abs(yMove), xMove = p2.getColumn() - p1c, xAbs = Math.abs(xMove);
        if (!(xMove == 0 || yMove == 0 || xAbs == yAbs)) return false;
        int distance = Math.max(xAbs, yAbs);
        xMove = (xMove != 0) ? xMove / xAbs : 0;
        yMove = (yMove != 0) ? yMove / yAbs : 0;
        for (int i = 1; i < distance; i++) {
            int c = p1c + xMove * i;
            int r = p1r + yMove * i;
            if (board[r][c] != null) return false;
        }
        return validCapture(p1, p2);
    }

    private boolean validBishop(Position p1, Position p2) {
        int p1r = p1.getRow(), p1c = p1.getColumn();
        int yMove = p2.getRow() - p1r, xMove = p2.getColumn() - p1c;
        if (!(Math.abs(xMove) == Math.abs(yMove) && xMove != 0)) return false;
        int distance = Math.abs(xMove);
        xMove = xMove / Math.abs(xMove);
        yMove = yMove / Math.abs(yMove);
        for (int i = 1; i < distance; i++) {
            int c = p1c + xMove * i;
            int r = p1r + yMove * i;
            if (board[r][c] != null) return false;
        }
        return validCapture(p1, p2);
    }

    private boolean validKnight(Position p1, Position p2) {
        int p1r = p1.getRow(), p1c = p1.getColumn();
        int yMove = p2.getRow() - p1r, yAbs = Math.abs(yMove), xMove = p2.getColumn() - p1c, xAbs = Math.abs(xMove);
        if (!((xAbs + yAbs == 3) && (xAbs == 1 || yAbs == 1))) return false;
        return validCapture(p1, p2);
    }

    private boolean validRook(Position p1, Position p2) {
        int p1r = p1.getRow(), p1c = p1.getColumn();
        int yMove = p2.getRow() - p1r, yAbs = Math.abs(yMove), xMove = p2.getColumn() - p1c, xAbs = Math.abs(xMove);
        if (!(xMove == 0 || yMove == 0)) return false;
        int distance = Math.max(xAbs, yAbs);
        xMove = (xMove != 0) ? xMove / xAbs : 0;
        yMove = (yMove != 0) ? yMove / yAbs : 0;
        for (int i = 1; i < distance; i++) {
            int c = p1c + xMove * i;
            int r = p1r + yMove * i;
            if (board[r][c] != null) return false;
        }
        return validCapture(p1, p2);
    }

    private boolean validPawn(Position p1, Position p2) {
        int p1r = p1.getRow(), p1c = p1.getColumn();
        int yMove = p2.getRow() - p1r, xMove = p2.getColumn() - p1c;
        boolean isWhite = (pieceAt(p1).getColor() == 'w');
        if ((yMove < 0 && isWhite) || (yMove > 0 && (!isWhite)) || yMove == 0) return false;
        // move is in correct direction
        if (xMove == 0) {
            // move is not diagonal and cannot kill
            int yAbs = Math.abs(yMove);
            if (yAbs == 1) {
                return (pieceAt(p2) == null);
            } else if (yAbs == 2) {
                return ((board[p1r+yMove][p1c] == null) && (board[p1r + (yMove/2)][p1c] == null) &&
                        ((isWhite && p1.getRow() == 1) || ((!isWhite) && p1.getRow() == 6)));
            } else {
                return false;
            }
        } else if (Math.abs(xMove) == 1) {
            // move is diagonal and must kill
            return ((Math.abs(yMove) == 1) && (pieceAt(p2) != null) && validCapture(p1, p2));
        } else {
            return false;
        }
    }

    private boolean validCapture(Position p1, Position p2) {
        return (pieceAt(p2) == null || pieceAt(p2).getColor() != pieceAt(p1).getColor());
    }

    @Override
    public Move castle(boolean isWhite, boolean queenSide) {
        if ((!validCastle(isWhite, queenSide)) || (isWhite != whiteToMove)) return null;
        int ogLocation = (queenSide) ? 0 : 7;
        int newKing = (queenSide) ? 2 : 6;
        int tempKing = (queenSide) ? 3 : 5;
        int newRook = (queenSide) ? 3 : 5;
        int row = (isWhite) ? 0: 7;
        Move m1 = executeMove(new Position(row, 4), new Position(row, tempKing));
        boolean check = isInCheck(new Position(row, tempKing));
        if (check) {
            undoMove(m1);
            return null;
        }
        Move m2 = executeMove(new Position(row, tempKing), new Position(row, newKing));
        check = isInCheck(new Position(row, newKing));
        if (check) {
            undoMove(m2);
            undoMove(m1);
            return null;
        }
        Position sRook = new Position(row, ogLocation);
        Position eRook = new Position(row, newRook);
        executeMove(sRook, eRook);
        whiteToMove = !whiteToMove;
        return new Move(sRook, eRook, pieceAt(new Position(row, newKing)), pieceAt(eRook), true);
    }

    public boolean validCastle(boolean isWhite, boolean queenSide) {
        // switch based on whether its white or queen side
        // verify that this castle is valid based on boolean, ensure there are no other pieces in the way,
        //      and make sure that the rook has not been captured

        if (validCastleCorner(isWhite, queenSide) && clearCastlePath(isWhite, queenSide)) {
            int row = (isWhite) ? 0 : 7;
            int side = (queenSide) ? 0 : 7;
            char color = (isWhite) ? 'w' : 'b';
            Piece king = pieceAt(new Position(row, 4));
            Piece rook = pieceAt(new Position(row, side));
            if (king == null || rook == null || king.getColor() != color || rook.getColor() != color
                    || rook.getType() != 'r' || king.getType() != 'k') return false;
            boolean inCheck = isInCheck(new Position(row, 4));
            boolean correctColor = pieceAt(new Position(row, side)).getColor() == color;
            return ((!inCheck) && (correctColor));
        }
        return false;
    }



    private boolean validCastleCorner(boolean isWhite, boolean queenSide) {
        // returns boolean used to determine if castle
        if (isWhite && !queenSide) return kingWhiteCastle;
        else if (isWhite) return queenWhiteCastle;
        else if (!queenSide) return kingBlackCastle;
        else return queenBlackCastle;
    }

    private boolean clearCastlePath(boolean isWhite, boolean queenSide) {
        int row = (isWhite) ? 0 : 7;
        int[] columns = (queenSide) ? new int[]{1, 2, 3} : new int[]{5, 6};
        for (int col : columns) {
            if (board[row][col] != null) return false;
        }
        return true;
    }

    public boolean validPosition(int row, int column) {
        return row >= 0 &&
                row < 8 &&
                column >=0 &&
                column < 8;
    }


    /////////////////////////////
    // Move Generation Methods //
    /////////////////////////////

    public Set<Move> generateMoves(boolean isWhite) {
        Set<Piece> pieces = (isWhite) ? new HashSet<>(whitePieces) : new HashSet<>(blackPieces);
        Set<Move> moves = new HashSet<>();
        Set<Move> tempMoves;
        for (Piece piece : pieces) {
            switch (piece.getType()) {
                case 'k' -> {
                    tempMoves = generateKingMoves(piece.getPosition());
                    moves.addAll(tempMoves);
                }
                case 'q' -> {
                    tempMoves = generateQueenMoves(piece.getPosition());
                    if (tempMoves != null) moves.addAll(tempMoves);
                }
                case 'n' -> {
                    tempMoves = generateKnightMoves(piece.getPosition());
                    if (tempMoves != null) moves.addAll(tempMoves);
                }
                case 'r' -> {
                    tempMoves = generateRookMoves(piece.getPosition());
                    if (tempMoves != null) moves.addAll(tempMoves);
                }
                case 'b' -> {
                    tempMoves = generateBishopMoves(piece.getPosition());
                    if (tempMoves != null) moves.addAll(tempMoves);
                }
                case 'p' -> {
                    tempMoves = generatePawnMoves(piece.getPosition());
                    if (tempMoves != null) moves.addAll(tempMoves);
                }
                default -> {
                }
            }
        }
        return moves;
    }

    protected Set<Move> generatePawnMoves(Position position) {
        Set<Move> moves = new HashSet<>();
        int r = position.getRow(), c = position.getColumn();
        int dir = (pieceAt(position).getColor() == 'w') ? 1 : -1;
        for (int x = -1; x < 2; x++) {
            int row = r+dir, column = c + x;
            if (validPosition(row, column)) {
                Position p = new Position(row, column);
                if (validMove(position, p, true)) moves.add(new Move(position, p, pieceAt(position), pieceAt(p), false));
            }
        }
        if (validPosition(r + (2 * dir), c)) {
            Position p = new Position(r + (2 * dir), c);
            if (validMove(position, p, true)) moves.add(new Move(position, p, pieceAt(position), pieceAt(p), false));
        }
        return moves;
    }

    protected Set<Move> generateBishopMoves(Position position) {
        Set<Move> moves = new HashSet<>();
        int r = position.getRow(), c = position.getColumn();
        for (int i = 4; i <= 7; i++) {
            int[] dir = omnidirectional(i);
            for (int j = 1; j <= 8; j++) {
                int row = r + (j * dir[0]), column = c + (j * dir[1]);
                if (validPosition(row, column)) {
                    Position p2 = new Position(row, column);
                    if (validMove(position, p2, true)) moves.add(new Move(position, p2, pieceAt(position), pieceAt(p2), false));
                }
            }
        }
        return moves;
    }

    protected Set<Move> generateRookMoves(Position position) {
        Set<Move> moves = new HashSet<>();
        int r = position.getRow(), c = position.getColumn();
        for (int i = 0; i <= 3; i++) {
            int[] dir = omnidirectional(i);
            for (int j = 1; j <= 8; j++) {
                int row = r + (j * dir[0]), column = c + (j * dir[1]);
                if (validPosition(row, column)) {
                    Position p2 = new Position(row, column);
                    if (validMove(position, p2, true)) moves.add(new Move(position, p2, pieceAt(position), pieceAt(p2), false));
                }
            }
        }
        return moves;
    }

    protected Set<Move> generateKnightMoves(Position position) {
        Set<Move> moves = new HashSet<>();
        int r = position.getRow(), c = position.getColumn();
        for (int i = 4; i <= 7; i++) {
            int[] dir = omnidirectional(i);
            int row = r + (2 * dir[0]), column = c + dir[1];
            if (validPosition(row, column)) {
                Position p2 = new Position(row, column);
                if (validMove(position, p2, true)) moves.add(new Move(position, p2, pieceAt(position), pieceAt(p2), false));
            }
            row = r + dir[0];
            column = c + (2 * dir[1]);
            if (validPosition(row, column)) {
                Position p2 = new Position(row, column);
                if (validMove(position, p2, true)) moves.add(new Move(position, p2, pieceAt(position), pieceAt(p2), false));
            }

        }
        return moves;
    }

    protected Set<Move> generateQueenMoves(Position position) {
        Set<Move> moves = new HashSet<>();
        int r = position.getRow(), c = position.getColumn();
        for (int i = 0; i < 8; i++) {
            int[] dir = omnidirectional(i);
            for (int j = 1; j <= 8; j++) {
                int row = r + (j * dir[0]), column = c + (j * dir[1]);
                if (validPosition(row, column)) {
                    Position p2 = new Position(row, column);
                    if (validMove(position, p2, true)) moves.add(new Move(position, p2, pieceAt(position), pieceAt(p2), false));
                }
            }
        }
        return moves;
    }

    // includes castling moves
    protected Set<Move> generateKingMoves(Position position) {
        Set<Move> moves = new HashSet<>();
        int r = position.getRow(), c = position.getColumn();
        for (int i = 0; i < 8; i++) {
            int[] dir = omnidirectional(i);
            int row = r + dir[0], column = c + dir[1];
            if (validPosition(row, column)) {
                Position p2 = new Position(row, column);
                if (validMove(position, p2, true)) moves.add(new Move(position, p2, pieceAt(position), pieceAt(p2), false));
            }
        }
        for (int i = 0; i < 4; i++) {
            // add castle moves if they are valid
            Move castle = castle(i > 1, i % 2 != 0);
            if (castle != null) {
                moves.add(castle);
                undoMove(castle);
            }
        }
        return moves;
    }

    /**
     * Used to represent all possible directions that pieces can move (aside from knights).
     * Parameters 0-3 give horizontal directions. Parameters 4-7 give diagonal directions.
     * @param i Direction as described above
     * @return Array of two values that are either 0, 1, or -1.
     */
    private int[] omnidirectional(int i) {
        return switch (i) {
            case 0 -> new int[]{1, 0};
            case 1 -> new int[]{0, -1};
            case 2 -> new int[]{0, 1};
            case 3 -> new int[]{-1, 0};
            case 4 -> new int[]{1, -1};
            case 5 -> new int[]{1, 1};
            case 6 -> new int[]{-1, -1};
            case 7 -> new int[]{-1, 1};
            default -> throw new IllegalArgumentException();
        };
    }


    //////////////////////////////
    // Overall Gameplay Methods //
    //////////////////////////////

    /**
     * Position must be a location with a piece on it, or it must be on the very top or bottom row.
     * @return Negative indicates black is in check, positive indicates white is in check
     */
    public boolean isInCheck(Position p) {
        Piece piece = pieceAt(p);
        int row = p.getRow();
        if (piece == null && row != 8 && row != 0) throw new IllegalArgumentException();
        boolean isWhite = ((piece == null && row == 0) || (piece != null && piece.getColor() == 'w'));
        Position king = getKing(isWhite).getPosition();
        Iterator<Piece> i = (isWhite) ? blackPieces.iterator() : whitePieces.iterator();
        while (i.hasNext()) {
            Position pos = i.next().getPosition();
            if (validMove(pos, king, false)) {
                return true;
            }
        }
        return false;
    }

    public Move alphaBetaMove(Move move, int depth, int alpha, int beta, boolean maximize) {
        executeMove(move);
        Set<Move> moves = generateMoves(maximize);
        if (depth == 0 || moves.size() == 0) {
            int s;
            if (moves.size() == 0) s = (maximize) ? -300 : 300;
            else s = score;
            undoMove(move);
            return (move != null) ? move.guarantee(s) : null;
        }

        Move minMax = null;
        // board is now ready to be evaluated
        for (Move m : moves) {
            // get static evaluation of move m
            Move next = alphaBetaMove(m,depth-1, alpha, beta,!maximize);
            if (next == null) {
                undoMove(move);
                return null;
            }
            int eval = next.getGuaranteedScore();
            m.guarantee(eval);
            // max is the move that has the maximal evaluation between max and the evaluation of m
            if (maximize) {
                minMax = Move.maxMove(m, minMax);
                alpha = Math.max(alpha, eval);
            }else {
                minMax = Move.minMove(m, minMax);
                beta = Math.min(beta, eval);
            }
            if (beta <= alpha) break;
        }
        // board returns to original state
        undoMove(move);
        return minMax;
    }

    public Move miniMaxMove(Move move, int depth, boolean maximize) {
        executeMove(move);
        Set<Move> moves = generateMoves(maximize);
        if (depth == 0 || moves.size() == 0) {
            int s = score;
            undoMove(move);
            return (move != null) ? move.guarantee(s) : null;
        }

        if (maximize) {
            Move max = null;
            // board is now ready to be evaluated
            for (Move m : moves) {
                // get static evaluation of move m
                Move next = miniMaxMove(m,depth-1,false);
                if (next == null) {
                    undoMove(move);
                    return null;
                }
                int eval = next.getGuaranteedScore();
                m.guarantee(eval);
                // max is the move that has the maximal evaluation between max and the evaluation of m
                max = Move.maxMove(m, max);
            }
            // board returns to original state
            undoMove(move);
            return max;
        } else {
            Move min = null;
            for (Move m : moves) {
                Move next = miniMaxMove(m,depth-1, true);
                if (next == null) {
                    undoMove(move);
                    return null;
                }
                int eval = next.getGuaranteedScore();
                m.guarantee(eval);
                min = Move.minMove(m, min);
            }
            undoMove(move);
            return min;
        }
    }
}
