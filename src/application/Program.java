package application;

import chess.ChessExecpiton;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Program {

    public static void main(String [] args) {
        Scanner sc = new Scanner(System.in);

        ChessMatch chessMatch = new ChessMatch();
        while (true){
            try {
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces());
                System.out.println();
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
            } catch (ChessExecpiton | InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }

        }


    }
}
