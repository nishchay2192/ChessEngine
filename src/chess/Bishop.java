package chess;

public class Bishop extends Piece {
	public Bishop(Square location, Color color, PieceType pieceType) {
		super(location, color, pieceType);

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
		if (Math.abs(destX - X) == Math.abs(destY - Y)) {
			int diffX = destX - X;
			diffX = diffX/Math.abs(diffX);
			int diffY = destY - Y;
			diffY = diffY/Math.abs(diffY);
			int currX = X;
			int currY = Y;
			while (currX != destX && currY != destY) {
				currX = X + diffX;
				currY = Y + diffY;
				if (currX != destX && currY != destY) {
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
