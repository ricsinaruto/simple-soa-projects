package microservices.movies;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="movie")
public class Movie {
	@XmlElement(name="title")
	public String title;
	@XmlElement(name="year")
	public int year;
	@XmlElement(name="director")
	public String director;
	@XmlElement(name="actor")
	public String[] actor;
	
	
	public Movie(String title, int year, String director, String[] actor) {
		this.title = title;
		this.year = year;
		this.director = director;
		this.actor = actor;
		if (actor == null) {
			this.actor = new String[0];
		}
	}
	
	public Movie() {
		
	}
}

