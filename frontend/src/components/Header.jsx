import { Box, Typography } from "@mui/material";
import React from "react";
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
        <Link to="/map">
          <MapIcon />
        </Link>
        <Link to="/trafficInfo">
          <TrafficIcon />
        </Link>
        <Link to="/weatherConditions">
          <WbSunnyIcon />
        </Link>
        <Link to="/graph">
          <BarChartIcon />
        </Link>
        <Link to="/profile">
          <PersonIcon />
        </Link>
        <Link to="/login">Login</Link>
        <Link to="/register">Register</Link>
      </Box>
    </Box>
  );
};

export default Header;
