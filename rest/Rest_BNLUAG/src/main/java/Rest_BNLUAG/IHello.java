package Rest_BNLUAG;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

@Path("MovieDatabase")
public interface IHello {
	
	@GET
	@Path("movies")
	public Response listMovies(@Context HttpHeaders headers);
	
	@POST
	@Path("movies")
	@Consumes("application/xml")
	public Response insertMovie_xml(@Context HttpHeaders headers, Movie body);
	
	@POST
	@Path("movies")
	@Consumes("application/json")
	public Response insertMovie_json(@Context HttpHeaders headers, Movie body);
	
	@PUT
	@Path("movies/{id}")
	@Consumes("application/json")
	public void insertMovieId_json(@Context HttpHeaders headers, @PathParam("id") int id, Movie body);
	
	@PUT
	@Path("movies/{id}")
	@Consumes("application/xml")
	public void insertMovieId_xml(@Context HttpHeaders headers, @PathParam("id") int id, Movie body);
	
	@GET
	@Path("movies/{id}")
	public Response getMovie(@Context HttpHeaders headers, @PathParam("id") int id);
	
	@DELETE
	@Path("movies/{id}")
	public void deleteMovie(@PathParam("id") int id);
	
	@GET
	@Path("movies/find")
	public Response selectMovie(@Context HttpHeaders headers, @QueryParam("year") int year, @QueryParam("orderby") String orderby);
}
