import React, { useState, useEffect } from "react";
import {
  Card,
  CardContent,
  Container,
  Typography,
  TextField,
} from "@mui/material";
import Header from "../components/Header";

const WeatherConditions = () => {
  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState("");
  const [locationDisplay, setLocationDisplay] = useState("");
  const [weatherConditions, setWeatherConditions] = useState([]);

  const fetchWeatherConditions = async (locationId) => {
    const response = await fetch(
      `http://localhost:3210/weatherCondition/location/${locationId}`
    );
    const data = await response.json();
    console.log(data);
    setWeatherConditions(data);
  };

  const checkAuth = async () => {
    try {
      const res = await fetch("http://localhost:3210/user/check", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      });

      if (res.ok) {
        const data = await res.json();
        setUserId(data._id);
        setUsername(data.username);
        if (data.defaultLocation) {
          console.log(data);
          setLocationDisplay(
            `${data.defaultLocation.city}, ${data.defaultLocation.address}`
          );
          await fetchWeatherConditions(data.defaultLocation._id);
        } else {
          console.log("No default location");
          await fetchWeatherConditions(undefined);
        }
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    checkAuth();
    fetchWeatherConditions();
  }, []);

  return (
    <Container>
      <Header page="Vremenski Pogoji" />
      <TextField
        disabled
        fullWidth
        id="outlined-basic"
        label={locationDisplay}
        variant="outlined"
      />
      <div>
        {weatherConditions.map((weatherCondition) => (
          <Card key={weatherCondition.id} sx={{ my: 2 }}>
            <CardContent>
              <Typography variant="h4">
                Lokacija: {weatherCondition.location.city},{" "}
                {weatherCondition.location.address}
              </Typography>
              <Typography variant="h6">
                Čas:{" "}
                {new Date(
                  new Date(weatherCondition.time).toISOString().slice(0, -1)
                ).toLocaleString("de-DE")}
              </Typography>
              <Typography variant="h6">
                Temperature: {weatherCondition.temperature}
              </Typography>
              <Typography variant="h6">
                Vlaga: {weatherCondition.humidity}
              </Typography>
              <Typography variant="h6">
                Veter: {weatherCondition.wind_speed}
              </Typography>
              <Typography variant="h6">
                Status: {weatherCondition.status}
              </Typography>
            </CardContent>
          </Card>
        ))}
      </div>
    </Container>
  );
};

export default WeatherConditions;
