import { Box, Typography } from "@mui/material";
import React, { useState } from "react";
import { Link } from "react-router-dom";

// Icons
import MapIcon from "@mui/icons-material/Map";
import BarChartIcon from "@mui/icons-material/BarChart";
import TrafficIcon from "@mui/icons-material/Traffic";
import WbSunnyIcon from "@mui/icons-material/WbSunny";
import ThermostatIcon from "@mui/icons-material/Thermostat";
import PersonIcon from "@mui/icons-material/Person";
import FlightTakeoffIcon from "@mui/icons-material/FlightTakeoff";

const Header = ({ page }) => {
  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState("");
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

  const handleLogout = async () => {
    try {
      const res = await fetch("http://localhost:3210/user/logout", {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
      });

      if (res.ok) {
        window.location.href = "/";
      }
    } catch (err) {
      console.error(err);
    }
  };

  useState(() => {
    checkAuth();
  }, []);

  if (userId !== "") {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          my: 2,
          alignItems: "center",
        }}
      >
        <Typography variant="h3">{page}</Typography>
        <Box sx={{ display: "flex", gap: 2 }}>
          <Link to="/">
            <MapIcon />
          </Link>
          <Link to="/graph">
            <BarChartIcon />
          </Link>
          <Link to="/trafficInfo">
            <TrafficIcon />
          </Link>
          <Link to="/weatherConditions">
            <WbSunnyIcon />
          </Link>
          <Link to="/profile">
            <PersonIcon />
          </Link>
          <Typography>{username}</Typography>
          <Link to="/" onClick={handleLogout}>
            Odjava
          </Link>
        </Box>
      </Box>
    );
  } else {
    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          my: 2,
          alignItems: "center",
        }}
      >
        <Typography variant="h3">{page}</Typography>
        <Box sx={{ display: "flex", gap: 2 }}>
          <Link to="/">
            <MapIcon />
          </Link>
          <Link to="/graph">
            <BarChartIcon />
          </Link>
          <Link to="/trafficInfo">
            <TrafficIcon />
          </Link>
          <Link to="/weatherConditions">
            <WbSunnyIcon />
          </Link>
          <Link to="/login">Prijava</Link>
          <Link to="/register">Registracija</Link>
        </Box>
      </Box>
    );
  }
};

export default Header;
