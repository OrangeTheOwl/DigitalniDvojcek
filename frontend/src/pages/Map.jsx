import { Button, Container, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import {
  APIProvider,
  Map as GoogleMap,
  AdvancedMarker,
  Pin,
} from "@vis.gl/react-google-maps";
import { Box, Card, CardContent } from "@mui/material";

const Map = () => {
  const [position, setPosition] = useState({ lat: 46.0792, lng: 14.9366 });
  const [positions, setPositions] = useState([]);
  const [selectedPosition, setSelectedPosition] = useState(null); // State to track the selected marker
  const [selectedAirport, setSelectedAirport] = useState(null); // State to track the selected airport info
  const [selectedFlights, setSelectedFlights] = useState(null); // State to track the selected flights

  const fetchAirports = async () => {
    try {
      const response = await fetch("http://localhost:3210/airport");
      const data = await response.json();
      // console.log(data);
      fetchData(data);
    } catch (error) {
      console.error(error);
    }
  };

  const fetchData = async (airports) => {
    try {
      airports.forEach(async (airport) => {
        const response = await fetch(
          `http://localhost:3210/location/address/${airport.location.address}/${airport.location.city}`
        );
        const data = await response.json();
        // console.log(data);
        const position = data[0].geometry.location;
        const id = data[0].id; // Extract the id from the response

        // Fetch flights for this location
        const flightsResponse = await fetch(
          `http://localhost:3210/flight/location/${id}`
        );
        const flightsData = await flightsResponse.json();
        console.log(flightsData);

        setPositions((prev) => [
          ...prev,
          { position, airport, id, flights: flightsData },
        ]); // Include the flights in the object
      });
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    fetchAirports();
  }, []);

  useEffect(() => {
    console.log(selectedFlights);
  }, selectedFlights);

  const handleMarkerClick = (position, airport, flights) => {
    setSelectedPosition(position);
    setSelectedAirport(airport);
    setSelectedFlights(flights);
  };

  return (
    <APIProvider apiKey="AIzaSyCDbwnaWOg-UHnB3Oen5PdHsynGZHf27gM">
      <Container>
        <Header page="Map" />
        <Box sx={{ height: "90vh", display: "flex" }}>
          <Box sx={{ flex: 1 }}>
            <GoogleMap
              defaultZoom={9}
              defaultCenter={position}
              mapId="7578437571083928"
            >
              {positions.map((pos, index) => (
                <AdvancedMarker
                  key={index}
                  position={pos.position}
                  onClick={() =>
                    handleMarkerClick(pos.position, pos.airport, pos.flights)
                  } // Set the selected position and airport
                ></AdvancedMarker>
              ))}
            </GoogleMap>
          </Box>
          {selectedPosition && selectedAirport && (
            <Box
              sx={{
                width: "350px",
                background: "#222",
                borderLeft: "1px solid #ccc",
                padding: "10px",
                overflowY: "auto",
                height: "100%",
              }}
            >
              <Button
                variant="outlined"
                sx={{ padding: "0", mb: "0.5rem" }}
                color="error"
                onClick={() => setSelectedPosition(null)}
              >
                X
              </Button>
              <Typography>{selectedAirport.name}</Typography>
              <Typography>{selectedAirport.location.city}</Typography>
              <Typography>{selectedAirport.location.address}</Typography>
              <hr />
              <Typography variant="h6">Leti</Typography>
              {selectedFlights.map((flight, index) => (
                <Card key={flight.id} sx={{ my: 2 }}>
                  <CardContent>
                    <Typography>Destinacija: {flight.destination}</Typography>
                    <Typography>{flight.gate.label}</Typography>
                    <Typography>
                      Planiran odhod:{" "}
                      {new Date(
                        new Date(flight.departurePlanned)
                          .toISOString()
                          .slice(0, -1)
                      ).toLocaleString("de-DE")}
                    </Typography>
                    <Typography>
                      Točen odhod:{" "}
                      {new Date(
                        new Date(flight.departureExact)
                          .toISOString()
                          .slice(0, -1)
                      ).toLocaleString("de-DE")}
                    </Typography>
                    <Typography>
                      Planiran prihod:{" "}
                      {new Date(
                        new Date(flight.arrivalPlanned)
                          .toISOString()
                          .slice(0, -1)
                      ).toLocaleString("de-DE")}
                    </Typography>
                    <Typography>
                      Točen prihod:{" "}
                      {new Date(
                        new Date(flight.arrivalExact).toISOString().slice(0, -1)
                      ).toLocaleString("de-DE")}
                    </Typography>
                    <Typography>
                      Število potnikov: {flight.numberOfPassengers}
                    </Typography>
                    <Typography>Status: {flight.status}</Typography>
                  </CardContent>
                </Card>
              ))}
            </Box>
          )}
        </Box>
      </Container>
    </APIProvider>
  );
};

export default Map;
