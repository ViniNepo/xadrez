package chess;

import boardGame.Board;
import boardGame.BoardExeption;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch {

    private Board board;
    private int turn;
    private Color currentPlayer;
    private boolean check;
    private boolean checkMate;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public int getTurn() {
        return turn;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

        for(int i = 0; i < board.getRows(); i++){
            for(int j = 0; j < board.getColumns(); j++){
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    public boolean[][] possibleMoves(ChessPosition initialPosition) {
        Position position = initialPosition.toPosition();
        validateInitialPosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition initial, ChessPosition target) {
        Position initialPosition = initial.toPosition();
        Position targetPosition = target.toPosition();

        validateInitialPosition(initialPosition);
        validateTargetPosition(initialPosition, targetPosition);

        Piece capturePiece = makeMove(initialPosition, targetPosition);
        if (testCheck(currentPlayer)) {
            undoMove(initialPosition, targetPosition, capturePiece);
            throw new ChessExecpiton("You can't put yourself in check");
        }
        check = testCheck(opponent(currentPlayer)) ? true : false;

        if(testeCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn();
        }

        return (ChessPiece)capturePiece;
    }

    private Piece makeMove(Position initial, Position target){
        ChessPiece p = (ChessPiece)board.removePiece(initial);
        p.increaseMoveCount();
        Piece capturePiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturePiece != null) {
            piecesOnTheBoard.remove(capturePiece);
            capturedPieces.add(capturePiece);
        }
        return  capturePiece;
    }

    private void undoMove(Position initialPosition, Position targetPosition, Piece capturedPiece) {
        ChessPiece p = (ChessPiece)board.removePiece(targetPosition);
        p.decreaseMoveCount();
        board.placePiece(p, initialPosition);

        if(capturedPiece != null) {
            board.placePiece(capturedPiece, targetPosition);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    private void validateTargetPosition(Position initial, Position target) {
        if(!board.piece(initial).possibleMove(target))
            throw new ChessExecpiton("The chosen piece cant't move to target");
    }

    private void validateInitialPosition(Position position) {
        if(!board.thereIsAPiece(position))
            throw new BoardExeption("There is no piece on initial position");
        if (currentPlayer != ((ChessPiece)board.piece(position)).getColor())
            throw new ChessExecpiton("The chosen piece is not yours");
        if(!board.piece(position).isTherePossibleMove())
            throw new ChessExecpiton("There in no moves for chosen piece");
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color opponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece)p;
            }
        }
        throw new IllegalStateException("There is no " + color + "king on the board");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()])
                return true;
        }
        return false;
    }

    private boolean testeCheckMate(Color color) {
        if(!testCheck(color))
            return false;

        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i< board.getRows(); i++) {
                for (int j = 0; j< board.getColumns(); j++) {
                    if(mat[i][j]) {
                        Position initial = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturePiece = makeMove(initial, target);
                        boolean testCheck = testCheck(color);
                        undoMove(initial, target, capturePiece);
                        if(!testCheck)
                            return false;
                    }
                }
            }
        }
        return  true;
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK));

    }
}
