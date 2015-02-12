package chess;

public class History {
	private History prev, next;
	private Piece upgradedTo;
	private boolean firstMove= false;
	private Square to, from;
	private MoveType moveType;
	private Color turn;
	private Board board;
	private Piece killed;
	public void setKilled(Piece piece){
		this.killed = piece;
	}
	public Piece getUpgradedTo(){
		return upgradedTo;
	}
	public void setUpgradedTo(Piece upgradedTo){
		this.upgradedTo = upgradedTo;
	}
	public boolean isFirstMove(){
		return this.firstMove;
	}
	public void setFirstMove(boolean isFirst){
		this.firstMove = isFirst;
	}
	public History(Square to, Square from, Piece killed) {
		this.to = to;
		this.from = from;
		this.killed = killed;
	}
	public Square getTo(){
		return this.to;
	}
	public Piece getKilled(){
		return this.killed;
	}
	public MoveType getMoveType(){
		return this.moveType;
	}
	public Square getFrom(){
		return this.from;
	}
	public void setMoveType(MoveType moveType){
		this.moveType = moveType;
	}
	public void setNext(History next) {
		this.next = next;
		next.prev = this;
	}
	public History getNext(){
		return this.next;
	}
	public History getPrevious(){
		return this.prev;
	}
}