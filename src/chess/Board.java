package chess;

public class Board {
	static final int WIDTH = 8, HEIGHT = 8;
	private Color turn;
	private History last;
	private History first;
	private Square enpassant;
	private Piece blackKing, whiteKing;
	boolean blackCanCastle = true, whiteCanCastle = true;
	Square[][] the_board = new Square[WIDTH][HEIGHT];

	public Board() {
		for (int i = 0; i < the_board.length; i++) {
			for (int j = 0; j < the_board.length; j++) {
				the_board[i][j] = new Square(i, j, this);
				the_board[i][j].setPiece(null);
			}
		}
		for (int i = 0; i < the_board.length; i++) {
			the_board[1][i].setPiece(new Pawn(the_board[1][i], Color.WHITE,
					PieceType.PAWN));
			the_board[6][i].setPiece(new Pawn(the_board[6][i], Color.BLACK,
					PieceType.PAWN));
		}
		the_board[0][0].setPiece(new Rook(the_board[0][0], Color.WHITE,
				PieceType.ROOK));
		the_board[0][7].setPiece(new Rook(the_board[0][7], Color.WHITE,
				PieceType.ROOK));
		the_board[7][0].setPiece(new Rook(the_board[7][0], Color.BLACK,
				PieceType.ROOK));
		the_board[7][7].setPiece(new Rook(the_board[7][7], Color.BLACK,
				PieceType.ROOK));
		the_board[0][1].setPiece(new Knight(the_board[0][1], Color.WHITE,
				PieceType.KNIGHT));
		the_board[0][6].setPiece(new Knight(the_board[0][6], Color.WHITE,
				PieceType.KNIGHT));
		the_board[7][1].setPiece(new Knight(the_board[7][1], Color.BLACK,
				PieceType.KNIGHT));
		the_board[7][6].setPiece(new Knight(the_board[7][6], Color.BLACK,
				PieceType.KNIGHT));
		the_board[0][2].setPiece(new Bishop(the_board[0][2], Color.WHITE,
				PieceType.BISHOP));
		the_board[0][5].setPiece(new Bishop(the_board[0][5], Color.WHITE,
				PieceType.BISHOP));
		the_board[7][2].setPiece(new Bishop(the_board[7][2], Color.BLACK,
				PieceType.BISHOP));
		the_board[7][5].setPiece(new Bishop(the_board[7][5], Color.BLACK,
				PieceType.BISHOP));
		the_board[0][3].setPiece(new Queen(the_board[0][3], Color.WHITE,
				PieceType.QUEEN));
		the_board[7][3].setPiece(new Queen(the_board[7][3], Color.BLACK,
				PieceType.QUEEN));

		the_board[0][4].setPiece(new King(the_board[0][4], Color.WHITE,
				PieceType.KING));
		the_board[7][4].setPiece(new King(the_board[7][4], Color.BLACK,
				PieceType.KING));
		turn = Color.WHITE;
		enpassant = null;
		first = null;
		last = null;
		blackKing = the_board[7][4].getPiece();
		whiteKing = the_board[0][4].getPiece();
	}

	public Color getTurn() {
		return turn;
	}

	public Square getEnpassant() {
		return enpassant;
	}

	public Square getKingSquare(Color color) {
		if (color == Color.BLACK)
			return blackKing.getSquare();
		else
			return whiteKing.getSquare();
	}

	public void move(Square to, Square from, Square enpassant) {
		this.enpassant = enpassant;
		Piece piece = from.getPiece();
		Piece toKill = to.getPiece();

		to.setPiece(piece);
		piece.setSquare(to);
		from.setPiece(null);
		if (first == null) {
			first = new History(to, from, toKill);
			last = first;
		} else {
			History temp = new History(to, from, toKill);
			last.setNext(temp);
			last = temp;
		}
		if (piece.moved == false) {
			piece.moved = true;
			last.setFirstMove(true);
		}
	}

	public void move(Square to, Square from) {
		move(to, from, null);
	}

	public void upgradePawn(Piece piece, char upgrade) {
		Piece toBeUpgraded;
		switch (upgrade) {
		case 'Q':
			toBeUpgraded = new Queen(piece.getSquare(), piece.getColor(),
					PieceType.QUEEN);
			break;
		case 'R':
			toBeUpgraded = new Rook(piece.getSquare(), piece.getColor(),
					PieceType.ROOK);
			break;
		case 'N':
			toBeUpgraded = new Knight(piece.getSquare(), piece.getColor(),
					PieceType.KNIGHT);
			break;
		case 'B':
			toBeUpgraded = new Bishop(piece.getSquare(), piece.getColor(),
					PieceType.BISHOP);
			break;
		default: // This is Redundant
			toBeUpgraded = new Queen(piece.getSquare(), piece.getColor(),
					PieceType.QUEEN);
			break;
		}
		// Upgrade on Board
		upgradePawn(piece, toBeUpgraded);
	}

	private void upgradePawn(Piece piece, Piece toBeUpgraded) {
		last.setUpgradedTo(toBeUpgraded);
		piece.getSquare().setPiece(toBeUpgraded);
		toBeUpgraded.setSquare(piece.getSquare());
		piece.kill();
		piece.setSquare(null);
		piece = toBeUpgraded;
	}

