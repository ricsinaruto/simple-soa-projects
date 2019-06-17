package microservices.movies;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="result")
public class Result2 {
	@XmlElement(name="id")
	public static int id;
	
	public int getId() {
		return id;
	}
}
