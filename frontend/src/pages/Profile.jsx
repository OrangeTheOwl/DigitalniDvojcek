import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  Box,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Card,
  CardContent,
} from "@mui/material";
import Header from "../components/Header";

const Profile = () => {
  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState("");
  const [allLocations, setAllLocations] = useState([]);
  const [locationId, setLocationId] = useState("");
  const [followedFlights, setFollowedFlights] = useState([]);

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
        setLocationId(data.defaultLocation._id);
        setFollowedFlights(data.followedFlights); // Set followed flights
      }
    } catch (err) {
      console.error(err);
    }
  };

  const getAllLocations = async () => {
    try {
      const res = await fetch("http://localhost:3210/location/", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      });

      if (res.ok) {
        const data = await res.json();
        setAllLocations(data);
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleChange = async (e) => {
    setLocationId(e.target.value);
  };

  const changeDefaultLocation = async () => {
    try {
      const res = await fetch(
        "http://localhost:3210/user/changeDefaultLocation",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ userId, location: locationId }),
          credentials: "include",
        }
      );

      if (res.ok) {
        console.log("Default location changed");
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    checkAuth();
    getAllLocations();
  }, []);

  useEffect(() => {
    changeDefaultLocation();
  }, [locationId]);

  return (
    <Container>
      <Header page="Profil" />
      <Box sx={{ my: 5 }}>
        <FormControl fullWidth>
          <InputLabel id="demo-simple-select-label">
            Privzeta lokacija
          </InputLabel>
          <Select
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={locationId}
            label="Privzeta lokacija"
            onChange={handleChange}
          >
            {allLocations.map((location) => (
              <MenuItem key={location._id} value={location._id}>
                {location.city}, {location.address}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        {/* Render Followed Flights */}
        <Box sx={{ mt: 5 }}>
          <Typography variant="h4" sx={{ mb: 2 }}>
            Priljubljeni leti
          </Typography>
          {followedFlights.length > 0 ? (
            followedFlights.map((flight) => (
              <Card key={flight._id} sx={{ mb: 2 }}>
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
            ))
          ) : (
            <Typography>No followed flights</Typography>
          )}
        </Box>
      </Box>
    </Container>
  );
};

export default Profile;
