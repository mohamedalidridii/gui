package services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entities.Location;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeatherService {
    private static final String API_KEY = "87e63c86183aa0dde9188c8578a4be3d";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast";
    private static final String GEOCODING_URL = "http://api.openweathermap.org/geo/1.0/direct";

    public List<String> getWeatherForecast(Location location, LocalDate startDate, LocalDate endDate) {
        List<String> weatherForecast = new ArrayList<>();
        try {
            if (location == null || location.getCountry() == null || location.getCountry().trim().isEmpty()) {
                weatherForecast.add("Location information is missing");
                return weatherForecast;
            }

            // Get coordinates for the location
            String coordinates = getCoordinates(location.getCountry());
            if (coordinates == null) {
                weatherForecast.add("Weather data unavailable for location: " + location.getCountry());
                return weatherForecast;
            }

            // Make API call to get weather forecast
            String url = String.format("%s?%s&appid=%s&units=metric", BASE_URL, coordinates, API_KEY);
            String response = makeApiCall(url);

            if (response != null) {
                Gson gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
                
                if (jsonResponse.has("list")) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate currentDate = startDate;
                    
                    while (!currentDate.isAfter(endDate)) {
                        String dateStr = currentDate.format(formatter);
                        double avgTemp = 0;
                        String description = "";
                        int count = 0;

                        for (var item : jsonResponse.getAsJsonArray("list")) {
                            JsonObject weatherItem = item.getAsJsonObject();
                            String itemDate = weatherItem.get("dt_txt").getAsString().split(" ")[0];
                            
                            if (itemDate.equals(dateStr)) {
                                avgTemp += weatherItem.getAsJsonObject("main").get("temp").getAsDouble();
                                description = weatherItem.getAsJsonArray("weather")
                                    .get(0).getAsJsonObject()
                                    .get("description").getAsString();
                                count++;
                            }
                        }

                        if (count > 0) {
                            avgTemp /= count;
                            weatherForecast.add(String.format("%s: %.1f°C, %s", 
                                dateStr, avgTemp, description));
                        }
                        
                        currentDate = currentDate.plusDays(1);
                    }
                } else {
                    weatherForecast.add("No weather data available for the specified dates");
                }
            } else {
                weatherForecast.add("Failed to get weather data from the API");
            }
        } catch (Exception e) {
            System.err.println("Error in getWeatherForecast: " + e.getMessage());
            e.printStackTrace();
            weatherForecast.add("Error fetching weather data: " + e.getMessage());
        }
        return weatherForecast;
    }

    private String getCoordinates(String location) {
        try {
            // First, try to get coordinates using the geocoding API
            String url = String.format("%s?q=%s&limit=5&appid=%s",
                GEOCODING_URL,
                location.replace(" ", "%20"),
                API_KEY);
            
            System.out.println("Searching for location: " + location);
            String response = makeApiCall(url);
            
            if (response != null && !response.equals("[]")) {
                try {
                    Gson gson = new Gson();
                    List<Map<String, Object>> locations = gson.fromJson(response, List.class);
                    
                    if (locations != null && !locations.isEmpty()) {
                        // Get the first (most relevant) result
                        Map<String, Object> locationObj = locations.get(0);
                        double lat = ((Number) locationObj.get("lat")).doubleValue();
                        double lon = ((Number) locationObj.get("lon")).doubleValue();
                        String foundName = (String) locationObj.get("name");
                        String foundCountry = (String) locationObj.get("country");
                        
                        System.out.println("Found closest match: " + foundName + ", " + foundCountry);
                        System.out.println("Using coordinates - Lat: " + lat + ", Lon: " + lon);
                        
                        return String.format("lat=%f&lon=%f", lat, lon);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing location data: " + e.getMessage());
                }
            }
            
            // If no results found, try with common city names
            String[] commonCities = {
                "London,UK", "Paris,FR", "New York,US", "Tokyo,JP", "Sydney,AU",
                "Berlin,DE", "Rome,IT", "Madrid,ES", "Amsterdam,NL", "Dubai,AE"
            };
            
            for (String city : commonCities) {
                url = String.format("%s?q=%s&limit=1&appid=%s",
                    GEOCODING_URL,
                    city.replace(" ", "%20"),
                    API_KEY);
                
                response = makeApiCall(url);
                if (response != null && !response.equals("[]")) {
                    try {
                        Gson gson = new Gson();
                        List<Map<String, Object>> locations = gson.fromJson(response, List.class);
                        if (locations != null && !locations.isEmpty()) {
                            Map<String, Object> locationObj = locations.get(0);
                            double lat = ((Number) locationObj.get("lat")).doubleValue();
                            double lon = ((Number) locationObj.get("lon")).doubleValue();
                            String foundName = (String) locationObj.get("name");
                            String foundCountry = (String) locationObj.get("country");
                            
                            System.out.println("Using fallback location: " + foundName + ", " + foundCountry);
                            System.out.println("Coordinates - Lat: " + lat + ", Lon: " + lon);
                            
                            return String.format("lat=%f&lon=%f", lat, lon);
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing fallback location data: " + e.getMessage());
                    }
                }
            }
            
            System.out.println("No matching location found for: " + location);
            return null;
            
        } catch (Exception e) {
            System.err.println("Error in getCoordinates: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private String makeApiCall(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            
            int responseCode = conn.getResponseCode();
            System.out.println("API Response Code: " + responseCode);
            
            if (responseCode == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                System.err.println("API call failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Error in makeApiCall: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
} 