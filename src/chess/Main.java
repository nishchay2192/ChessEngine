package chess;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.InputMismatchException;

public class Main {
	public static void main(String s[]) {
		InputReader in = new InputReader(System.in);
		Board board = new Board();
		Result result = playOnConsole(board, in);
	}

	public static Result playOnConsole(Board board, InputReader in) {
		HashMap<PieceType, String> hash = new HashMap();
		hash.put(PieceType.KING, "E");
		hash.put(PieceType.QUEEN, "Q");
		hash.put(PieceType.BISHOP, "B");
		hash.put(PieceType.ROOK, "R");
		hash.put(PieceType.KNIGHT, "K");
		hash.put(PieceType.PAWN, "P");
		Result result = Result.DRAW;
		boolean gameNotOver = true;
		while (gameNotOver) {
			displayBoard(board, hash);
			displayOptions();
			gameNotOver = !(processInput(in, board));
		}
		//result = board.result;
		return result;
	}

	public static boolean processInput(InputReader in, Board board) {
		String fromStr;
		boolean gameOver = false;
		int fromY = 0, toY = 0;
		int fromX = 0, toX = 0;
		boolean correctInput = false;
		while (!correctInput) {
			fromStr = in.readString();
			if (fromStr.equalsIgnoreCase("Undo")) {
				board.undoMove();
				correctInput = true;
			} else if (fromStr.equalsIgnoreCase("Redo")) {
				board.redoMove();
				correctInput = true;
			} else if (fromStr.equalsIgnoreCase("showHistory")) {
				System.out.println(board.showHistory());
				correctInput = true;
			} else if (fromStr.equalsIgnoreCase("quit")) {
				gameOver = true;
				correctInput = true;
			} else {
				fromY = fromStr.charAt(0) - 'A';
				fromX = fromStr.charAt(1) - '0';
				fromStr = in.readString();
				toY = fromStr.charAt(0) - 'A';
				toX = fromStr.charAt(1) - '0';
				if (!((fromX >= 0 && fromX <= 7 && fromY >= 0 && fromY <= 7) && (toX >= 0
						&& toX <= 7 && toY >= 0 && toY <= 7))) {
					System.out.println("wrongInput, please Input again");
				} else {
					correctInput = true;
				}
				MoveType moveType = board.tryToMove(board.the_board[toX][toY],
						board.the_board[fromX][fromY]);
				if (moveType == MoveType.UPGRADE) {
					char upgrade = askForUpgradedPiece(in);
					board.upgradePawn(board.the_board[toX][toY].getPiece(),
							upgrade);
				}
				// System.out.println("Taken Input");
				/*
				 * if (moveType != MoveType.ILLEGAL) { boolean checkMate =
				 * false; checkMate = board.checkIfMate(board.getTurn());
				 * 
				 * if (checkMate) { gameNotOver = false; result =
				 * board.getTurn() == Color.WHITE ? Result.BLACK : Result.WHITE;
				 * } }
				 */
			}
		}
		return gameOver;
	}

	public static char askForUpgradedPiece(InputReader in) {
		System.out
				.println("Upgrade pawn to: Enter Q for Queen, R for Rook, N for Knight or B for Bishop, default is Queen");
		char upgrade = in.readString().toUpperCase().charAt(0);
		if (upgrade == 'R' || upgrade == 'N' || upgrade == 'B')
			return upgrade;
		return 'Q';
	}

	public static void displayOptions() {
		System.out.println(". Enter your move, or Undo or Redo, quit or showHistory.");
	}

	public static void displayBoard(Board board, HashMap<PieceType, String> hash) {
		System.out.println("    	   A  B  C  D  E  F  G  H ");
		System.out.println("    	   ---------------------- ");

		for (int i = 7; i >= 0; i--) {
			System.out.print(" 	" + (i) + "|");
			for (int j = 0; j < 8; j++) {
				Piece here = board.the_board[i][j].getPiece();
				if (here == null) {
					System.out.print(" _ ");
					continue;
				}
				if (here.getColor() == Color.WHITE)
					System.out.print(" "
							+ hash.get(board.the_board[i][j].getPiece()
									.getPieceType()) + " ");
				else {
					System.out.print(" "
							+ hash.get(
									board.the_board[i][j].getPiece()
											.getPieceType()).toLowerCase()
							+ " ");
				}
			}
			System.out.println();
		}
		System.out.print("turn: " + board.getTurn());
	}
}

class InputReader {
	private InputStream stream;
	private byte[] buf = new byte[1024];
	private int curChar;
	private int numChars;

	public InputReader(InputStream stream) {
		this.stream = stream;
	}

	public int read() {
		if (numChars == -1)
			throw new InputMismatchException();
		if (curChar >= numChars) {
			curChar = 0;
			try {
				numChars = stream.read(buf);
			} catch (IOException e) {
				throw new InputMismatchException();
			}
			if (numChars <= 0)
				return -1;
		}
		return buf[curChar++];
	}

	public int readInt() {
		int c = read();
		while (isSpaceChar(c))
			c = read();
		int sgn = 1;
		if (c == '-') {
			sgn = -1;
			c = read();
		}
		int res = 0;
		do {
			if (c < '0' || c > '9')
				throw new InputMismatchException();
			res *= 10;
			res += c - '0';
			c = read();
		} while (!isSpaceChar(c));
		return res * sgn;
	}

	public long readLong() {
		int c = read();
		while (isSpaceChar(c))
			c = read();
		int sgn = 1;
		if (c == '-') {
			sgn = -1;
			c = read();
		}
		long res = 0;
		do {
			if (c < '0' || c > '9')
				throw new InputMismatchException();
			res *= 10;
			res += c - '0';
			c = read();
		} while (!isSpaceChar(c));
		return res * sgn;
	}

	public String readString() {
		int c = read();
		while (isSpaceChar(c))
			c = read();
		StringBuffer res = new StringBuffer();
		do {
			res.appendCodePoint(c);
			c = read();
		} while (!isSpaceChar(c));
		return res.toString();
	}

	public static boolean isSpaceChar(int c) {
		return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
	}

	public String next() {
		return readString();
	}
}
