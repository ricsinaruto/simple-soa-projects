package microservices.movies;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="movies")
public class Result {
	@XmlElement(name="id")
	public static int[] id;
	
	public int[] getId() {
		return id;
	}
}
