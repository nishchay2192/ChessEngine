package chess;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.InputMismatchException;

public class Main {
	public static void main(String s[]) {
		InputReader in = new InputReader(System.in);
		Board board = new Board();
		HashMap<PieceType, String> hash = new HashMap();
		hash.put(PieceType.KING, "E");
		hash.put(PieceType.QUEEN, "Q");
		hash.put(PieceType.BISHOP, "B");
		hash.put(PieceType.ROOK, "R");
		hash.put(PieceType.KNIGHT, "K");
		hash.put(PieceType.PAWN, "P");
		boolean gameNotOver = true;
		while (gameNotOver) {
			displayBoard(board, hash);
			System.out.print("turn: " + board.getTurn());
			displayOptions();
			String fromStr = in.readString();
			String toStr = in.readString();

			int fromY = fromStr.charAt(0)-'A';
			int fromX = fromStr.charAt(1)-'0';
			int toY = toStr.charAt(0)-'A';
			int toX = toStr.charAt(1)-'0';
			board.tryToMove(board.the_board[toX][toY], board.the_board[fromX][fromY]);
			//gameNotOver = processInput(takeInput);
		}
	}
	public static void displayOptions(){
		System.out.println(". Enter your move:");
	}
	public static void displayBoard(Board board, HashMap<PieceType, String> hash) {
		System.out.println("    	   A  B  C  D  E  F  G  H ");
		System.out.println("    	   ---------------------- ");
		
		for (int i = 7; i >= 0; i--) {
			System.out.print(" 	"+(i)+"|");
			for (int j = 0; j < 8; j++) {
				Piece here = board.the_board[i][j].getPiece();
				if(here==null){
					System.out.print(" _ ");
					continue;
				}
				if (here.getColor() == Color.WHITE)
					System.out.print(" "
							+ hash.get(board.the_board[i][j].getPiece()
									.getPieceType())+" ");
				else{
					System.out.print(" "
							+ hash.get(board.the_board[i][j].getPiece()
									.getPieceType()).toLowerCase()+" ");
				}
			}
			System.out.println();
		}
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
