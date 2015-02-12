package chess;

import java.util.Vector;

public abstract class Piece {
	private Color color;
	private Square location;
	private PieceType pieceType;
	private boolean isAlive;
	protected boolean moved = false;

	public Piece(Square location, Color color, PieceType pieceType) {
		this.color = color;
		this.location = location;
		this.pieceType = pieceType;
		this.isAlive = true;
	}
	public void setAlive(){
		this.isAlive = true;
	}
	public void kill() {
		this.isAlive = false;
	}

	public boolean isAlive() {
		return this.isAlive;
	}

	public Square getSquare() {
		return location;
	}

	public Color getColor() {
		return color;
	}

	public PieceType getPieceType() {
		return pieceType;
	}

	public void setPieceType(PieceType pieceType) {
		this.pieceType = pieceType;
	}

	public void setSquare(Square square) {
		this.location = square;
	}

	public Vector<Square> isAttackedBy(Square square, boolean whiteAttacked) {
		Board board = square.board;
		Vector<Square> attackers = new Vector<Square>();
		int X = square.getX();
		int Y = square.getY();
		// attack by Pawn
		Color attackingColor;
		boolean attackedByWhite = false;
		if (whiteAttacked) {
			attackedByWhite = true;
			attackingColor = Color.WHITE;
		} else {
			attackingColor = Color.BLACK;
		}
		if (attackedByWhite) {
			Piece left = board.the_board[X - 1][Y - 1].getPiece();
			if ((X > 1 && Y > 0 && left != null
					&& left.getPieceType() == PieceType.PAWN && left.getColor() == Color.WHITE))
				attackers.add(left.getSquare());
			Piece right = board.the_board[X - 1][Y + 1].getPiece();
			if ((X > 1 && Y < 7 && right != null
					&& right.getPieceType() == PieceType.PAWN && right
					.getColor() == Color.WHITE))
				attackers.add(right.getSquare());
		} else {
			Piece left = board.the_board[X + 1][Y - 1].getPiece();
			if ((X < 6 && Y > 0 && left != null
					&& left.getPieceType() == PieceType.PAWN && left.getColor() == Color.BLACK))
				attackers.add(left.getSquare());
			Piece right = board.the_board[X + 1][Y + 1].getPiece();
			if ((X < 6 && Y < 7 && right != null
					&& right.getPieceType() == PieceType.PAWN && right
					.getColor() == Color.BLACK))
				attackers.add(right.getSquare());
		}
		// attack by Knight or King
		int dirKnight[][] = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 },
				{ 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 } };
		int dirKing[][] = { { 1, 0 }, { 1, 1 }, { 1, -1 }, { -1, 0 },
				{ -1, 1 }, { -1, -1 }, { 0, 1 }, { 0, -1 } };
		int checkTurn[][][] = { dirKnight, dirKing };
		PieceType knightOrKing;
		for (int dir[][] : checkTurn) {
			for (int diff[] : dir) {
				if (Math.abs(diff[0]) + Math.abs(diff[1]) == 3)
					knightOrKing = PieceType.KNIGHT;
				else
					knightOrKing = PieceType.KING;
				if (X + diff[0] <= 7 && X + diff[0] >= 0 && Y + diff[1] <= 7
						&& Y + diff[1] >= 0) {
					Piece attacker = board.the_board[X + diff[0]][Y + diff[1]]
							.getPiece();
					if (attacker != null
							&& attacker.getPieceType() == knightOrKing
							&& attacker.getColor() == attackingColor) {
						attackers.add(attacker.getSquare());
					}
				}
			}
		}
		// attack by Queen and Rook on vertical and horizontal profile;
		int dirRook[][] = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
		int dirBishop[][] = { { 1, 1 }, { -1, -1 }, { -1, 1 }, { 1, -1 } };
		int turnCheck[][][] = { dirRook, dirBishop };
		PieceType rookOrBishop;
		for (int diffUp[][] : turnCheck) {
			for (int diff[] : diffUp) {
				if (Math.abs(diff[0]) + Math.abs(diff[1]) == 1)
					rookOrBishop = PieceType.ROOK;
				else
					rookOrBishop = PieceType.BISHOP;
				boolean carryOn = true;
				int addX = diff[0];
				int addY = diff[1];
				while (carryOn) {
					if (X + addX <= 7 && X + addX >= 0 && Y + addY <= 7
							&& Y + addY >= 0) {
						Piece attacker = board.the_board[X + diff[0]][Y
								+ diff[1]].getPiece();
						if (attacker == null) {
							addX += diff[0];
							addY += diff[1];
							continue;
						} else {
							carryOn = false;
						}
						if (attacker != null
								&& (attacker.getPieceType() == rookOrBishop || attacker
										.getPieceType() == PieceType.QUEEN)
								&& attacker.getColor() == attackingColor) {
							attackers.add(attacker.getSquare());
						}
					} else {
						carryOn = false;
					}
				}
			}
		}
		return attackers;
	}

	public abstract MoveType canMove(Square dest);

	public MoveType basicCheck(Square dest) {
		if (dest == location) {
			// System.out.println("Return Illegal move 0");
			return MoveType.ILLEGAL;
		}
		if (location.board == null)
			// System.out.println("Done");
			if (location.board.getTurn() != color) {
				// System.out.println("Return Illegal move 1");
				return MoveType.ILLEGAL;
			}
		if (dest.getPiece() != null && dest.getPiece().getColor() == color) {
			// System.out.println("Return Illegal move 3");
			return MoveType.ILLEGAL;
		}
		// System.out.println(this.getSquare().getX() + " "
		// + this.getSquare().getY());
		Square from = location;
		from.setPiece(null);
		Piece destPiece = dest.getPiece();
		dest.setPiece(this);
		if (destPiece != null)
			destPiece.setSquare(null);
		this.setSquare(dest);

		boolean whiteAttacked = false;
		if (color == Color.BLACK) {
			whiteAttacked = true;
		}
		if (isAttackedBy(dest.board.getKingSquare(color), whiteAttacked).size() > 0) {
			System.out.println("King is attacked! Illelegal");

			// restore
			from.setPiece(this);
			dest.setPiece(destPiece);
			if (destPiece != null)
				destPiece.setSquare(dest);
			this.setSquare(from);
			return MoveType.ILLEGAL;
		}
		// System.out.println("King safe so far");
		// restore
		from.setPiece(this);
		dest.setPiece(destPiece);
		if (destPiece != null)
			destPiece.setSquare(dest);
		this.setSquare(from);
		// System.out.println(this.getSquare().getX() + " "
		// + this.getSquare().getY());
		return MoveType.NORMAL;
	}

	public MoveType tryToMove(Square dest) {
		MoveType canMoveType = canMove(dest);
		if (canMoveType != MoveType.ILLEGAL) {
			if (canMoveType == MoveType.ENPASSANT) {
				if (dest.board.getEnpassant() == dest) {
					dest.board.move(dest, this.getSquare());
					return MoveType.ENPASSANT;
				} else {
					return MoveType.ILLEGAL;
				}
			} else if (canMoveType == MoveType.DOUBLESTEP) {
				if (this.color == Color.WHITE)
					dest.board.move(dest, this.getSquare(),
							dest.board.the_board[dest.getX() - 1][dest.getY()]);
				else {
					dest.board.move(dest, this.getSquare(),
							dest.board.the_board[dest.getX() + 1][dest.getY()]);
				}
				return MoveType.DOUBLESTEP;
			} else if (canMoveType == MoveType.CASTLE) {
				dest.board.move(dest, location);
				int row = 7;
				if (this.color == Color.WHITE) {
					row = 0;
				}
				if (dest.getY() < location.getY()) {
					dest.board.move(dest.board.the_board[row][3],
							dest.board.the_board[row][0]);
				} else {
					dest.board.move(dest.board.the_board[row][5],
							dest.board.the_board[row][7]);
				}
				return MoveType.CASTLE;
			} else if (canMoveType == MoveType.UPGRADE) {
				dest.board.move(dest, location);
				return MoveType.UPGRADE;
			} else {
				dest.board.move(dest, location);
				return MoveType.NORMAL;
			}
		} else {
			return MoveType.ILLEGAL;
		}
	}
}
