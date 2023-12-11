package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.google.gson.annotations.SerializedName;

import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.text.Font;

/**
 * The {@code ApiApp} class is a JavaFX application that integrates two external RESTful JSON APIs
 * to provide users with weather information based on venue locations. The application allows users
 * to input a venue name, fetch the latitude and longitude from the first API and use this
 * information to obtain the latest weather details from the second API.
 */
public class ApiApp extends Application {

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
         .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    private static final String TICKETMASTER_ENDPOINT =
        "https://app.ticketmaster.com/discovery/v2/venues.json";

    private static final String TICKETMASTER_API_KEY;
    private static final String WEATHER_API_KEY;

    static {
        Properties config = new Properties();
        try (InputStream input =
            ApiApp.class.getClassLoader().getResourceAsStream("config.properties")) {
            config.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } // try
        TICKETMASTER_API_KEY = config.getProperty("ticketmaster.apikey");
        WEATHER_API_KEY = config.getProperty("weather.apikey");
    } // static

    private static final String WEATHER_ENDPOINT = "https://api.api-ninjas.com/v1/weather";

    // Stage and Scene for the application
    Stage stage;
    Scene scene;

    // Root layout for the application
    HBox root;

    // Layers within the root
    Separator separatorLeft;
    VBox rightVBox;

    // Components within the rightVBox
    HBox topHBox;
    VBox venueLocationVBox;
    HBox venueInfoHBox;
    VBox venueInfoDisplayVbox;
    HBox weatherInfoHBox;
    VBox weatherInfoDisplayVBox;
    VBox bottomVBox;

    // 1. Components within the topHBox
    Label text;

    // 2. Components within the venueLocationVBox
    HBox venueHBox;
    HBox stateCodeVenueHBox;

    // 2b. Components within the venueHBox
    Label venueLabel;
    TextField venueSearch;
    Button searchButton;

    // 2c. Components within stateCodeVenueHBox
    Label stateCodeLabel;
    TextField stateCodeSearch;

    // 3. Components within the venueInfoHBox
    Label venueText;

    // 4. Components within the venueInfoDisplayVbox
    Label venueName;
    Label venueLocation;
    Label venueAddress;

    // 5. Components within weatherInfoHBox
    Label weatherText;

    // 6. Components within weatherInfoDisplayVBox
    Label tempLabel;
    Label cloudPercentageLabel;
    Label humidityLabel;
    Label windSpeedLabel;

    // 7. Components within the bottomHBox
    Label bottomVenueLabel;
    Label bottomWeatherLabel;

    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        this.stage = null;
        this.scene = null;
        root = new HBox();

        // Components within the root
        separatorLeft = new Separator(Orientation.VERTICAL);
        rightVBox = new VBox(20);

        // Components within rightVBox
        topHBox = new HBox();
        venueLocationVBox = new VBox(2);
        venueInfoHBox = new HBox();
        venueInfoDisplayVbox = new VBox();
        weatherInfoHBox = new HBox();
        weatherInfoDisplayVBox = new VBox();
        bottomVBox = new VBox();

        // 1. Components within the topHBox
        text = new Label("\nWelcome to the Venue-Weather finder!\n\n" +
        "Search for a specific venue in the search bars below, using\n" +
        "the venue's name and state code, to get information about the\n" +
        "venue as well as the current weather conditions of the venue.");

        // 2. Components within the venueLocationVBox
        venueHBox = new HBox(12);
        stateCodeVenueHBox = new HBox(1);

        // 2b. Components within the venueHBox
        venueLabel = new Label("Venue:     ");
        venueSearch = new TextField();
        searchButton = new Button("Search");

        // 2c. Components within the stateVenueLabel
        stateCodeLabel = new Label("State Code:");
        stateCodeSearch = new TextField();

        // 3. Components within the venueInfoHBox
        venueText = new Label("Venue Information:");

        // 4. Components within the venueInfoDisplayVbox
        venueName = new Label("Venue Name:");
        venueLocation = new Label("Location:");
        venueAddress = new Label("Address:");

        // 5. Components within weatherInfoHBox
        weatherText = new Label("Weather Information:");

        // 6. Components within weatherInfoDisplayVBox
        tempLabel = new Label("Temperature:");
        cloudPercentageLabel = new Label("Cloud Percentage:");
        humidityLabel = new Label("Humidity:");
        windSpeedLabel = new Label("Wind Speed:");

