import org.junit.Test;

import java.util.Set;

public class ChessBoardTest {
    @Test
    public void testCastle() {
        String[][] testBoard = {{"br",  "bn",  "bb",  "bq",  "bk",  "bb",  "bn",  "br"},
                                {"bp",  "bp",  "bp",  "bp",  "bp",  "bp",  "bp",  null},
                                {null,  null,  null,  null,  null,  null,  null,  null},
                                {null,  null,  null,  null,  null,  null,  null,  null},
                                {null,  null,  null,  null,  null,  null,  null,  null},
                                {null,  null,  null,  null,  null,  null,  null,  null},
                                {"wp",  "wp",  "wp",  "wp",  "wp",  "wp",  null,  null},
                                {"wr",  null,  null,  null,  "wk",  null,  null,  "wr"}};
        ChessBoard chess = new ChessBoard(testBoard);
        Move c = chess.castle(true, false);
        System.out.println(chess.getBoard());
        assert(chess.verifyBoard());
        chess.undoMove(c);
        System.out.println(chess.getBoard());
        assert(chess.verifyBoard());
    }

    @Test
    public void testIsInCheck() {
        String[][] testBoard = {{"br",  "bn",  "bb",  "bq",  "bk",  "bb",  "bn",  "br"},
                                {"bp",  "bp",  "bp",  "bp",  "bp",  "bp",  "br",  null},
                                {null,  null,  null,  null,  null,  null,  null,  null},
                                {null,  null,  null,  null,  null,  null,  null,  null},
                                {null,  null,  null,  null,  null,  null,  null,  null},
                                {null,  null,  null,  null,  null,  null,  null,  null},
                                {"wp",  "wp",  "wp",  "wp",  "wp",  "wp",  "wr",  null},
                                {"wr",  null,  null,  null,  "wk",  null,  null,  "wr"}};
        ChessBoard chess = new ChessBoard(testBoard);
        chess.movePiece(new Position(1, 0), new Position(2, 0));
        assert(!chess.isInCheck(new Position(0, 4)));
        chess.movePiece(new Position(7, 7), new Position(0, 7));
        chess.movePiece(new Position(2, 0), new Position(3, 0));
        assert(chess.isInCheck(new Position(0, 4)));
        chess.movePiece(new Position(1, 6), new Position(0, 6));
        chess.movePiece(new Position(3, 0), new Position(4, 0));
        assert(!chess.isInCheck(new Position(0, 4)));
        chess.movePiece(new Position(0, 7), new Position(0, 6));
        assert(chess.isInCheck(new Position(0, 4)));
        assert(chess.verifyBoard());
    }

    @Test
    public void testPawnMove() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                                {null,  "bp",  "bn",  null,  "wn",  "bn",  "wn",  "wp"},
                                {null,  null,  null,  "bp",  null,  null,  null,  null},
                                {null,  null,  null,  "wp",  null,  null,  "wr",  null},
                                {"wp",  null,  null,  "wb",  null,  "wp",  null,  null},
                                {null,  "wp",  null,  "wq",  "wn",  "wb",  "wp",  "wp"},
                                {"wr",  null,  null,  null,  null,  "wr",  "wk",  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        // valid moves
        // forward one
        assert( chess.validMove(new Position(1, 7), new Position(2, 7), true));
        assert( chess.validMove(new Position(6, 0), new Position(5, 0), true));
        assert(!chess.validMove(new Position(3, 3), new Position(4, 3), true));
        // forward two
        assert( chess.validMove(new Position(1, 7), new Position(3, 7), true));
        assert( chess.validMove(new Position(6, 0), new Position(4, 0), true));
        // capture
        assert( chess.validMove(new Position(6, 5), new Position(5, 4), true));
        assert( chess.validMove(new Position(6, 5), new Position(5, 6), true));

        // invalid moves
        // forward with piece in the way
        assert(!chess.validMove(new Position(6, 5), new Position(5, 5), true));
        assert(!chess.validMove(new Position(6, 5), new Position(4, 5), true));
        assert(!chess.validMove(new Position(1, 6), new Position(3, 6), true));
        // invalid captures
        assert(!chess.validMove(new Position(3, 3), new Position(2, 4), true));
        assert(!chess.validMove(new Position(1, 6), new Position(2, 5), true));
        assert(!chess.validMove(new Position(2, 0), new Position(3, 1), true));
        // double moves when not in right location
        assert(!chess.validMove(new Position(2, 0), new Position(4, 0), true));
        assert(!chess.validMove(new Position(5, 1), new Position(3, 1), true));
        // more than 2 forward
        assert(!chess.validMove(new Position(6, 0), new Position(3, 0), true));
        // backwards
        assert(!chess.validMove(new Position(6, 0), new Position(7, 0), true));
        assert(!chess.validMove(new Position(1, 7), new Position(0, 7), true));
        // stay in same spot
        assert(!chess.validMove(new Position(6, 5), new Position(6, 5), true));
    }

