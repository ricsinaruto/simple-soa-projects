package banking;
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

@Path("banking")
public interface IHello_b {
	
	@POST
	@Path("ChargeCard")
	@Consumes("application/x-protobuf")
	@Produces("application/x-protobuf")
	public Response ChargeCard(InputStream input);
	
	@POST
	@Path("ChargeCard")
	@Consumes("application/json")
	@Produces("application/json")
	public Response ChargeCard_json(String input);
}