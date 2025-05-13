package Utils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class MapApp extends Application {
    @Override
    public void start(Stage stage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        // Replace with your real API key
        String apiKey = "YOUR_GOOGLE_MAPS_API_KEY";
        String latitude = "37.422";     // example: Google HQ
        String longitude = "-122.084";

        // HTML with embedded map
        String content = """
            <html>
            <head>
              <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
              <style type="text/css">
                #map { height: 100%%; width: 100%%; }
                html, body { height: 100%%; margin: 0; padding: 0; }
              </style>
              <script src="https://maps.googleapis.com/maps/api/js?key=%s"></script>
              <script>
                function initMap() {
                  var location = { lat: %s, lng: %s };
                  var map = new google.maps.Map(document.getElementById('map'), {
                    zoom: 15,
                    center: location
                  });
                  var marker = new google.maps.Marker({
                    position: location,
                    map: map
                  });
                }
                window.onload = initMap;
              </script>
            </head>
            <body>
              <div id="map"></div>
            </body>
            </html>
            """.formatted(apiKey, latitude, longitude);

        webEngine.loadContent(content);

        stage.setScene(new Scene(webView, 800, 600));
        stage.setTitle("Google Maps in JavaFX");
        stage.show();
    }
}
