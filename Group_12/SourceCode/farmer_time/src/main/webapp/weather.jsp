<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Weather Data App</title>
    <style>
      html,
      body {
        height: 100%;
        margin: 10px;
        padding: 10px;
        background-color: #353535;
        color: #f0f0f0;
      }
    </style>
  </head>
  <body>
    <h3>Welcome to the Weather App!</h3>
    <br />
    <form action="getData" method="post">
      Enter city: <input type="text" name="city" />
      <input type="submit" style="color: black" />
    </form>
    <br />
    <h1>Weather in ${weather.city}</h1>
    <p>Temperature: ${weather.temperature} &#8451;</p>
    <p>Description: ${weather.description}</p>
    <p>Humidity: ${weather.humidity}%</p>
  </body>
</html>
