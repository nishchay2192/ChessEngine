package chess;

public class Rook extends Piece {
	public Rook(Square location, Color color, PieceType pieceType) {
		super(location, color, pieceType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MoveType canMove(Square dest) {
		MoveType moveType = basicCheck(dest);
		if (moveType == MoveType.ILLEGAL)
			return moveType;
		int X = this.getSquare().getX();
		int Y = this.getSquare().getY();
		int destX = dest.getX();
		int destY = dest.getY();
		if ((Math.abs(destX - X) == 0 || Math.abs(destY - Y) == 0)) {
			int diffX = destX - X;
			int diffY = destY - Y;
			int currX = X;
			int currY = Y;
			while (currX != destX) {
				currX = X + diffX;
				currY = Y + diffY;
				if (currX != destX) {
					if (dest.board.the_board[currX][currY].getPiece() == null) {
						continue;
					} else {
						return MoveType.ILLEGAL;
					}
				} else {
					return MoveType.NORMAL;
				}
			}
		} else {
			return MoveType.ILLEGAL;
		}
		return null;
	}

}
