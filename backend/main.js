// Libraries
const express = require("express");
require("dotenv").config();

// Routes

// Global variables
const port = process.env.DEV_PORT;

// Basic setup
const app = express();

app.get("/", (req, res) => {
  res.send("You can access the API");
});

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
