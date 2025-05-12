package services;
import entities.DestinationRequest;
import org.json.JSONObject;

import javax.sound.sampled.Line;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FlaskConnector {

    private static final String FLASK_API_URL = "http://127.0.0.1:5000/predict";

    public static String getConnection(DestinationRequest request) throws IOException{
        JSONObject json=new JSONObject();
        json.put("Mood", request.getMood());
        json.put("Physicality", request.getPhysicality());
        json.put("Family Friendly", request.getFamilyFriendly());
        json.put("Budget ($/day)", request.getBudgetPerDay());
        json.put("Security", request.getSecurity());
        json.put("temp_avg", request.getTempAvg());
        json.put("duration_avg", request.getDurationAvg());


        URL url=new URL(FLASK_API_URL);
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        try(OutputStream os=conn.getOutputStream()){
            byte[] input=json.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input);
        }

        StringBuilder response=new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

            String Line;
            while((Line=br.readLine())!=null){
                response.append(Line);
            }

        }
        JSONObject responseJson=new JSONObject(response.toString());
        return responseJson.optString("recommended_destination", "No recommendation found");

    }
}
