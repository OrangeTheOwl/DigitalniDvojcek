import React, { useState, useEffect } from "react";
import { Card, CardContent, Container, Typography } from "@mui/material";
import Header from "../components/Header";

const Flights = () => {
  const [flights, setFlights] = useState([]);

  const fetchFlights = async () => {
    const response = await fetch("http://localhost:3210/flight");
    const data = await response.json();
    console.log(data);
    setFlights(data);
  };

  useEffect(() => {
    fetchFlights();
  }, []);

  return (
    <Container>
      <Header page="Flights" />
      <div>
        {flights.map((flight) => (
          <Card key={flight.id} sx={{ my: 2 }}>
            <CardContent>
              <Typography variant="h4">
                Destinacija: {flight.destination}
              </Typography>
              <Typography variant="h6">
                Letališče: {flight.airport.name}
              </Typography>
              <Typography variant="h6">{flight.gate.label}</Typography>
              <Typography variant="h6">
                Planiran odhod:{" "}
                {new Date(
                  new Date(flight.departurePlanned).toISOString().slice(0, -1)
                ).toLocaleString("de-DE")}
              </Typography>
              <Typography variant="h6">
                Točen odhod:{" "}
                {new Date(
                  new Date(flight.departureExact).toISOString().slice(0, -1)
                ).toLocaleString("de-DE")}
              </Typography>
              <Typography variant="h6">
                Planiran prihod:{" "}
                {new Date(
                  new Date(flight.arrivalPlanned).toISOString().slice(0, -1)
                ).toLocaleString("de-DE")}
              </Typography>
              <Typography variant="h6">
                Točen prihod:{" "}
                {new Date(
                  new Date(flight.arrivalExact).toISOString().slice(0, -1)
                ).toLocaleString("de-DE")}
              </Typography>
              <Typography variant="h6">
                Število potnikov: {flight.numberOfPassengers}
              </Typography>
              <Typography variant="h6">Status: {flight.status}</Typography>
            </CardContent>
          </Card>
        ))}
      </div>
    </Container>
  );
};

export default Flights;
