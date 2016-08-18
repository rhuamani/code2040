
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
        String needle = obj.get("needle").toString();
        JsonArray arr = obj.getJsonArray("haystack");

        for (int i = 0; i < arr.size(); i++) {
            String str = arr.get(i).toString();
            if (str.equals(needle)) return i;
        }
        return 0;
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
//        //Challenge1
//        Request challenge1 = new Request ("token","a8eb7d1e871165d0827b291c76399b5a", "github", "http://github.com/rhuamani/code2040/","http://challenge.code2040.org/api/register");
//        challenge1.connect();
//
//        //Challenge 2
//        Request challenge2 = new Request("token","a8eb7d1e871165d0827b291c76399b5a","http://challenge.code2040.org/api/reverse");
//        Request challenge2Return = new Request ("token","a8eb7d1e871165d0827b291c76399b5a", "string", challenge2.reversedString(challenge2.connect()), "http://challenge.code2040.org/api/reverse/validate" );
//        challenge2Return.connect();
//
//        //Challenge 3
//        Request challenge3 = new Request("token", "a8eb7d1e871165d0827b291c76399b5a", "http://challenge.code2040.org/api/haystack");
//        Request challenge3Return = new Request("token", "a8eb7d1e871165d0827b291c76399b5a", "needle", challenge3.findNeedleIndex(challenge3.connect()), "http://challenge.code2040.org/api/haystack/validate");
//        challenge3Return.connect();
////
        //Challenge 4
        Request challenge4 = new Request("token", "a8eb7d1e871165d0827b291c76399b5a","http://challenge.code2040.org/api/prefix");
        Request challenge4Return = new Request("token", "a8eb7d1e871165d0827b291c76399b5a", "array", challenge4.stringArrwithoutPrefix(challenge4.connect()),"http://challenge.code2040.org/api/prefix/validate");
        challenge4Return.connect();
//
        //Challenge 5
        Request challenge5 = new Request("token", "a8eb7d1e871165d0827b291c76399b5a", "http://challenge.code2040.org/api/dating");
        Request challenge5Return = new Request("token","a8eb7d1e871165d0827b291c76399b5a","datestamp", challenge5.addTime(challenge5.connect()), "http://challenge.code2040.org/api/dating/validate");
        challenge5Return.connect();
    }

}