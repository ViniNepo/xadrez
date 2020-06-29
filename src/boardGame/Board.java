package boardGame;

import com.sun.xml.internal.stream.events.ProcessingInstructionEvent;
import javafx.geometry.Pos;

public class Board {

    private int rows;
    private int columns;
    private Piece[][] pieces;

    public Board(int rows, int columns) {
        if(rows < 1 && columns < 1){
            throw new BoardExeption("Error crating board: pelo menos uma linha e uma coluna");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Piece piece(int row, int column) {
        if(!positionExist(row, column))
        throw new BoardExeption("Position not on the board");

        return pieces[row][column];
    }

    public Piece piece(Position position) {
        if(!positionExist(position))
            throw new BoardExeption("Position not on the board");

        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position){
        if(thereIsAPiece(position))
            throw new BoardExeption("There is already a piece on position" + position);

        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    private Boolean positionExist(int row, int column){
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }

    public Boolean positionExist(Position position) {
        return positionExist(position.getRow(), position.getColumn());
    }

    public Boolean thereIsAPiece(Position position){
        if(!positionExist(position))
            throw new BoardExeption("Position not on the board");

        return piece(position) != null;
    }

    public Piece removePiece(Position position){
        if(!positionExist(position))
            throw new BoardExeption("Position not on the board");

        if(piece(position) == null)
            return null;

        Piece aux = piece(position);
        aux.position = null;

        pieces[position.getRow()][position.getColumn()] = null;
        return aux;
    }
}