	public void redoMove() {
		if (last.getNext() != null) {
			tryToMove(last.getNext().getTo(), last.getNext().getFrom());
			if (last.getUpgradedTo() != null) {
				upgradePawn(last.getTo().getPiece(), last.getUpgradedTo());
			}
		}
	}

	public void undoMove() {
		if (last.isFirstMove()) {
			last.getTo().getPiece().moved = false;
		}
		Piece piece = last.getTo().getPiece();
		Piece killed = last.getKilled();

		if (last.getMoveType() == MoveType.NORMAL
				|| last.getMoveType() == MoveType.DOUBLESTEP) {
			if (killed != null) {
				killed.setAlive();
				killed.setSquare(last.getTo());
			}
			last.getTo().setPiece(killed);
			last.getFrom().setPiece(piece);
			piece.setSquare(last.getFrom());
			last = last.getPrevious();
		} else if (last.getMoveType() == MoveType.ENPASSANT) {
			piece.setSquare(last.getFrom());
			last.getFrom().setPiece(piece);
			last.getTo().setPiece(null);
			int X = last.getTo().getX();
			int Y = last.getTo().getY();
			if (killed.getColor() == Color.WHITE) {
				killed.setAlive();
				killed.setSquare(the_board[X + 1][Y]);
				the_board[X + 1][Y].setPiece(killed);
			} else {
				killed.setAlive();
				killed.setSquare(the_board[X - 1][Y]);
				the_board[X - 1][Y].setPiece(killed);
			}
			this.enpassant = last.getTo();
			last = last.getPrevious();
		} else if (last.getMoveType() == MoveType.CASTLE) {
			int fromY = last.getFrom().getY();
			int fromX = last.getFrom().getX();
			int toY = last.getTo().getY();
			int toX = last.getTo().getX();
			if (toY < fromY) {
				Piece rook = the_board[toX][toY + 1].getPiece();
				the_board[toX][toY + 1].setPiece(null);
				rook.setSquare(the_board[toX][0]);
				the_board[toX][0].setPiece(rook);
				rook.moved = false;
			} else {
				Piece rook = the_board[toX][toY - 1].getPiece();
				the_board[toX][toY - 1].setPiece(null);
				rook.setSquare(the_board[toX][7]);
				the_board[toX][7].setPiece(rook);
				rook.moved = false;
			}
			piece.setSquare(last.getFrom());
			last.getFrom().setPiece(piece);
			last.getTo().setPiece(null);
			last = last.getPrevious();
		} else if (last.getMoveType() == MoveType.UPGRADE) {
			Piece pawn = new Pawn(last.getFrom(), piece.getColor(),
					PieceType.PAWN);
			last.getFrom().setPiece(pawn);
			last.getTo().setPiece(killed);
			killed.setSquare(last.getTo());
			piece.kill();
			piece.setSquare(null);
			last = last.getPrevious();
		}
		turn = turn == Color.WHITE ? Color.BLACK : Color.WHITE;
	}

	public MateType canKingMove(Square king) {

		return MateType.NOTYETMATE;
	}

	public MateType checkIfMate(Color color) {
		Square king = getKingSquare(color);
		MateType mateType = canKingMove(king);
		return mateType;
	}

	public MoveType tryToMove(Square to, Square from) {
		Piece piece = from.getPiece();
		// System.out.println("Entered Board");
		if (piece == null) {
			System.out.println("MoveType is: " + MoveType.ILLEGAL);
			return MoveType.ILLEGAL;
		} else if (piece.getColor() != turn) {
			System.out.println("MoveType is: " + MoveType.ILLEGAL);
			return MoveType.ILLEGAL;
		}
		System.out.println("TryingToMove: " + piece.getPieceType());
		MoveType moveType = piece.tryToMove(to);
		System.out.println("MoveType is: " + moveType);
		if (moveType == MoveType.ILLEGAL) {
			return MoveType.ILLEGAL;
		}
		if (turn == Color.BLACK)
			turn = Color.WHITE;
		else
			turn = Color.BLACK;
		if (moveType == MoveType.ENPASSANT) {
			if (piece.getColor() == Color.WHITE) {
				Piece getPiece = to.board.the_board[to.getX() - 1][to.getY()]
						.getPiece();
				to.board.the_board[to.getX() - 1][to.getY()].setPiece(null);
				getPiece.kill();
				// give a thought? or preserve deatSquare to be used in
				// undo?
				getPiece.setSquare(null);
				last.setKilled(getPiece);
			} else {
				Piece getPiece = to.board.the_board[to.getX() + 1][to.getY()]
						.getPiece();
				to.board.the_board[to.getX() + 1][to.getY()].setPiece(null);
				getPiece.kill();
				// give a thought? or preserve deathSquare to be used in
				// undo?
				getPiece.setSquare(null);
				last.setKilled(getPiece);
			}
			return MoveType.ENPASSANT;
		} else {
			last.setMoveType(moveType);
			return moveType;
		}
	}
}
