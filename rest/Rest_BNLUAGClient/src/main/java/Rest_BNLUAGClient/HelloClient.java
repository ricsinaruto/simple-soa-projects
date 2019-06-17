package Rest_BNLUAGClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class HelloClient {
	public static void main(String[] args) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		try{
			// Handle 404 error.
			//HttpGet request = new HttpGet("http://localhost:8081/banking/ChargeCard");
			
			
			//HttpDelete request = new HttpDelete("http://localhost:8080/Rest_BNLUAG/resources/MovieDatabase/movies/4621910");
			
			HttpPost request = new HttpPost("http://localhost:8082/banking/ChargeCard");
			//HttpPut request = new HttpPut("http://localhost:8080/Rest_BNLUAG/resources/MovieDatabase/movies/4621913");
		    StringEntity params = new StringEntity("{ \"cardNumber\": \"abcd\", \"amount\": 1000 }");
		    request.setEntity(params);
			
			request.addHeader("Content-Type", String.valueOf(ContentType.APPLICATION_JSON));
			request.addHeader("Accept", String.valueOf(ContentType.APPLICATION_JSON));
		    HttpResponse response = httpClient.execute(request);
		    
		    
		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		    String line;
		    while ((line = rd.readLine()) != null) {
		        System.out.println(line);
		    }
		    rd.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}