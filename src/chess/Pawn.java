package chess;

public class Pawn extends Piece {

	public Pawn(Square location, Color color, PieceType pieceType) {
		super(location, color, pieceType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MoveType canMove(Square dest) {
		if (basicCheck(dest) == MoveType.ILLEGAL)
			return MoveType.ILLEGAL;
		// System.out.println("Pawn Move passed BasicCheck");
		int X = this.getSquare().getX();
		int Y = this.getSquare().getY();
		int destX = dest.getX();
		int destY = dest.getY();
		// System.out.println(X+" "+Y+" "+destX+" "+destY);
		if (this.getColor() == Color.WHITE) {
			if (destX - X == 1 && destY == Y) {
				// System.out.println("Pawn seems to make a normal move.");
				return MoveType.NORMAL;
			} else if (destX - X == 2 && destY == Y && !this.moved) {
				// System.out.println("Pawn seems to make a doublestep move.");
				return MoveType.DOUBLESTEP;
			} else if (destX - X == 1 && (destY - Y == 1 || destY - Y == -1)) {
				if (dest.getPiece() != null) {
					// System.out.println("Pawn seems to make a kill move.");
					return MoveType.NORMAL;
				} else {
					// System.out.println("Pawn seems to make an enpassant move.");
					return MoveType.ENPASSANT;
				}
			} else {
				// System.out.println("Pawn seems to make an illegal move.");
				return MoveType.ILLEGAL;
			}

		} else {
			if (destX - X == -1 && destY == Y) {

				// System.out.println("Pawn seems to make a normal move.");
				return MoveType.NORMAL;
			} else if (destX - X == -2 && destY == Y && !this.moved) {

				// System.out.println("Pawn seems to make a doublestep move.");
				return MoveType.DOUBLESTEP;
			} else if (destX - X == -1 && (destY - Y == 1 || destY - Y == -1)) {
				if (dest.getPiece() != null) {

					// System.out.println("Pawn seems to make a kill move.");
					return MoveType.NORMAL;
				} else {

					// System.out.println("Pawn seems to make an enpassant move.");
					return MoveType.ENPASSANT;
				}
			} else {

				// System.out.println("Pawn seems to make an illegal move.");
				return MoveType.ILLEGAL;
			}
		}
	}
}
