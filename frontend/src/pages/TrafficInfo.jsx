import React, { useState, useEffect } from "react";
import {
  Card,
  CardContent,
  Container,
  Typography,
  TextField,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
} from "@mui/material";
import Header from "../components/Header";

const TrafficInfo = () => {
  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState("");
  const [locationDisplay, setLocationDisplay] = useState("");
  const [traficInfos, setTrafficInfos] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState("");

  const handleChange = (event) => {
    setSelectedStatus(event.target.value);
  };

  const fetchTrafficInfos = async (locationId) => {
    const response = await fetch(
      `http://localhost:3210/trafficInfo/location/${locationId}`
    );
    const data = await response.json();
    console.log(data);
    setTrafficInfos(data);
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
        console.log(data);
        setUserId(data._id);
        setUsername(data.username);

        if (data.defaultLocation) {
          await fetchTrafficInfos(data.defaultLocation._id);
          setLocationDisplay(
            `${data.defaultLocation.city}, ${data.defaultLocation.address}`
          );
        } else {
          await fetchTrafficInfos(undefined);
        }
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    checkAuth();
    // fetchTrafficInfos();
  }, []);

  return (
    <Container>
      <Header page="Traffic Info" />
      <TextField
        disabled
        fullWidth
        id="outlined-basic"
        label={locationDisplay}
        variant="outlined"
        sx={{ my: 2 }}
      />
      <FormControl fullWidth>
        <InputLabel id="demo-simple-select-label">
          Filtriraj po statusu
        </InputLabel>
        <Select
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          value={selectedStatus}
          label="Filtriraj po statusu"
          onChange={handleChange}
        >
          <MenuItem value="">Vsi statusi</MenuItem>
          <MenuItem value="brez zastojev">Brez zastojev</MenuItem>
          <MenuItem value="zastoj">Zastoj</MenuItem>
          <MenuItem value="nesreča">Nesreča</MenuItem>
          <MenuItem value="delo na cesti">Delo na cesti</MenuItem>
        </Select>
      </FormControl>
      <div>
        {traficInfos
          .filter((info) => !selectedStatus || info.status === selectedStatus)
          .map((trafficInfo) => (
            <Card key={trafficInfo.id} sx={{ my: 2 }}>
              <CardContent>
                <Typography variant="h4">
                  Lokacija: {trafficInfo.location.city},{" "}
                  {trafficInfo.location.address}
                </Typography>
                <Typography variant="h6">
                  Čas:{" "}
                  {new Date(
                    new Date(trafficInfo.time).toISOString().slice(0, -1)
                  ).toLocaleString("de-DE")}
                </Typography>
                <Typography variant="h6">
                  Status: {trafficInfo.status}
                </Typography>
                <Typography variant="h6">
                  Zamuda: {trafficInfo.delay} minut
                </Typography>
              </CardContent>
            </Card>
          ))}
      </div>
    </Container>
  );
};

export default TrafficInfo;
