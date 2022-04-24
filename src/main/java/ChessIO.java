import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ChessIO {
    static ChessBoard game;

    public static void main(String[] args) {
        String[][] board = {{"br", "bn", "bb", null, "bk", "bb", "bn", "br"},
                            {"bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp"},
                            {null, null, null, null, null, "bq", null, null},
                            {null, null, null, null, null, null, null, null},
                            {null, null, null, null, null, null, null, "bb"},
                            {null, null, null, null, null, null, null, null},
                            {"wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"},
                            {"wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr"}};
        game = new ChessBoard(board);
        playGame();
    }

    private static void botVbot() {
        Set<Double> mmTimes = new HashSet<>();
        Set<Double> abTimes = new HashSet<>();
        int i =0;
        while(!game.isOver() && i++ < 30) {
            if (game.whiteToMove()) {
                game.movePiece(game.alphaBetaMove(null, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true));
            } else {
                double start = System.nanoTime();
                Move abMove = game.alphaBetaMove(null, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                double abTime = (System.nanoTime() - start) / 1_000_000_000d;
                abTimes.add(abTime);
                start = System.nanoTime();
                Move mmMove = game.miniMaxMove(null, 5, false);
                double mmTime = (System.nanoTime() - start) / 1_000_000_000d;
                mmTimes.add(mmTime);
                System.out.println("AB: " + abMove.getGuaranteedScore() + " MM: " + mmMove.getGuaranteedScore());
                game.movePiece(abMove);
            }
        }
        System.out.println("MM avg time: " + average(mmTimes));
        System.out.println("AB avg time: " + average(abTimes));

    }

    private static double average(Set<Double> times) {
        double sum = 0;
        for (double val : times) {
            sum += val;
        }
        return sum / times.size();
    }

    private static void playGame() {
        Scanner s = new Scanner(System.in);
        while(!game.isOver()) {
            if (game.whiteToMove()) {
                String turn = (game.whiteToMove()) ? "white" : "black";
                System.out.println(turn + " to move");
                System.out.println(game.getBoard());
                char[] move = s.next().toCharArray();
                boolean validMove;
                try {
                    if (move[1] == 'q' || move[1] == 'k') {
                        boolean queen = move[1] == 'q';
                        Move castle = game.castle(game.whiteToMove(), queen);
                        validMove = castle != null;
                    } else {
                        Position p1 = new Position(move[0], Character.getNumericValue(move[1]));
                        Position p2 = new Position(move[2], Character.getNumericValue(move[3]));
                        validMove = game.movePiece(p1, p2);
                    }
                    if (!validMove) {
                        System.out.println("Invalid move");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid move");
                }
            } else {
                double start = System.nanoTime();
                Move abMove = game.alphaBetaMove(null, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                double abTime = (System.nanoTime() - start) / 1_000_000_000d;
                start = System.nanoTime();
                game.miniMaxMove(null, 5, false);
                double mmTime = (System.nanoTime() - start) / 1_000_000_000d;
                game.movePiece(abMove);
                System.out.print(abMove.getStart().toString() + abMove.getEnd() + " AB Time: ");
                System.out.printf("%4.3f", abTime);
                System.out.print(" MM Time: ");
                System.out.printf("%4.3f", mmTime);
                System.out.println();
            }
        }
        System.out.println(game.getBoard());
        String winner = (game.whiteToMove()) ? "Black" : "White";
        System.out.println("Game over: " + winner + " wins!");
    }
}
