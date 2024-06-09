import React, { useState, useEffect } from "react";
import { Card, CardContent, Container, Typography } from "@mui/material";
import Header from "../components/Header";

const Airports = () => {
  const [airports, setAirports] = useState([]);

  const fetchAirports = async () => {
    const response = await fetch("http://localhost:3210/airport");
    const data = await response.json();
    console.log(data);
    setAirports(data);
  };

  useEffect(() => {
    fetchAirports();
  }, []);

  return (
    <Container>
      <Header page="Airports" />
      <div>
        {airports.map((airport) => (
          <Card key={airport.id} sx={{ my: 2 }}>
            <CardContent>
              <Typography variant="h4">{airport.name}</Typography>
              <Typography variant="h6">
                {airport.location.postalCode}
              </Typography>
              <Typography variant="h6">{airport.location.city}</Typography>
              <Typography variant="h6">{airport.location.address}</Typography>
              <Typography variant="h5">Gates</Typography>
              {airport.gates.map((gate) => (
                <Typography key={gate.id}>{gate.label}</Typography>
              ))}
            </CardContent>
          </Card>
        ))}
      </div>
    </Container>
  );
};

export default Airports;