    @Test
    public void testQueenMove() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  null,  null,  "bk",  null},
                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                {null,  "wp",  "bn",  null,  "wn",  "bn",  "wn",  "bp"},
                {null,  null,  null,  "bp",  null,  null,  null,  null},
                {null,  null,  null,  "wp",  null,  null,  "wr",  null},
                {"wp",  null,  "bp",  "wb",  null,  "wp",  null,  null},
                {null,  null,  null,  "wq",  "wn",  "wb",  "wp",  "wp"},
                {"wr",  null,  null,  null,  null,  "wr",  "wk",  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        // valid moves
        // diagonal
        assert( chess.validMove(new Position(1, 3), new Position(5, 7), true));
        assert( chess.validMove(new Position(1, 3), new Position(4, 6), true));
        assert( chess.validMove(new Position(1, 3), new Position(0, 2), true));
        assert( chess.validMove(new Position(1, 3), new Position(0, 4), true));
        assert( chess.validMove(new Position(1, 3), new Position(2, 2), true));
        assert( chess.validMove(new Position(7, 3), new Position(5, 1), true));
        assert( chess.validMove(new Position(7, 3), new Position(6, 4), true));
        // horizontal
        assert( chess.validMove(new Position(1, 3), new Position(1, 0), true));
        assert( chess.validMove(new Position(7, 3), new Position(7, 5), true));
        // vertical
        assert( chess.validMove(new Position(1, 3), new Position(0, 3), true));
        assert( chess.validMove(new Position(7, 3), new Position(5, 3), true));

        // invalid moves
        // capture friendly
        assert(!chess.validMove(new Position(7, 3), new Position(7, 2), true));
        assert(!chess.validMove(new Position(7, 3), new Position(4, 3), true));
        assert(!chess.validMove(new Position(7, 3), new Position(5, 5), true));
        // stay in same spot
        assert(!chess.validMove(new Position(7, 3), new Position(7, 3), true));
        assert(!chess.validMove(new Position(1, 3), new Position(1, 3), true));
        // jump over person
        assert(!chess.validMove(new Position(7, 3), new Position(4, 0), true));
        assert(!chess.validMove(new Position(7, 3), new Position(4, 6), true));
        assert(!chess.validMove(new Position(7, 3), new Position(0, 3), true));
        // knight-like moves
        assert(!chess.validMove(new Position(7, 3), new Position(6, 0), true));
        assert(!chess.validMove(new Position(7, 3), new Position(3, 2), true));

    }

    @Test
    public void testKnightMove() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                                {null,  "bp",  "bn",  null,  "wn",  "bn",  "wn",  "wp"},
                                {null,  null,  null,  "bp",  null,  null,  null,  null},
                                {null,  null,  null,  "wp",  null,  null,  "wr",  null},
                                {"wp",  null,  null,  "wb",  null,  "wp",  null,  null},
                                {null,  "wp",  null,  "wq",  "wn",  "wb",  "wp",  "wp"},
                                {"wr",  null,  null,  null,  null,  "wr",  "wk",  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        // valid
        // any of 8 spots
        assert( chess.validMove(new Position(1, 4), new Position(2, 6), true));
        assert( chess.validMove(new Position(1, 4), new Position(3, 5), true));
        assert( chess.validMove(new Position(1, 4), new Position(2, 2), true));
        assert( chess.validMove(new Position(1, 4), new Position(0, 2), true));
        assert( chess.validMove(new Position(5, 5), new Position(6, 3), true));
        assert( chess.validMove(new Position(5, 5), new Position(3, 4), true));
        assert( chess.validMove(new Position(5, 5), new Position(6, 7), true));
        // capture
        assert( chess.validMove(new Position(5, 5), new Position(3, 6), true));
        assert( chess.validMove(new Position(5, 2), new Position(3, 3), true));
        assert( chess.validMove(new Position(5, 4), new Position(6, 6), true));

        // invalid
        // straight
        assert(!chess.validMove(new Position(5, 5), new Position(5, 3), true));
        assert(!chess.validMove(new Position(5, 5), new Position(3, 5), true));
        // more than 2 in one direction
        assert(!chess.validMove(new Position(5, 5), new Position(4, 2), true));
        assert(!chess.validMove(new Position(5, 5), new Position(2, 6), true));
        // stay in same spot
        assert(!chess.validMove(new Position(5, 5), new Position(5, 5), true));
        // capture same team
        assert(!chess.validMove(new Position(1, 4), new Position(3, 3), true));
    }

    @Test
    public void testRookMove() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                {null,  "bp",  null,  null,  "wn",  "bn",  "wn",  "wp"},
                {null,  null,  null,  "bp",  null,  null,  null,  null},
                {null,  null,  null,  "wp",  null,  null,  "wr",  null},
                {null,  null,  "wp",  "wb",  null,  "wp",  null,  null},
                {null,  "wr",  null,  "bq",  "wn",  "wb",  "wp",  "wp"},
                {"wr",  null,  null,  null,  null,  "wr",  "wk",  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        // valid
        // vertical
        assert( chess.validMove(new Position(1, 1), new Position(4, 1), true));
        assert( chess.validMove(new Position(1, 1), new Position(0, 1), true));
        assert( chess.validMove(new Position(7, 2), new Position(3, 2), true));
        // horizontal
        assert( chess.validMove(new Position(1, 1), new Position(1, 2), true));
        assert( chess.validMove(new Position(0, 0), new Position(4, 0), true));
        assert( chess.validMove(new Position(7, 2), new Position(7, 0), true));
        // capture
        assert( chess.validMove(new Position(7, 2), new Position(2, 2), true));
        assert( chess.validMove(new Position(1, 1), new Position(1, 3), true));

        // invalid
        // diagonal
        assert(!chess.validMove(new Position(7, 2), new Position(6, 3), true));
        assert(!chess.validMove(new Position(7, 2), new Position(5, 4), true));
        // jumping
        assert(!chess.validMove(new Position(7, 2), new Position(4, 5), true));
        assert(!chess.validMove(new Position(7, 2), new Position(0, 2), true));
        assert(!chess.validMove(new Position(3, 6), new Position(6, 6), true));
        // capture same team
        assert(!chess.validMove(new Position(3, 6), new Position(5, 6), true));
        assert(!chess.validMove(new Position(3, 6), new Position(1, 6), true));
        assert(!chess.validMove(new Position(3, 6), new Position(3, 3), true));
        // stay in same spot
        assert(!chess.validMove(new Position(3, 6), new Position(3, 6), true));
        // knight like moves
        assert(!chess.validMove(new Position(3, 6), new Position(2, 4), true));
        assert(!chess.validMove(new Position(3, 6), new Position(4, 4), true));
    }

    @Test
    public void testBishop() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                {"bp",  "bb",  null,  null,  null,  "bb",  "bp",  null},
                {null,  "bp",  "bn",  null,  "wn",  "bn",  "bn",  "wp"},
                {null,  null,  null,  "bp",  null,  null,  null,  null},
                {null,  null,  null,  "bp",  null,  null,  "wr",  null},
                {"wp",  null,  null,  "wb",  null,  "wp",  null,  null},
                {null,  "wp",  null,  "wq",  "wn",  "wb",  "wp",  "wp"},
                {"wr",  null,  null,  null,  null,  "wr",  "wk",  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        // valid moves
        // diagonal in any direction
        assert( chess.validMove(new Position(1, 5), new Position(3, 7), true));
        assert( chess.validMove(new Position(1, 5), new Position(2, 4), true));
        assert( chess.validMove(new Position(6, 1), new Position(5, 0), true));
        assert( chess.validMove(new Position(2, 3), new Position(5, 0), true));
        assert( chess.validMove(new Position(2, 3), new Position(0, 1), true));
        assert( chess.validMove(new Position(2, 3), new Position(4, 5), true));
        // capture opposing team
        assert( chess.validMove(new Position(2, 3), new Position(5, 6), true));
        assert( chess.validMove(new Position(1, 5), new Position(3, 3), true));
        assert( chess.validMove(new Position(6, 5), new Position(5, 4), true));

        // invalid moves
        // capture same team
        assert(!chess.validMove(new Position(2, 3), new Position(1, 4), true));
        assert(!chess.validMove(new Position(6, 5), new Position(5, 6), true));
        assert(!chess.validMove(new Position(6, 5), new Position(5, 6), true));
        // jump
        assert(!chess.validMove(new Position(2, 3), new Position(6, 7), true));
        assert(!chess.validMove(new Position(1, 5), new Position(4, 2), true));
        assert(!chess.validMove(new Position(2, 3), new Position(6, 6), true));
        assert(!chess.validMove(new Position(6, 1), new Position(3, 4), true));
        // stay in same spot
        assert(!chess.validMove(new Position(2, 3), new Position(2, 3), true));
        // vertical or horizontal
        assert(!chess.validMove(new Position(2, 3), new Position(2, 1), true));
        assert(!chess.validMove(new Position(6, 1), new Position(6, 4), true));
        assert(!chess.validMove(new Position(6, 1), new Position(7, 1), true));
        // knight-like moves
        assert(!chess.validMove(new Position(2, 3), new Position(4, 7), true));
        assert(!chess.validMove(new Position(6, 1), new Position(4, 0), true));
    }

    @Test
    public void testKingMove() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                {null,  "bp",  "bn",  null,  "wn",  "bn",  "wn",  "wp"},
                {null,  null,  null,  "bp",  null,  null,  null,  null},
                {null,  null,  null,  "bp",  null,  null,  "wr",  null},
                {"wp",  null,  "wk",  "wb",  null,  "wp",  null,  null},
                {null,  "wp",  null,  "wq",  "wn",  "wb",  "wp",  "wp"},
                {"wr",  null,  null,  null,  null,  "wr",  null,  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        // valid moves
        // any of 8 locations
        assert(!chess.validMove(new Position(2, 2), new Position(3, 1), true));
        assert( chess.validMove(new Position(2, 2), new Position(2, 1), true));
        assert( chess.validMove(new Position(2, 2), new Position(1, 2), true));
        assert(!chess.validMove(new Position(2, 2), new Position(3, 2), true));
        assert(!chess.validMove(new Position(2, 2), new Position(3, 3), true));
        // capture enemy

        // invalid moves
        // capture friendly
        assert(!chess.validMove(new Position(2, 2), new Position(1, 1), true));
        assert(!chess.validMove(new Position(2, 2), new Position(1, 3), true));
        assert(!chess.validMove(new Position(2, 2), new Position(2, 3), true));
        // move more than 2 spaces
        assert(!chess.validMove(new Position(2, 2), new Position(4, 2), true));
        assert(!chess.validMove(new Position(2, 2), new Position(4, 4), true));
        assert(!chess.validMove(new Position(2, 2), new Position(3, 4), true));
        // stay in same spot
        assert(!chess.validMove(new Position(2, 2), new Position(2, 2), true));
    }

    @Test
    public void testPieceAt() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                {null,  "bp",  null,  null,  "bn",  "bn",  null,  "bp"},
                {null,  null,  null,  "bp",  null,  null,  null,  null},
                {null,  null,  null,  "wp",  null,  null,  null,  null},
                {"wp",  null,  null,  "wb",  null,  "wp",  null,  null},
                {null,  "wp",  null,  "wq",  "wn",  "wb",  "wp",  "wp"},
                {"wr",  null,  null,  null,  null,  "wr",  "wk",  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        Position p = new Position('H', 6);
        assert(p.getColumn() == 7);
        assert(p.getRow() == 5);
        Piece s = new Piece('w', 'r');
        Piece piece = chess.pieceAt(new Position('A', 1));
        assert(piece.equals(s));
    }

    @Test
    public void testSetBoard() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                {null,  "bp",  null,  null,  "bn",  "bn",  null,  "bp"},
                {null,  null,  null,  "bp",  null,  null,  null,  null},
                {null,  null,  null,  "wp",  null,  null,  null,  null},
                {"wp",  null,  null,  "wb",  null,  "wp",  null,  null},
                {null,  "wp",  null,  "wq",  "wn",  "wb",  "wp",  "wp"},
                {"wr",  null,  null,  null,  null,  "wr",  "wk",  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        assert( chess.pieceAt(new Position(0, 0)).getType() == 'r');
        assert( chess.pieceAt(new Position(3, 3)).getType() == 'p');
    }

    @Test
    public void testGenerateKingMoves() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                                {null,  "bp",  null,  null,  "bn",  "bn",  null,  "bp"},
                                {null,  null,  null,  "bp",  null,  null,  null,  null},
                                {null,  null,  null,  "wp",  null,  null,  null,  null},
                                {"wp",  null,  null,  "wb",  null,  "wp",  null,  null},
                                {null,  "wp",  null,  "wq",  "wn",  "wb",  "wk",  null},
                                {"wr",  null,  null,  null,  null,  "wr",  null,  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        Set<Move> moves = chess.generateKingMoves(new Position(1, 6));
        for (Move move : moves) {
            System.out.println(chess.getBoard());
            chess.executeMove(move.getStart(), move.getEnd());
            System.out.println(chess.getBoard());
            chess.undoMove(move);
        }
    }

    @Test
    public void testGenerateQueenMoves() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                {null,  "bp",  null,  null,  "bn",  "bn",  null,  "bp"},
                {null,  null,  null,  "bp",  null,  null,  null,  null},
                {null,  null,  null,  "wp",  null,  null,  null,  null},
                {"wp",  null,  null,  "wb",  null,  "wp",  null,  null},
                {null,  "wp",  null,  "wq",  "wn",  "wb",  "wk",  null},
                {"wr",  null,  null,  null,  null,  "wr",  null,  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        Set<Move> moves = chess.generateQueenMoves(new Position(1, 3));
        assert(moves.size() == 11);
        for (Move move : moves) {
            chess.executeMove(move);
            System.out.println(chess.getBoard());
            chess.undoMove(move);
            assert(chess.verifyBoard());
        }
    }

    @Test
    public void testGenerateRookMoves() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                {null,  "bp",  null,  null,  "bn",  "bn",  null,  "bp"},
                {null,  null,  null,  "bp",  null,  null,  null,  null},
                {null,  null,  null,  "wp",  null,  null,  null,  null},
                {"wp",  null,  null,  "wb",  null,  "wp",  null,  null},
                {null,  "wr",  null,  "wq",  "wn",  "wb",  "wk",  null},
                {"wr",  null,  null,  null,  null,  "wr",  null,  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        Set<Move> moves = chess.generateRookMoves(new Position(1, 1));
        assert(moves.size() == 7);
        for (Move move : moves) {
            chess.executeMove(move);
            System.out.println(chess.getBoard());
            chess.undoMove(move);
            assert(chess.verifyBoard());
        }
        String[][] test2 =   {{"br", "bn", "bb", "bq", "bk", "bb", "bn", "br"},
                                {"bp", "bp", "bp", "bp", "bp", "bp", "bp", null,},
                                {null, null, null, null, null, null, null, null,},
                                {null, null, null, null, null, null, null, "bp",},
                                {"wp", null, null, null, null, null, "wp", null,},
                                {null, null, null, null, null, null, null, null,},
                                {null, "wp", "wp", "wp", "wp", "wp", null, "wp",},
                                {"wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr",}};
        chess = new ChessBoard(test2);
        moves = chess.generateRookMoves(new Position(7, 7));
        for (Move move : moves) {
            chess.executeMove(move);
            System.out.println(chess.getBoard());
            chess.undoMove(move);
        }

    }

    @Test
    public void testGenerateKnightMoves() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                {null,  "bp",  null,  null,  "bn",  "bn",  null,  "bp"},
                {null,  null,  null,  "bp",  null,  null,  null,  null},
                {null,  null,  null,  "wp",  null,  null,  null,  null},
                {"wp",  null,  "bn",  "wb",  null,  "wp",  null,  null},
                {null,  "wr",  null,  "wq",  "wn",  "wb",  "wk",  null},
                {"wr",  null,  null,  null,  null,  "wr",  null,  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        Set<Move> moves = chess.generateKnightMoves(new Position(2, 2));
        assert(moves.size() == 7);
        for (Move move : moves) {
            chess.executeMove(move);
            System.out.println(chess.getBoard());
            chess.undoMove(move);
            assert(chess.verifyBoard());
        }
    }

    @Test
    public void testGeneratePawnMoves() {
        String[][] testBoard = {{null,  null,  "br",  "bq",  "br",  null,  "bk",  null},
                                {"bp",  "bb",  null,  null,  null,  "bp",  "bp",  null},
                                {null,  "bp",  null,  null,  "bn",  "bn",  null,  "wp"},
                                {null,  null,  null,  "bp",  null,  null,  null,  null},
                                {null,  null,  null,  "wp",  null,  null,  null,  null},
                                {"wp",  null,  "bn",  "wb",  null,  "wp",  null,  null},
                                {null,  "wr",  null,  "wq",  "wn",  "wb",  "wk",  null},
                                {"wr",  null,  null,  null,  null,  "wr",  null,  null}};
        ChessBoard chess = new ChessBoard(testBoard);
        Set<Move> moves = chess.generatePawnMoves(new Position(6, 6));
        assert(moves.size() == 3);
        for (Move move : moves) {
            chess.executeMove(move);
            System.out.println(chess.getBoard());
            chess.undoMove(move);
            assert(chess.verifyBoard());
        }
    }

    @Test
    public void testTotalMovesInitSetup() {
        ChessBoard chess = new ChessBoard();
        System.out.println(movesByDepth(5, true, chess));
    }

    @Test
    public void testCheckMate() {
        String[][] board = {{"br", "bn", "bb", "bq", "bk", "bb", "bn", "br"},
                            {"bp", "bp", "bp", "bp", "bp", null, "bp", "bp"},
                            {null, null, null, null, null, null, null, null},
                            {null, null, null, null, null, null, null, null},
                            {null, null, "wb", null, null, null, null, null},
                            {null, null, null, null, "wp", "wq", null, null},
                            {"wp", "wp", "wp", "wp", null, "wp", "wp", "wp"},
                            {"wr", "wn", "wb", null, "wk", null, "wn", "wr"}};
        ChessBoard chess = new ChessBoard(board);
        Position p1 = new Position(3, 2);
        Position p2 = new Position(6, 5);
        chess.movePiece(new Move(p1, p2, chess.pieceAt(p1), chess.pieceAt(p2), false));
        assert(chess.isOver());
    }

    private int movesByDepth(int depth, boolean isWhite, ChessBoard board) {
        if (depth == 0 || board.isOver()) {
            return 1;
        }
        int numPositions = 0;
        Set<Move> moves = board.generateMoves(isWhite);
        for (Move m : moves) {
            board.executeMove(m);
            numPositions += movesByDepth(depth-1, !isWhite, board);
            board.undoMove(m);
        }
        return numPositions;
    }
}
