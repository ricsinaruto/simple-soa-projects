package microservices.movies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="movies")
public class Movies_ {
	@XmlElement(name="movie")
	public static List<Movie> movie = new ArrayList<Movie>();
	
	public static Map<Integer, Movie> movie_map = new HashMap<Integer, Movie>();
	
	public List<Movie> getMovie() {
		return movie;
	}
}
