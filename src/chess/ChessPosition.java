package chess;

import boardGame.Position;

public class ChessPosition extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public char column;
    public int row;

    public ChessPosition(char column, int row) {
        if(column < 'a' || column > 'h' || row < 1 || row > 8)
            throw new ChessExecpiton("Error instantiating ChessPosition");
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    protected Position toPosition() {
        return new Position(8 - row, column - 'a');
    }

    protected static ChessPosition fromPosition(Position position) {
        return new ChessPosition((char)('a' + position.getColumn()), 8 - position.getRow());
    }

    @Override
    public String toString() {
        return "" + column + row;
    }
}
