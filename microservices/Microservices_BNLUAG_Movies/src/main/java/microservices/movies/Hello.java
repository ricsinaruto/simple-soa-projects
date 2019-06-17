package microservices.movies;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import microservices.movies.IHello;
import movies.Movies;
import movies.Movies.MovieId;
import movies.Movies.MovieIdList;
import movies.Movies.MovieList;
import movies.Movies.MovieList.Builder;

public class Hello implements IHello {
	public static Movies_ movies = new Movies_();
	public static Result result = new Result();
	public static Result2 result2 = new Result2();
	
	public Response listMovies_json() throws InvalidProtocolBufferException {
		Movies.MovieList response = build_movies().build();
		String json = JsonFormat.printer().print(response);
		return Response.ok(json).build();
	}
	public Response listMovies() {
		return Response.ok(build_movies().build().toByteArray()).build();
	}
	
	public MovieList.Builder build_movies() {
		MovieList.Builder movies_builder = Movies.MovieList.newBuilder();
		for (Movie movie: Movies_.movie) {
			movies_builder.addMovie(build_movie(movie));
		}
		
		return movies_builder;
	}
	
	public MovieId build_id(int id) {
		return MovieId.newBuilder().setId(id).build();
	}
	
	public movies.Movies.Movie.Builder build_movie(Movie movie) {
		movies.Movies.Movie.Builder movie_builder = Movies.Movie.newBuilder();
		movie_builder.setDirector(movie.director);
		movie_builder.setTitle(movie.title);
		movie_builder.setYear(movie.year);
		for (String actor: movie.actor) {
			movie_builder.addActor(actor);
		}
		
		return movie_builder;
	}
	
	public void deleteMovie(int id) {
		Movies_.movie.remove(Movies_.movie_map.get(id));
		Movies_.movie_map.remove(id);
	}
	
	public Response selectMovie(int year, String orderby) {
		List<Integer> filtered = new ArrayList<Integer>();
		
		for(Integer i: Movies_.movie_map.keySet()) {
			if (Movies_.movie_map.get(i).year == year) {
				filtered.add(i);
			}
		}
		
		// Order.
		if (orderby.equals("Title")) {
			Collections.sort(filtered, new Comparator<Integer>() {
				public int compare(Integer i1, Integer i2) {
					return Movies_.movie_map.get(i1).title.compareTo(Movies_.movie_map.get(i2).title);
				}
			});
		}
		else if (orderby.equals("Director")) {
			Collections.sort(filtered, new Comparator<Integer>() {
				public int compare(Integer i1, Integer i2) {
					return Movies_.movie_map.get(i1).director.compareTo(Movies_.movie_map.get(i2).director);
				}
			});
		}
		
		MovieIdList.Builder id_list = MovieIdList.newBuilder();
		for (int i=0; i<filtered.size(); i++) {
			id_list.addId(filtered.get(i));
		}
		return Response.ok(id_list.build().toByteArray()).build();
	}
	
	public Response selectMovie_json(int year, String orderby) throws InvalidProtocolBufferException {
		List<Integer> filtered = new ArrayList<Integer>();
		
		for(Integer i: Movies_.movie_map.keySet()) {
			if (Movies_.movie_map.get(i).year == year) {
				filtered.add(i);
			}
		}
		
		// Order.
		if (orderby.equals("Title")) {
			Collections.sort(filtered, new Comparator<Integer>() {
				public int compare(Integer i1, Integer i2) {
					return Movies_.movie_map.get(i1).title.compareTo(Movies_.movie_map.get(i2).title);
				}
			});
		}
		else if (orderby.equals("Director")) {
			Collections.sort(filtered, new Comparator<Integer>() {
				public int compare(Integer i1, Integer i2) {
					return Movies_.movie_map.get(i1).director.compareTo(Movies_.movie_map.get(i2).director);
				}
			});
		}
		
		MovieIdList.Builder id_list = MovieIdList.newBuilder();
		for (int i=0; i<filtered.size(); i++) {
			id_list.addId(filtered.get(i));
		}
		String json = JsonFormat.printer().print(id_list.build());
		return Response.ok(json).build();
	}
	
	public void insertMovieId(int id, InputStream input) throws IOException {
		Movies.Movie x = Movies.Movie.parseFrom(input);
		Movie m = parse_movie(x);
		
		if (Movies_.movie_map.containsKey(id)) {
			Movies_.movie.remove(Movies_.movie_map.get(id));
		}
		Movies_.movie_map.put(id, m);
		Movies_.movie.add(m);
	}
	
	public void insertMovieId_json(int id, String input) throws InvalidProtocolBufferException, IOException {
		Movies.Movie.Builder request_builder = Movies.Movie.newBuilder();
		JsonFormat.parser().merge(input, request_builder);
		Movie m = parse_movie(request_builder.build());
		
		if (Movies_.movie_map.containsKey(id)) {
			Movies_.movie.remove(Movies_.movie_map.get(id));
		}
		Movies_.movie_map.put(id, m);
		Movies_.movie.add(m);
	}
	
	public Response getMovie(int id) {
		if (!Movies_.movie_map.containsKey(id)) {
			return Response.status(404).build();
		}
		Movie x = Movies_.movie_map.get(id);
		return Response.ok(build_movie(x).build().toByteArray()).build();
	}
	
	public Response getMovie_json(int id) throws InvalidProtocolBufferException {
		if (!Movies_.movie_map.containsKey(id)) {
			return Response.status(404).build();
		}
		Movie x = Movies_.movie_map.get(id);
		Movies.Movie response = build_movie(x).build();
		String json = JsonFormat.printer().print(response);
		return Response.ok(json).build();
	}
	
	public Movie parse_movie(Movies.Movie x) throws IOException {
		String[] actors = new String[x.getActorCount()];
		for (int i=0; i<x.getActorCount(); i++) actors[i] = x.getActor(i);
		return new Movie(x.getTitle(), x.getYear(), x.getDirector(), actors);
	}
	
	public Response insertMovie(InputStream input) throws IOException {
		Movies.Movie x = Movies.Movie.parseFrom(input);
		Movie m = parse_movie(x);
		int id = 1;
		
		// Get a new id.
		Set<Integer> keys = Movies_.movie_map.keySet();
		if (keys != null) {
			for (int key: keys) {
				if (key >= id) {
					id = key + 1;
				}
			}
		}
		Movies_.movie_map.put(id, m);
		Movies_.movie.add(m);
		
		return Response.ok(build_id(id).toByteArray()).build();
	}
	
	public Response insertMovie_json(String input) throws InvalidProtocolBufferException, IOException {
		Movies.Movie.Builder request_builder = Movies.Movie.newBuilder();
		JsonFormat.parser().merge(input, request_builder);
		Movie m = parse_movie(request_builder.build());
		int id = 1;
		
		// Get a new id.
		Set<Integer> keys = Movies_.movie_map.keySet();
		if (keys != null) {
			for (int key: keys) {
				if (key >= id) {
					id = key + 1;
				}
			}
		}
		Movies_.movie_map.put(id, m);
		Movies_.movie.add(m);
		
		String json = JsonFormat.printer().print(build_id(id));
		return Response.ok(json).build();
	}
	
}