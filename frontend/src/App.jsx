import logo from "./logo.svg";
import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import CssBaseline from "@mui/material/CssBaseline";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Airports from "./pages/Airports";
import Flights from "./pages/Flights";
import TrafficInfo from "./pages/TrafficInfo";
import WeatherConditions from "./pages/WeatherConditions";
import Profile from "./pages/Profile";
import Map from "./pages/Map";
import Graph from "./pages/Graph";

const darkTheme = createTheme({
  palette: {
    mode: "dark",
    //   primary: {
    //     main: "#90caf9",
    //   },
    //   secondary: {
    //     main: "#f48fb1",
    //   },
  },
});

function App() {
  return (
    <ThemeProvider theme={darkTheme}>
      <CssBaseline />
      <Router>
        <Routes>
          <Route path="/" Component={Home} />
          <Route path="/login" Component={Login} />
          <Route path="/register" Component={Register} />
          <Route path="/airports" Component={Airports} />
          <Route path="/flights" Component={Flights} />
          <Route path="/trafficInfo" Component={TrafficInfo} />
          <Route path="/weatherConditions" Component={WeatherConditions} />
          <Route path="/profile" Component={Profile} />
          <Route path="/map" Component={Map} />
          <Route path="/graph" Component={Graph} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;
