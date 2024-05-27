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
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [repeatPassword, setRepeatPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleRegister = async () => {
    if (
      username === "" ||
      password === "" ||
      repeatPassword === "" ||
      email === ""
    ) {
      setErrorMessage("Prosim izpolni vsa polja");
      setIsModalOpen(true);
      setTimeout(() => setIsModalOpen(false), 3000);
      return;
    }
    if (password !== repeatPassword) {
      setErrorMessage("Gesli se ne ujemata");
      setIsModalOpen(true);
      setTimeout(() => setIsModalOpen(false), 3000);
      return;
    }
    if (password.length < 8) {
      setErrorMessage("Geslo mora vsebovati vsaj 8 znakov");
      setIsModalOpen(true);
      setTimeout(() => setIsModalOpen(false), 3000);
      return;
    }
    try {
      const res = await fetch("http://localhost:3210/user", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, email, password }),
        credentials: "include",
      });
      if (res.ok) navigate("/");
      else {
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
        <CardHeader title="Registracija" />
        <CardContent>
          <TextField
            label="Uporabniško ime"
            variant="outlined"
            fullWidth
            margin="normal"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField
            label="E-pošta"
            variant="outlined"
            fullWidth
            margin="normal"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
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
          <TextField
            label="Ponovi geslo"
            variant="outlined"
            fullWidth
            margin="normal"
            type="password"
            value={repeatPassword}
            onChange={(e) => setRepeatPassword(e.target.value)}
          />
          <br />
          <br />
          <Button variant="contained" color="primary" onClick={handleRegister}>
            Registracija
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
