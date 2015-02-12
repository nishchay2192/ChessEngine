package chess;

public class Queen extends Piece {
	public Queen(Square location, Color color, PieceType pieceType) {
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
		if ((Math.abs(destX - X) == 0 || Math.abs(destY - Y) == 0)
				|| (Math.abs(destX - X) == Math.abs(destY - Y))) {
			int diffX = destX - X;
			if (diffX != 0)
				diffX = diffX / Math.abs(diffX);
			int diffY = destY - Y;
			if (diffY != 0)
				diffY = diffY / Math.abs(diffY);
			int currX = X;
			int currY = Y;
			while (currX != destX || currY != destY) {
				currX = currX + diffX;
				currY = currY + diffY;
				if (currX != destX || currY != destY) {
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
