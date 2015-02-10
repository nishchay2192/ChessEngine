package chess;

public class Square {
	private int x, y;
	private Piece piece;
	private Color color;
	Board board;
	
	public Square(int X, int Y, Board board) {
		this.board = board;
		x = X;
		y = Y;
		if ((x & 1) == (y & 2)) {
			color = Color.WHITE;
		}else{
			color = Color.BLACK;
		}
	}
	public void setPiece(Piece piece){
		this.piece = piece;
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Piece getPiece() {
		return this.piece;
	}
}
