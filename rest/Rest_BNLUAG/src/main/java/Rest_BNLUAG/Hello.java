package Rest_BNLUAG;

import java.io.IOException;
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

import Rest_BNLUAG.IHello;

public class Hello implements IHello {
	public static Movies movies = new Movies();
	public static Result result = new Result();
	public static Result2 result2 = new Result2();
	
	public Response listMovies(HttpHeaders headers) {
		return Response.status(Response.Status.OK).entity(movies).build();
	}
	
	public void deleteMovie(int id) {
		Movies.movie.remove(Movies.movie_map.get(id));
		Movies.movie_map.remove(id);
	}
	
	public Response selectMovie(HttpHeaders headers, int year, String orderby) {
		List<Integer> filtered = new ArrayList<Integer>();
		
		for(Integer i: Movies.movie_map.keySet()) {
			if (Movies.movie_map.get(i).year == year) {
				filtered.add(i);
			}
		}
		
		// Order.
		if (orderby.equals("Title")) {
			Collections.sort(filtered, new Comparator<Integer>() {
				public int compare(Integer i1, Integer i2) {
					return Movies.movie_map.get(i1).title.compareTo(Movies.movie_map.get(i2).title);
				}
			});
		}
		else if (orderby.equals("Director")) {
			Collections.sort(filtered, new Comparator<Integer>() {
				public int compare(Integer i1, Integer i2) {
					return Movies.movie_map.get(i1).director.compareTo(Movies.movie_map.get(i2).director);
				}
			});
		}
		
		Result.id = new int[filtered.size()];
		for (int i=0; i<filtered.size(); i++) {
			Result.id[i] = filtered.get(i);
		}
		return Response.status(Response.Status.OK).entity(result).build();
	}
	
	public void insertMovieId_xml(HttpHeaders headers, int id, Movie body) {
		Movie x = body;
		Movie m = new Movie(x.title, x.year, x.director, x.actor);
		
		if (Movies.movie_map.containsKey(id)) {
			Movies.movie.remove(Movies.movie_map.get(id));
		}
		Movies.movie_map.put(id, m);
		Movies.movie.add(m);
	}
	
	public void insertMovieId_json(HttpHeaders headers, int id, Movie body) {
		Movie x = body;
		Movie m = new Movie(x.title, x.year, x.director, x.actor);
		
		if (Movies.movie_map.containsKey(id)) {
			Movies.movie.remove(Movies.movie_map.get(id));
		}
		Movies.movie_map.put(id, m);
		Movies.movie.add(m);
	}
	
	public Response getMovie(HttpHeaders headers, int id) {
		if (!Movies.movie_map.containsKey(id)) {
			return Response.status(404).build();
		}
		return Response.status(Response.Status.OK).entity(Movies.movie_map.get(id)).build();
	}
	
	public Response insertMovie_xml(HttpHeaders headers, Movie body) {
		Movie x = body;
		Movie m = new Movie(x.title, x.year, x.director, x.actor);
		int id = 1;
		
		// Get a new id.
		Set<Integer> keys = Movies.movie_map.keySet();
		if (keys != null) {
			for (int key: keys) {
				if (key >= id) {
					id = key + 1;
				}
			}
		}
		Movies.movie_map.put(id, m);
		Movies.movie.add(m);
		
		Result2.id = id;
		return Response.status(Response.Status.OK).entity(result2).build();
	}
	
	public Response insertMovie_json(HttpHeaders headers, Movie body) {
		Movie x = body;
		Movie m = new Movie(x.title, x.year, x.director, x.actor);
		int id = 1;
		
		// Get a new id.
		Set<Integer> keys = Movies.movie_map.keySet();
		if (keys != null) {
			for (int key: keys) {
				if (key >= id) {
					id = key + 1;
				}
			}
		}
		Movies.movie_map.put(id, m);
		Movies.movie.add(m);
		
		Result2.id = id;
		return Response.status(Response.Status.OK).entity(result2).build();
	}
	
}