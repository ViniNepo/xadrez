package application;

import chess.ChessExecpiton;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Program {

    public static void main(String [] args) {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        List<ChessPiece> captured = new ArrayList<>();

        while (!chessMatch.getCheckMate()){
            try {
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);
                System.out.println();
                System.out.print("initial position: ");
                ChessPosition initial = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(initial);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.println();
                System.out.print("target: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturePiece = chessMatch.performChessMove(initial, target);

                if (capturePiece != null)
                    captured.add(capturePiece);

                if (chessMatch.getPromoted() != null) {
                    System.out.print("Enter piece for promotion (B/N/R/Q): ");
                    String type = sc.nextLine();
                    chessMatch.replacePromotedPiece(type);
                }
            } catch (ChessExecpiton | InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }

        }

        UI.clearScreen();
        UI.printMatch(chessMatch, captured);


    }
}
