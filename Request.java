import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.*;
import java.io.*;
import java.util.ArrayList;
import javax.json.*;
import javax.json.JsonArray;
import org.joda.time.*;
import org.joda.time.format.*;
import org.joda.time.format.ISODateTimeFormat;
public class Request {

    private String endpoint;
    private JsonObject jsonObj;
    private JsonObjectBuilder builder;

    //For Challenge 2
    public Request(String key1, String value1, String endpoint) {
        this.endpoint = endpoint;
        builder = Json.createObjectBuilder()
                .add(key1, value1);
        jsonObj= builder.build();
    }
    //For Challenge 1
    public Request(String key1, String value1, String key2, String value2, String endpoint)
    {
        this(key1, value1, endpoint);
        jsonObj = builder.add(key2,value2).build();
    }

    // for Challenge 3
    public Request(String key1, String value1, String key2, Integer value2, String endpoint)
    {
        this(key1, value1, endpoint);
        jsonObj = builder.add(key2,value2).build();
    }

    // for Challenge 4
    public Request(String key1, String value1, String key2, ArrayList<String> value2, String endpoint)
    {
        this(key1, value1,endpoint);
        JsonArrayBuilder arrBld = Json.createArrayBuilder();
        for (String str : value2){
            arrBld.add(str);
        }
        jsonObj = builder.add(key2, arrBld.build()).build();

    }

    //Sends request and
    //prints what the endpoint gives back
    public String connect() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response;
        StringBuffer result = new StringBuffer();

        try {
            HttpPost request = new HttpPost(endpoint);
            StringEntity data = new StringEntity(jsonObj.toString());
            request.addHeader("content-type", "application/json");

            request.setEntity(data);
            response = client.execute(request);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            String line = "";

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        System.out.println(result);
        return result.toString();

    }
    //Returns reversed form of input string
    public String reversedString(String input) {

        String reverse = "";
        for (int i = input.length() - 1; i >= 0; i--) {
            reverse += input.charAt(i);
        }
        return reverse;
    }


    //Returns jsonObject of a string in jsonformat
    public JsonObject stringToJson(String input) {

        JsonReader jsonReader = Json.createReader(new StringReader(input));
        JsonObject obj = jsonReader.readObject();
        jsonReader.close();
        return obj;
    }

    public int findNeedleIndex(String input) {

        JsonObject obj = stringToJson(input);
        JsonArray arr = obj.getJsonArray("haystack");

        return arr.indexOf(obj.get("needle"));

    }

    //returns arraylist of strings without the specified prefix
    public ArrayList<String> stringArrwithoutPrefix(String input){

        ArrayList<String> arr = new ArrayList<>();
        JsonObject obj = stringToJson(input);
        String prefix = obj.get("prefix").toString();
        JsonArray jArr = obj.getJsonArray("array");

        for (JsonValue item : jArr){
            String str = item.toString();
            if (!str.startsWith(prefix)) arr.add(str);
        }
        return arr;
    }

    public String addTime (String input){
        JsonObject obj = stringToJson(input);
        String dateStamp = obj.get("datestamp").toString();
        int interval = Integer.parseInt(obj.get("interval").toString());

        DateTimeFormatter formatter = ISODateTimeFormat.dateTimeNoMillis();
        LocalDateTime time = formatter.parseLocalDateTime(dateStamp);

        return time.plusSeconds(interval).toString(formatter)+"Z";

    }

    public static void main(String[] args) {
//
        String token = "a8eb7d1e871165d0827b291c76399b5a";
        String url = "http://challenge.code2040.org/api/";

        //Challenge1
        Request chlng1 = new Request ("token",token, "github", "http://github.com/rhuamani/code2040/",url+ "register");
        chlng1.connect();

        //Challenge 2
        Request chlng2 = new Request("token",token, url +" reverse");
        Request chlng2Return = new Request ("token",token, "string", chlng2.reversedString(chlng2.connect()), url+ "reverse/validate" );
        chlng2.connect();

        //Challenge 3
        Request chlng3 = new Request("token", token, url+ "haystack");
        Request chlng3Return = new Request("token", token, "needle", chlng3.findNeedleIndex(chlng3.connect()), url+ "haystack/validate");
        chlng3Return.connect();

        //Challenge 4
        Request chlng4 = new Request("token",token,url + "prefix");
        Request chlng4Return = new Request("token", token, "array", chlng4.stringArrwithoutPrefix(chlng4.connect()),url +"prefix/validate");
        chlng4Return.connect();

        //Challenge 5
        Request chlng5 = new Request("token", token,url +"dating");
        Request chlng5Return = new Request("token",token,"datestamp", chlng5.addTime(chlng5.connect()), url + "dating/validate");
        chlng5Return.connect();
    }

}