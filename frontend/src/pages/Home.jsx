import React from "react";
import { Link } from "react-router-dom";
import Header from "../components/Header";
import { Container } from "@mui/material";

const Home = () => {
  return (
    <Container>
      <Header page="Home" />
    </Container>
  );
};

export default Home;
