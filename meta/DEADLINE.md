# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

> Please provide a friendly description of your app, including
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

The ApiApp provides users with weather information based on venue locations. The application allows users to
input a venue name and state code, fetch the latitude and longitude from the first API and use this
information to obtain the latest weather details from the second API.
After a venue name and state code are input from the user, the app returns the venue name, location, and
address, as well as, the temperature, cloud percentage, humidity, and wind speed of that venue.
API 1 - Ticketmaster API https://developer.ticketmaster.com/products-and-docs/apis/discovery-api/v2/#anchor_get - Gives venue information including coordinates that are used to create the second API URL
API 2 - API Ninjas - Weather API https://api-ninjas.com/api/weather - Gives additional information not
given by the first API. This allows a user to be prepared for the weather conditions when going to an event.
https://github.com/aal51282/cs1302-api-app.git

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
https://app.ticketmaster.com/discovery/v2/venues.json?apikey=P5sQkOAV8W74oWGNQXhmgWbyWbGtnCmi&keyword=Georgia%20Theatre&stateCode=GA&locale=*
```
### API 2

```
https://api.api-ninjas.com/v1/weather?lat=33.9583371&lon=-83.3772112&apikey=MSwLXVb0/qLmR2Yd0LosLQ==uPFPvNy17FcUQDc1
```
The above link is how my application requests the weather API. However, if you wanted to type this into a URL,
then "apikey" must be changed to "X-Api-Key" as shown below.
https://api.api-ninjas.com/v1/weather?lat=33.9583371&lon=-83.3772112&X-Api-Key=MSwLXVb0/qLmR2Yd0LosLQ==uPFPvNy17FcUQDc1

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

I learned how to do certain JavaFX UI applications, such as setFont and Java components, which helped me become better at making my applications more user friendly. I also learned a lot about HTTP and JSON because we were required to make our own custom Java classes as opposed to project 4. I also learned a lot about GIT and howto use the web version as well as a deeper understanding of its use on Odin.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

I would have done more research about the API's I wanted to use. I am satisfied with my choices, but if my weather API had more information on precipitation information, then I could inform the user with more weather conditions such as rain and rain percentage.