package ticketings;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import banking.Banking.ChargeCardRequest;
import banking.Banking.ChargeCardResponse;
import movies.Movies;
import movies.Movies.MovieIdList;
import ticketing.Ticketing.BuyTicketsRequest;
import ticketing.Ticketing.BuyTicketsResponse;
import ticketing.Ticketing.GetMoviesRequest;
import ticketing.Ticketing.GetMoviesResponse;
import ticketing.Ticketing.GetTicketsResponse;
import ticketing.Ticketing.Ticket;

public class Ticketing implements ITicketing {
	private static String MoviesAddress;
	private static String BankingAddress;
	
	private static Map<Integer,Integer> movie_tickets = new HashMap<Integer,Integer>();
	
	private static String getMoviesAddress() {
		if (MoviesAddress == null) MoviesAddress = System.getProperty("microservices.movies.url");
		return MoviesAddress;
	}
	
	private static String getBankingAddress() {
		if (BankingAddress == null) BankingAddress = System.getProperty("microservices.banking.url");
		return BankingAddress;
	}
	
	private IHello getMoviesBackend() {
		String backendAddress = Ticketing.getMoviesAddress();
		if (backendAddress == null) return null;
		
		ResteasyProviderFactory instance = ResteasyProviderFactory.getInstance();
		ResteasyClient client = new ResteasyClientBuilder().providerFactory(instance).build();
		ResteasyWebTarget target = client.target(backendAddress);
		
		return target.proxy(IHello.class);
	}
	
	private IHello_b getBankingBackend() {
		String backendAddress = Ticketing.getBankingAddress();
		if (backendAddress == null) return null;
		
		ResteasyProviderFactory instance = ResteasyProviderFactory.getInstance();
		ResteasyClient client = new ResteasyClientBuilder().providerFactory(instance).build();
		ResteasyWebTarget target = client.target(backendAddress);
		
		return target.proxy(IHello_b.class);
	}

	@Override
	public Response GetMovies(InputStream input) throws IOException {
		GetMoviesRequest request = GetMoviesRequest.parseFrom(input);
		int year = request.getYear();
		
		IHello backend = this.getMoviesBackend();
		Response r = backend.selectMovie(year, "Title");
		
		MovieIdList id_list = MovieIdList.parseFrom((InputStream)r.getEntity());
		
		GetMoviesResponse.Builder builder = GetMoviesResponse.newBuilder();
		for (int i=0;i<id_list.getIdCount();i++) {
			Response movie_response = backend.getMovie(id_list.getId(i));
			Movies.Movie movie = Movies.Movie.parseFrom((InputStream)movie_response.getEntity());
			
			builder.addMovie(ticketing.Ticketing.Movie.newBuilder().setId(id_list.getId(i)).setTitle(movie.getTitle()).build());
		}
		
		return Response.ok(builder.build().toByteArray()).build();
	}

	@Override
	public Response GetMovies_json(String input) throws InvalidProtocolBufferException, IOException {
		GetMoviesRequest.Builder request_builder = GetMoviesRequest.newBuilder();
		JsonFormat.parser().merge(input, request_builder);
		int year = request_builder.build().getYear();
		
		IHello backend = this.getMoviesBackend();
		Response r = backend.selectMovie(year, "Title");
		
		MovieIdList id_list = MovieIdList.parseFrom((InputStream)r.getEntity());
		
		GetMoviesResponse.Builder builder = GetMoviesResponse.newBuilder();
		for (int i=0;i<id_list.getIdCount();i++) {
			Response movie_response = backend.getMovie(id_list.getId(i));
			Movies.Movie movie = Movies.Movie.parseFrom((InputStream)movie_response.getEntity());
			
			builder.addMovie(ticketing.Ticketing.Movie.newBuilder().setId(id_list.getId(i)).setTitle(movie.getTitle()).build());
		}
		
		String json = JsonFormat.printer().print(builder.build());
		return Response.ok(json).build();
	}

	@Override
	public Response BuyTickets(InputStream input) throws IOException {
		BuyTicketsRequest request = BuyTicketsRequest.parseFrom(input);
		
		int id = request.getMovieId();
		int count = request.getCount();
		String cardNumber = request.getCardNumber();
		
		IHello_b backend = this.getBankingBackend();
		Response r = backend.ChargeCard(new ByteArrayInputStream(ChargeCardRequest.newBuilder().setAmount(count*10).setCardNumber(cardNumber).build().toByteArray()));
		
		ChargeCardResponse response = ChargeCardResponse.parseFrom((InputStream)r.getEntity());
		boolean success = response.getSuccess();
		
		if (success && movie_tickets.containsKey(id)) movie_tickets.replace(id, movie_tickets.get(id) + count);
		else if (success) movie_tickets.put(id, count);
		
		return Response.ok(BuyTicketsResponse.newBuilder().setSuccess(success).build().toByteArray()).build();
	}
	
	@Override
	public Response BuyTickets_json(String input) throws IOException {
		BuyTicketsRequest.Builder request_builder = BuyTicketsRequest.newBuilder();
		JsonFormat.parser().merge(input, request_builder);
		BuyTicketsRequest request = request_builder.build();
		
		int id = request.getMovieId();
		int count = request.getCount();
		String cardNumber = request.getCardNumber();
		
		IHello_b backend = this.getBankingBackend();
		Response r = backend.ChargeCard(new ByteArrayInputStream(ChargeCardRequest.newBuilder().setAmount(count*10).setCardNumber(cardNumber).build().toByteArray()));
		
		ChargeCardResponse response = ChargeCardResponse.parseFrom((InputStream)r.getEntity());
		boolean success = response.getSuccess();
		
		if (success && movie_tickets.containsKey(id)) movie_tickets.replace(id, movie_tickets.get(id) + count);
		else if (success) movie_tickets.put(id, count);
		
		String json = JsonFormat.printer().print(BuyTicketsResponse.newBuilder().setSuccess(success).build());
		return Response.ok(json).build();
	}

	@Override
	public Response GetTickets(InputStream input) throws IOException {
		GetTicketsResponse.Builder builder = GetTicketsResponse.newBuilder();
		for (int id: movie_tickets.keySet()) {
			Ticket t = Ticket.newBuilder().setMovieId(id).setCount(movie_tickets.get(id)).build();
			builder.addTicket(t);
		}
		
		return Response.ok(builder.build().toByteArray()).build();
	}

	@Override
	public Response GetTickets_json(String input) throws IOException {
		GetTicketsResponse.Builder builder = GetTicketsResponse.newBuilder();
		for (int id: movie_tickets.keySet()) {
			Ticket t = Ticket.newBuilder().setMovieId(id).setCount(movie_tickets.get(id)).build();
			builder.addTicket(t);
		}
		
		String json = JsonFormat.printer().print(builder.build());
		return Response.ok(json).build();
	}
}
