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
public interface IHello {
	
	@GET
	@Path("movies")
	@Consumes("application/json")
	@Produces("application/json")
	public Response listMovies_json() throws InvalidProtocolBufferException;
	
	@GET
	@Path("movies")
	@Consumes("application/x-protobuf")
	@Produces("application/x-protobuf")
	public Response listMovies();
	
	@POST
	@Path("movies")
	@Consumes("application/x-protobuf")
	@Produces("application/x-protobuf")
	public Response insertMovie(InputStream input) throws IOException;
	
	@POST
	@Path("movies")
	@Consumes("application/json")
	@Produces("application/json")
	public Response insertMovie_json(String input) throws InvalidProtocolBufferException, IOException;
	
	@PUT
	@Path("movies/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public void insertMovieId_json(@PathParam("id") int id, String input) throws InvalidProtocolBufferException, IOException;
	
	@PUT
	@Path("movies/{id}")
	@Consumes("application/x-protobuf")
	@Produces("application/x-protobuf")
	public void insertMovieId(@PathParam("id") int id, InputStream input) throws IOException;
	
	@GET
	@Path("movies/{id}")
	@Consumes("application/x-protobuf")
	@Produces("application/x-protobuf")
	public Response getMovie(@PathParam("id") int id);
	
	@GET
	@Path("movies/{id}")
	@Consumes("application/json")
	@Produces("application/json")
	public Response getMovie_json(@PathParam("id") int id) throws InvalidProtocolBufferException;
	
	@DELETE
	@Path("movies/{id}")
	public void deleteMovie(@PathParam("id") int id);
	
	@GET
	@Path("movies/find")
	@Consumes("application/x-protobuf")
	@Produces("application/x-protobuf")
	public Response selectMovie(@QueryParam("year") int year, @QueryParam("orderby") String orderby);
	
	@GET
	@Path("movies/find")
	@Consumes("application/x-json")
	@Produces("application/x-json")
	public Response selectMovie_json(@QueryParam("year") int year, @QueryParam("orderby") String orderby) throws InvalidProtocolBufferException;
}