        // 7. Components within the bottomVBox
        bottomVenueLabel = new Label("Venue information provided by Ticketmaster API.");
        bottomWeatherLabel = new Label("Weather information provided by API Ninjas. ");

    } // ApiApp

    /** {@inheritDoc} */
    @Override
    public void init() {
        // Connect all components to a root
        root.getChildren().addAll(separatorLeft, rightVBox);
        // Connect the children to the rightVBox
        rightVBox.getChildren().addAll(topHBox, venueLocationVBox, venueInfoHBox,
            venueInfoDisplayVbox, weatherInfoHBox, weatherInfoDisplayVBox, bottomVBox);
        // 1. Connect components to topHBox
        topHBox.getChildren().addAll(text);
        // 2. Connect components to venueLocationVBox
        venueLocationVBox.getChildren().addAll(venueHBox, stateCodeVenueHBox);
        // 2b. Connect components to venueHBox
        venueHBox.getChildren().addAll(venueLabel, venueSearch, searchButton);
        // 2c. Connect components to stateCodeVenueHBox
        stateCodeVenueHBox.getChildren().addAll(stateCodeLabel, stateCodeSearch);
        // 3. Connect components to venueInfoHBox
        venueInfoHBox.getChildren().addAll(venueText);
        // 4. Connect components to venueInfoDisplayVbox
        venueInfoDisplayVbox.getChildren().addAll(venueName, venueLocation, venueAddress);
        // 5. Connect components to weatherInfoHBox
        weatherInfoHBox.getChildren().addAll(weatherText);
        // 6. Connect components to weatherInfoDisplayVBox
        weatherInfoDisplayVBox.getChildren().addAll(tempLabel, cloudPercentageLabel, humidityLabel,
            windSpeedLabel);
        // 7. Connect components to bottomVBox
        bottomVBox.getChildren().addAll(bottomVenueLabel, bottomWeatherLabel);


    } // init

    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {
        this.stage = stage;

        // Set UI Properties
        venueSearch.setText("Georgia Theatre");
        stateCodeSearch.setText("GA");
        text.setFont(new Font(17));
        venueLabel.setFont(new Font(14));
        stateCodeLabel.setFont(new Font(14));
        venueText.setFont(new Font(17));
        venueName.setFont(new Font(14));
        venueLocation.setFont(new Font(14));
        venueAddress.setFont(new Font(14));
        weatherText.setFont(new Font(17));
        tempLabel.setFont(new Font(14));
        humidityLabel.setFont(new Font(14));
        windSpeedLabel.setFont(new Font(14));
        bottomVenueLabel.setFont(new Font(14));
        bottomWeatherLabel.setFont(new Font(14));

        // Event handler for the "Search" button
        searchButton.setOnAction(event -> fetchDataFromAPI());

        // setup scene
        this.scene = new Scene(root, 550, 500);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();
        this.stage.setResizable(false);
    } // start

    /**
     * Fetches venue information from the TicketmasterAPI based on the user's input.
     */
    private void fetchDataFromAPI() {
        // Extract user input
        String venueTerm = venueSearch.getText().trim();
        String stateCodeTerm = stateCodeSearch.getText().trim();

        // Encode venue term
        venueTerm = encodeVenueTerm(venueTerm);

        // Check if both venue and state code are provided
        if (!venueTerm.isEmpty() && !stateCodeTerm.isEmpty()) {
            // Build API URL
            String apiUrl = buildApiUrl(venueTerm, stateCodeTerm);

            // Make API request and handle response
            handleApiResponse(apiUrl);
        } // if
    } // fetchDataFromAPI

    /**
     * Encodes the venue term for use in the API request.
     *
     * @param venueTerm The venue term to be encoded.
     * @return The encoded venue term.
     */
    private String encodeVenueTerm(String venueTerm) {
        return URLEncoder.encode(venueTerm, StandardCharsets.UTF_8)
            .replace("+", "%20");
    } // encodeVenueTerm

    /**
     * Builds the Ticketmaster API URL based on venue and state code.
     *
     * @param venueTerm The encoded venue term.
     * @param stateCodeTerm The state code term.
     * @return The constructed Ticketmaster API URL.
     */
    private String buildApiUrl(String venueTerm, String stateCodeTerm) {
        return TICKETMASTER_ENDPOINT +
            "?apikey=" + TICKETMASTER_API_KEY +
            "&keyword=" + venueTerm +
            "&stateCode=" + stateCodeTerm +
            "&locale=*";
    } // buildApiUrl

    /**
     * Makes API request, handles reponse, and updates UI.
     *
     * @param apiUrl The API URL to make the request to.
     */
    private void handleApiResponse(String apiUrl) {
        try {
            // Make HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();
            //System.out.printf("VENUE REQUEST: %s\n", request);

            // Send the HTTP request and receive the response
            HttpResponse<String> response =
                HTTP_CLIENT.send(request, BodyHandlers.ofString());
            //System.out.printf("VENUE RESPONSE: %s\n", response);

            // Check if the request was successful (HTTP status code 200)
            if (response.statusCode() == 200) {
                handleSuccessfulApiResponse(response);
            } else {
                // Handle HTTP errors
                displayAlert("Error", "Failed to fetch venue information. HTTP status code: "
                    + response.statusCode());
            } // if
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } // try
    } // handleApiResponse

    /**
     * Handles a successful API response, parses JSON, and updates UI.
     *
     * @param response The successful API response.
     */
    private void handleSuccessfulApiResponse(HttpResponse<String> response) {
        // Parse the JSON response using Gson
        String body = response.body();
        Embedded embedded = GSON.fromJson(body, Embedded.class);

        // Access venue information from the parsed object
        if (embedded != null
            && embedded.venuesEmbedded != null
            && embedded.venuesEmbedded.venues != null
            && embedded.venuesEmbedded.venues.length > 0) {
            Venue firstVenue = embedded.venuesEmbedded.venues[0];

            // Access venue information name, city, state, location
            String venueName = firstVenue.name;
            String venueAddress = firstVenue.address.line1;
            String city = firstVenue.city.name;
            String state = firstVenue.state.name;

            // Call displayVenueInformation to update the UI
            displayVenueInformation(venueName, city, state, venueAddress);

            // Fetch weather information using latitude and longitude
            double latitude = Double.parseDouble(firstVenue.location.latitude);
            double longitude = Double.parseDouble(firstVenue.location.longitude);

            // Fetch weather information using latitude and longitude
            fetchWeatherData(latitude, longitude);
        } else {
            // Handle case where no venues were found
            displayAlert("Venue not found", "No venue found for the given name.");
        } // if
    } // handleSuccessfulApiResponse

    /**
     * Displays venue information in the UI.
     *
     * @param name The venue information to display.
     * @param city The city where the venue is located.
     * @param state The state where the venue is located.
     * @param address The address where the venue it located.
     */
    private void displayVenueInformation(String name, String city, String state, String address) {
        this.venueName.setText("Venue Name: " + name);
        this.venueLocation.setText("Location: " + city + ", "  + state);
        this.venueAddress.setText("Address: " + address);
    } // displayVenueInformation

    /**
     * Fetches weather information from an external API based on latitude and longitude coordinates
     * from the TicketmasterAPI.
     *
     * @param latitude The latitude coordinates of the venue's location.
     * @param longitude The longitude coordinates of the venue's location.
     */
    private void fetchWeatherData(double latitude, double longitude) {
        // Construct the weather API URL with latitude, longitude, and API Key
        String weatherApiUrl = WEATHER_ENDPOINT +
            "?lat=" + latitude +
            "&lon=" + longitude +
            "&apikey=" + WEATHER_API_KEY;
        // System.out.println(weatherApiUrl);

        try {
            // Make HTTP request for the weather API
            HttpRequest weatherRequest = HttpRequest.newBuilder()
                .uri(URI.create(weatherApiUrl))
                .header("X-Api-Key", WEATHER_API_KEY)
                .build();

            //System.out.printf("WEATHER REQUEST: %s\n", weatherRequest);

            // Send the HTTP request and receive the response
            HttpResponse<String> weatherResponse =
                HTTP_CLIENT.send(weatherRequest, BodyHandlers.ofString());
            //System.out.printf("WEATHER RESPONSE: %s\n", weatherResponse);
            // System.out.println("WEATHER RESPONSE BODY: " + weatherResponse.body());

            // Check if the request was successful (HTTP status code 200)
            if (weatherResponse.statusCode() == 200) {
                // Parse the weather response and update the UI as needed
                String weatherResponseBody = weatherResponse.body();
                parseWeatherInformation(weatherResponseBody);
            } else {
                // Handle weather API HTTP errors
                displayAlert("Error", "Failed to fetch weather information. HTTP status code: "
                    + weatherResponse.statusCode());
            } // if
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } // try
    } // fetchWeatherData

    /**
     * Parses the weather infromation from the JSON response and updates the UI accordingly.
     *
     * @param weatherResponse the JSON-formatted weather information response from the API.
     */
    private void parseWeatherInformation(String weatherResponse) {
        WeatherResponse weatherInfo = GSON.fromJson(weatherResponse, WeatherResponse.class);

        if (weatherInfo != null) {
            // Access weather information
            double temp = weatherInfo.temp;
            int cloudPercentage = weatherInfo.cloudPercentage;
            int humidity = weatherInfo.humidity;
            double windSpeed = weatherInfo.windSpeed;

            // Update UI components
            tempLabel.setText("Temperature: " + temp + " °C");
            cloudPercentageLabel.setText("Cloud Percentage: " + cloudPercentage + "%");
            humidityLabel.setText("Humidity: " + humidity + "%");
            windSpeedLabel.setText("Wind Speed: " + windSpeed + " m/s");
        } // if
    } // parseWeatherInformation


    /**
     * Displays an alert.
     *
     * @param title The title of the alert.
     * @param content The content of the alert.
     */
    private void displayAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    } // displayAlert

} // ApiApp
