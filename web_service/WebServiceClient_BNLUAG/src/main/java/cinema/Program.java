package cinema;
import javax.xml.ws.BindingProvider;
import seatreservation.CinemaService;
import seatreservation.ICinema;
import seatreservation.ICinemaBuyCinemaException;
import seatreservation.ICinemaLockCinemaException;
import seatreservation.ICinemaReserveCinemaException;
import seatreservation.Seat;

public class Program {
	public static void main(String[] args) throws ICinemaLockCinemaException, ICinemaReserveCinemaException, ICinemaBuyCinemaException {
		if (args.length != 4) {
			System.out.println("The number of specified parameters is not 4.");
		}
		else {
			try {
				String url = args[0];
				String row = args[1];
				String column = args[2];
				String task = args[3];
				// Create the proxy factory:
				CinemaService CinemaService = new CinemaService();
				// Create the hello proxy object:
				ICinema cinema = CinemaService.getICinemaHttpSoap11Port();
				// Cast it to a BindingProvider:
				BindingProvider bp = (BindingProvider)cinema;
				// Set the URL of the service:
				bp.getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
				
				Seat seat = new Seat();
				seat.setColumn(column);
				seat.setRow(row);
				
				if (args[3].equals("Lock")) {
					cinema.lock(seat, 1);
				}
				
				if (args[3].equals("Reserve")) {
					String id = cinema.lock(seat, 1);
					cinema.reserve(id);
				}
				
				if (args[3].equals("Buy")) {
					String id = cinema.lock(seat, 1);
					cinema.buy(id);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}