import {
  Button,
  Container,
  Typography,
  Box,
  Card,
  CardContent,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import Header from "../components/Header";
import {
  GoogleMap,
  useLoadScript,
  Marker,
  Polyline,
} from "@react-google-maps/api";
import { Favorite, FavoriteBorder } from "@mui/icons-material";

const libraries = ["places"];

const Map = () => {
  const [position, setPosition] = useState({ lat: 46.0792, lng: 14.9366 });
  const [positions, setPositions] = useState([]);
  const [selectedPosition, setSelectedPosition] = useState(null);
  const [selectedAirport, setSelectedAirport] = useState(null);
  const [selectedFlights, setSelectedFlights] = useState(null);
  const [followedFlights, setFollowedFlights] = useState([]);
  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState("");

  const { isLoaded, loadError } = useLoadScript({
    googleMapsApiKey: "AIzaSyCDbwnaWOg-UHnB3Oen5PdHsynGZHf27gM",
    libraries,
  });

  const fetchAirports = async () => {
    try {
      const response = await fetch("http://localhost:3210/airport");
      const data = await response.json();
      fetchData(data);
    } catch (error) {
      console.error(error);
    }
  };

  const fetchData = async (airports) => {
    try {
      for (const airport of airports) {
        const response = await fetch(
          `http://localhost:3210/location/address/${airport.location.address}/${airport.location.city}`
        );
        const data = await response.json();
        const position = data[0].geometry.location;
        const id = data[0].id; // Extract the id from the response

        // Fetch flights for this location
        const flightsResponse = await fetch(
          `http://localhost:3210/flight/location/${id}`
        );
        const flightsData = await flightsResponse.json();
        // console.log(flightsData);

        setPositions((prev) => [
          ...prev,
          { position, airport, id, flights: flightsData },
        ]); // Include the flights in the object
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const intervalId = setInterval(() => {
      // This will trigger a re-render
      setPositions((prevPositions) => [...prevPositions]);
    }, 1000); // Update every 10 seconds

    return () => clearInterval(intervalId); // Cleanup interval on component unmount
  }, []);

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
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    checkAuth();
    fetchAirports();
  }, []);

  useEffect(() => {
    const fetchFollowedFlights = async () => {
      try {
        const response = await fetch(
          `http://localhost:3210/user/${userId}/followed-flights`
        );
        const data = await response.json();
        // console.log(data);
        setFollowedFlights(data);
      } catch (error) {
        console.error("Error fetching followed flights:", error);
      }
    };

    fetchFollowedFlights();
  }, [userId]);

  // Calculate the current position of the flight based on the current time and the departure/arrival times
  const calculatePosition = (
    departureTime,
    arrivalTime,
    startPosition,
    endPosition
  ) => {
    const twoHoursInMillis = 2 * 60 * 60 * 1000;
    const now = Date.now();
    const departureTimestamp = new Date(departureTime);
    departureTimestamp.setTime(departureTimestamp.getTime() - twoHoursInMillis); // Subtract 2 hours
    const arrivalTimestamp = new Date(arrivalTime);
    arrivalTimestamp.setTime(arrivalTimestamp.getTime() - twoHoursInMillis); // Subtract 2 hours

    // If current time is before departure, airplane is at departure position
    if (now < departureTimestamp) {
      return startPosition;
    }

    // If current time is after arrival, airplane is at arrival position
    if (now >= arrivalTimestamp) {
      return endPosition;
    }

    // Calculate fraction of time elapsed between departure and arrival
    const fractionElapsed =
      (now - departureTimestamp) / (arrivalTimestamp - departureTimestamp);

    // Interpolate position along flight path
    const lat =
      startPosition.lat +
      fractionElapsed * (endPosition.lat - startPosition.lat);
    const lng =
      startPosition.lng +
      fractionElapsed * (endPosition.lng - startPosition.lng);

    return { lat, lng };
  };

  const handleFollowToggle = async (flightId) => {
    try {
      const isFollowed = followedFlights.some(
        (flight) => flight._id === flightId
      );
      const url = isFollowed
        ? `http://localhost:3210/user/${userId}/unfollow-flight/${flightId}`
        : `http://localhost:3210/user/${userId}/follow-flight/${flightId}`;

      const response = await fetch(url, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        const updatedUser = await response.json();
        console.log(updatedUser.followedFlights);
        setFollowedFlights(updatedUser.followedFlights);
      } else {
        console.error("Failed to toggle follow flight status");
      }
    } catch (error) {
      console.error("Error toggling follow flight status:", error);
    }
  };

  const handleMarkerClick = (position, airport, flights) => {
    setSelectedPosition(position);
    setSelectedAirport(airport);
    setSelectedFlights(flights);
  };

  if (loadError) return <div>Error loading maps</div>;
  if (!isLoaded) return <div>Loading Maps</div>;

  return (
    <Container>
      <Header page="Zemljevid" />
      <Box sx={{ height: "90vh", display: "flex" }}>
        <Box sx={{ flex: 1 }}>
          <GoogleMap
            mapContainerStyle={{ width: "100%", height: "100%" }}
            zoom={9}
            center={position}
          >
            {positions.map((pos, index) => (
              <React.Fragment key={index}>
                <Marker
                  position={pos.position}
                  onClick={() =>
                    handleMarkerClick(pos.position, pos.airport, pos.flights)
                  }
                />
                {pos.flights.map((flight, flightIndex) => {
                  const currentPosition = calculatePosition(
                    flight.departureExact,
                    flight.arrivalExact,
                    pos.position,
                    {
                      lat: flight.destinationLat,
                      lng: flight.destinationLng,
                    }
                  );
                  return (
                    <React.Fragment key={flightIndex}>
                      <Polyline
                        path={[
                          pos.position,
                          {
                            lat: flight.destinationLat,
                            lng: flight.destinationLng,
                          },
                        ]}
                        options={{ strokeColor: "#FF0000", strokeWeight: 2 }}
                      />
                      <Marker
                        position={currentPosition}
                        icon={{
                          url: "http://maps.google.com/mapfiles/kml/shapes/airports.png",
                          scaledSize: new window.google.maps.Size(20, 20),
                        }}
                      />
                    </React.Fragment>
                  );
                })}
              </React.Fragment>
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
            {selectedFlights.map((flight) => (
              <Card key={flight.id} sx={{ my: 2 }}>
                <CardContent>
                  <Box
                    display="flex"
                    justifyContent="space-between"
                    alignItems="center"
                  >
                    <Typography>Destinacija: {flight.destination}</Typography>
                    <Button onClick={() => handleFollowToggle(flight._id)}>
                      {userId ? (
                        followedFlights.some(
                          (flightObj) => flightObj._id === flight._id
                        ) ? (
                          <Favorite />
                        ) : (
                          <FavoriteBorder />
                        )
                      ) : null}
                    </Button>
                  </Box>
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
                      new Date(flight.departureExact).toISOString().slice(0, -1)
                    ).toLocaleString("de-DE")}
                  </Typography>
                  <Typography>
                    Planiran prihod:{" "}
                    {new Date(
                      new Date(flight.arrivalPlanned).toISOString().slice(0, -1)
                    ).toLocaleString("de-DE")}
                  </Typography>
                  <Typography>
                    Točen prihod:{" "}
                    {new Date(
                      new Date(flight.arrivalExact).toISOString().slice(0, -1)
                    ).toLocaleString("de-DE")}
                  </Typography>
                  <Typography>Status: {flight.status}</Typography>
                </CardContent>
              </Card>
            ))}
          </Box>
        )}
      </Box>
    </Container>
  );
};

export default Map;
