import React, { useState } from "react";
import {
  Card,
  CardContent,
  CardHeader,
  TextField,
  Button,
  Grid,
  Alert,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const modalStyles = {
  content: {
    top: "5%",
    left: "50%",
    right: "auto",
    bottom: "auto",
    marginRight: "-50%",
    transform: "translate(-50%, -50%)",
    position: "fixed",
  },
  overlay: {
    background: "none",
  },
};

const Login = () => {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const handleLogin = async () => {
    if (username === "" || password === "") {
      setIsModalOpen(true);
      setErrorMessage("Prosim izpolni vsa polja");
      setTimeout(() => setIsModalOpen(false), 3000);
      return;
    }
    try {
      const res = await fetch("http://localhost:3210/user/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
        credentials: "include",
      });
      if (res.ok) navigate("/");
      else {
        setErrorMessage("Nepravilni prijavni podatki");
        setIsModalOpen(true);
        setTimeout(() => setIsModalOpen(false), 3000);
      }
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <Grid
      container
      direction="column"
      alignItems="center"
      justifyContent="center"
      minHeight="100vh"
    >
      <Card variant="outlined">
        <CardHeader title="Prijava" />
        <CardContent>
          <TextField
            label="Ime"
            variant="outlined"
            fullWidth
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            label="Geslo"
            variant="outlined"
            fullWidth
            margin="normal"
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <br />
          <br />
          <Button variant="contained" color="primary" onClick={handleLogin}>
            Prijava
          </Button>
        </CardContent>
      </Card>

      {isModalOpen && (
        <Alert severity="error" style={modalStyles.content}>
          {errorMessage}
        </Alert>
      )}
    </Grid>
  );
};

export default Login;
