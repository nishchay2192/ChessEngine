package chess;

public class King extends Piece {
	public King(Square location, Color color, PieceType pieceType) {
		super(location, color, pieceType);
		// TODO Auto-generated constructor stub
	}

	@Override
	public MoveType canMove(Square dest) {
		// normal case
		MoveType moveType = basicCheck(dest);
		if (moveType == MoveType.ILLEGAL) {
			return moveType;
		} else {
			int destX = dest.getX();
			int destY = dest.getY();
			int X = this.getSquare().getX();
			int Y = this.getSquare().getY();

			if ((Math.abs(destX - X) == 1 || Math.abs(destY - Y) == 0)
					&& (Math.abs(destY - Y) == 1 || Math.abs(destY - Y) == 0)) {
				return MoveType.NORMAL;
			} else if (Math.abs(destY - Y) == 2 && destX == X
					&& this.getColor() == Color.WHITE && !this.moved
					&& isAttackedBy(this.getSquare(), false).size() == 0
					&& isAttackedBy(dest, false).size() == 0) {
				if (destY > Y) {
					Board board = this.getSquare().board;
					if (!board.the_board[0][7].getPiece().moved
							&& board.the_board[0][6].getPiece() == null
							&& board.the_board[0][5].getPiece() == null) {
						return MoveType.CASTLE;
					} else {
						return MoveType.ILLEGAL;
					}
				} else {
					Board board = this.getSquare().board;
					if (!board.the_board[0][0].getPiece().moved
							&& board.the_board[0][1].getPiece() == null
							&& board.the_board[0][2].getPiece() == null
							&& board.the_board[0][3].getPiece() == null) {
						return MoveType.CASTLE;
					} else {
						return MoveType.ILLEGAL;
					}
				}
			} else if (Math.abs(destY - Y) == 2 && destX == X
					&& this.getColor() == Color.BLACK && !this.moved
					&& isAttackedBy(this.getSquare(), true).size() == 0
					&& isAttackedBy(dest, true).size() == 0) {
				if (destY > Y) {
					Board board = this.getSquare().board;
					if (!board.the_board[7][7].getPiece().moved
							&& board.the_board[7][6].getPiece() == null
							&& board.the_board[7][5].getPiece() == null) {
						return MoveType.CASTLE;
					} else {
						return MoveType.ILLEGAL;
					}
				} else {
					Board board = this.getSquare().board;
					if (!board.the_board[7][0].getPiece().moved
							&& board.the_board[7][1].getPiece() == null
							&& board.the_board[7][2].getPiece() == null
							&& board.the_board[7][3].getPiece() == null) {
						return MoveType.CASTLE;
					} else {
						return MoveType.ILLEGAL;
					}
				}
			} else {
				return MoveType.ILLEGAL;
			}
		}
	}
}
