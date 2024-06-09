import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  Box,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
} from "@mui/material";
import Header from "../components/Header";

const Profile = () => {
  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState("");
  const [allLocations, setAllLocations] = useState([]);
  const [locationId, setLocationId] = useState("");

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
      <Header page="Profile" />
      <Box sx={{ my: 5 }}>
        <Typography variant="h4" sx={{ mb: 3 }}>
          Username: {username}
        </Typography>
        <FormControl fullWidth>
          <InputLabel id="demo-simple-select-label">
            Privzeta lokacija
          </InputLabel>
          <Select
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={locationId}
            label="Age"
            onChange={handleChange}
          >
            {allLocations.map((location) => (
              <MenuItem value={location._id}>
                {location.city}, {location.address}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Box>
    </Container>
  );
};

export default Profile;
