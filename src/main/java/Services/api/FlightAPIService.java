package Services.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FlightAPIService {
    private static final String API_KEY = "d07683152a40f26781a5ca047fd2bb7a";
    private static final String BASE_URL = "http://api.aviationstack.com/v1/flights";

    public String fetchFlights(String departureIATA, String arrivalIATA) throws IOException, InterruptedException {
        String url = BASE_URL + "?access_key=" + API_KEY
                + "&dep_iata=" + departureIATA
                + "&arr_iata=" + arrivalIATA;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); // You will parse this JSON response
    }
}
