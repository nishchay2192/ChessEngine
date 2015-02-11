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
		piece.moved = true;
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
		piece.getSquare().setPiece(toBeUpgraded);
		toBeUpgraded.setSquare(piece.getSquare());
		piece.kill();
		piece.setSquare(null);
		piece = toBeUpgraded;
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
		//System.out.println("Entered Board");
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
