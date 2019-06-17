package WebSocket_BNLUAG;

public class Seat {
	public String status;
	public int col;
	public int row;
	
	public Seat(String status, int row, int col) {
		this.status = status;
		this.row = row;
		this.col = col;
	}
}