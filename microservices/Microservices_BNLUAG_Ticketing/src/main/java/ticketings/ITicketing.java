package ticketings;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import com.google.protobuf.InvalidProtocolBufferException;

@Path("")
public interface ITicketing {
	
	@POST
	@Path("GetMovies")
	@Consumes("application/x-protobuf")
	@Produces("application/x-protobuf")
	public Response GetMovies(InputStream input) throws IOException;
	
	@POST
	@Path("GetMovies")
	@Consumes("application/json")
	@Produces("application/json")
	public Response GetMovies_json(String input) throws InvalidProtocolBufferException, IOException;
	
	@POST
	@Path("BuyTickets")
	@Consumes("application/json")
	@Produces("application/json")
	public Response BuyTickets_json(String input) throws InvalidProtocolBufferException, IOException;
	
	@POST
	@Path("BuyTickets")
	@Consumes("application/x-protobuf")
	@Produces("application/x-protobuf")
	public Response BuyTickets(InputStream input) throws IOException;
	
	@POST
	@Path("GetTickets")
	@Consumes("application/x-protobuf")
	@Produces("application/x-protobuf")
	public Response GetTickets(InputStream input) throws IOException;
	
	@POST
	@Path("GetTickets")
	@Consumes("application/json")
	@Produces("application/json")
	public Response GetTickets_json(String input) throws IOException;
}