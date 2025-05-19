# Venue Weather Finder

## Overview

Venue Weather Finder is a JavaFX application that integrates two external RESTful JSON APIs to provide users with weather information based on venue locations. The application allows users to search for venues using the venue name and state code, fetch location coordinates from the Ticketmaster API, and then retrieve real-time weather information for that location using the API Ninjas Weather API.

## Features

- Search for venues by name and state code
- Display venue details (name, location, address)
- Show current weather conditions at the venue location:
  - Temperature
  - Cloud percentage
  - Humidity
  - Wind speed

## Technologies Used

- Java 17
- JavaFX for the user interface
- Maven for dependency management
- Google Gson for JSON parsing
- HTTP Client for API requests
- External APIs:
  - Ticketmaster API for venue information
  - API Ninjas for weather data

## Requirements

- Java Development Kit (JDK) 17 or higher
- Maven 3.9.0 or higher
- API keys for:
  - Ticketmaster API
  - API Ninjas Weather API

## Setup

1. Clone the repository:

   ```
   git clone https://github.com/aal51282/venue-weather-finder
   cd venue-weather-finder
   ```

2. Create a `config.properties` file in the `resources` directory with your API keys:

   ```
   ticketmaster.apikey=your_ticketmaster_api_key
   weather.apikey=your_api_ninjas_key
   ```

3. Build the application:

   ```
   mvn clean compile
   ```

4. Run the application:
   ```
   ./run.sh
   ```
   Alternatively:
   ```
   mvn exec:exec -Dexec.mainClass=cs1302uga.api/cs1302.api.ApiDriver
   ```

## How to Use

1. Enter a venue name in the "Venue" text field (e.g., "Georgia Theatre")
2. Enter the state code in the "State Code" text field (e.g., "GA")
3. Click the "Search" button
4. The application will display venue information and current weather conditions at that location

## Acknowledgments

- Ticketmaster API for venue data
- API Ninjas for weather data
