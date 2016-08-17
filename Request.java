

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.*;
import javax.json.*;

public class Request {


    private String api;
    private String githubRepo;
    private JsonObject jsonObj;


    public Request (String api, String githubRepo)

    {
        this.api = api;
        this.githubRepo = githubRepo;
        jsonObj = Json.createObjectBuilder()
                .add("token", api)
                .add("github", githubRepo)
                .build();
        System.out.println(jsonObj.toString());
    }

    public void connect(){
        HttpClient client = HttpClientBuilder.create().build();

        try {

            HttpPost request = new HttpPost("http://challenge.code2040.org/api/register");
            StringEntity string = new StringEntity(jsonObj.toString());
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(string);
            client.execute(request);
            
        }catch (Exception e){

        }

    }

    public static void main (String[] args){

        Request request = new Request ("a8eb7d1e871165d0827b291c76399b5a", "https://github.com/rhuamani/code2040/");
        request.connect();

    }

}