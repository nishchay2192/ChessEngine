package chess;

public class Knight extends Piece {
	public Knight(Square location, Color color, PieceType pieceType) {
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
		if ((Math.abs(destX - X) == 2 && Math.abs(destY - Y) == 1)
				|| (Math.abs(destX - X) == 1 && Math.abs(destY - Y) == 2)) {
			return MoveType.NORMAL;
		} else {
			return MoveType.ILLEGAL;
		}
	}

}
