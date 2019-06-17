package WebSocket_BNLUAG;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

@ServerEndpoint("/cinema")
public class Endpoint {
	static Map<String,Session> sessions = new HashMap<String,Session>();
	static Map<String,Seat> locks = new HashMap<String,Seat>();
	static Seat seats[][];
	
	@OnOpen
	public void open(Session session) {
		System.out.println("WebSocket opened: " + session.getId());
		sessions.put(session.getId(), session);
	}
	@OnClose
	public void close(Session session) {
		System.out.println("WebSocket closed: " + session.getId());
		sessions.remove(session.getId());
	}
	@OnError
	public void error(Throwable t) {
		System.out.println("WebSocket error: " + t.getMessage());
	}
	@OnMessage
	public String message(Session session, String msg) throws IOException {
		JsonReader jsonReader = Json.createReader(new StringReader(msg));
		JsonObject object = jsonReader.readObject();
		jsonReader.close();
		String type = object.get("type").toString();
		String out_msg = "";
		
		if (type.equals("\"initRoom\"")) out_msg = initRoom(object);
		else if (type.equals("\"getRoomSize\"")) out_msg = getRoomSize();
		else if (type.equals("\"updateSeats\"")) updateSeats(session);
		else if (type.equals("\"lockSeat\"")) out_msg = lockSeat(object);
		else if (type.equals("\"unlockSeat\"")) out_msg = unlockSeat(object);
		else if (type.equals("\"reserveSeat\"")) out_msg = reserveSeat(object);
		
		return out_msg;
	}
	
	public String initRoom(JsonObject msg) {
		int rows = Integer.parseInt(msg.get("rows").toString());
		int columns = Integer.parseInt(msg.get("columns").toString());
		if (rows < 1 || columns < 1) return "{\"type\": \"error\", \"message\": \"Too few rows or columns.\"}";
		else {
			seats = new Seat[rows][columns];
			locks.clear();
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					seats[i][j] = new Seat("free", i+1, j+1);
				}
			}
		}
		return "";
	}
	
	public String getRoomSize() {
		int rows = seats.length;
		int columns = seats[0].length;
		return "{\"type\": \"roomSize\", \"rows\": "+rows+", \"columns\": "+columns+"}";
	}
	
	public void updateSeats(Session session) throws IOException {
		int rows = seats.length;
		int columns = seats[0].length;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				String msg = "{ \"type\": \"seatStatus\", \"row\": "+(i+1)+", \"column\": "+(j+1)+", \"status\": \""+seats[i][j].status+"\"}";
				session.getBasicRemote().sendText(msg);
			}
		}
	}
	
	public String lockSeat(JsonObject msg) throws IOException {
		int row = Integer.parseInt(msg.get("row").toString());
		int col = Integer.parseInt(msg.get("column").toString());
		String error_msg = "{\"type\": \"error\", \"message\": \"Seat is not free.\"}";
		
		if (row > seats.length || row < 0 || col > seats[0].length || col < 0) return error_msg;
		if (!seats[row][col].status.equals("free")) return error_msg;
		
		
		String id = "lock"+locks.size();
		locks.put(id, seats[row][col]);
		seats[row][col].status = "locked";
		// Update all sessions.
		for (Session sess: sessions.values()) {
			sess.getBasicRemote().sendText("{ \"type\": \"seatStatus\", \"row\": "+(row+1)+", \"column\": "+(col+1)+", \"status\": \"locked\"}");
		}
		return "{\"type\": \"lockResult\", \"lockId\": \""+id+"\"}";
	}
	
	public String unlockSeat(JsonObject msg) throws IOException {
		String lock = msg.get("lockId").toString().replace("\"", "");
		if (!locks.containsKey(lock)) return "{\"type\": \"error\", \"message\": \"No such locked seat.\"}";
		
		Seat seat = locks.get(lock);
		seat.status = "free";
		locks.remove(lock);
		for (Session sess: sessions.values()) {
			sess.getBasicRemote().sendText("{ \"type\": \"seatStatus\", \"row\": "+(seat.row)+", \"column\": "+(seat.col)+", \"status\": \"free\"}");
		}
		return "";
	}
	
	public String reserveSeat(JsonObject msg) throws IOException {
		String lock = msg.get("lockId").toString().replace("\"", "");
		if (!locks.containsKey(lock)) return "{\"type\": \"error\", \"message\": \"No such locked seat.\"}";
		
		Seat seat = locks.get(lock);
		seat.status = "reserved";
		for (Session sess: sessions.values()) {
			sess.getBasicRemote().sendText("{ \"type\": \"seatStatus\", \"row\": "+(seat.row)+", \"column\": "+(seat.col)+", \"status\": \"reserved\"}");
		}
		return "";
	}
}
