// Libraries
const express = require("express");
const mongoose = require("mongoose");
require("dotenv").config();

// Routes

// Global variables
const port = process.env.DEV_PORT;

// Basic setup
const app = express();

// // Connect to MongoDB
mongoose
  .connect(process.env.MONGODB_CONNECTION_STRING, { dbName: "website" })
  .then(() => {
    console.log("Connected to MongoDB");
  })
  .catch((error) => {
    console.error("Failed to connect to MongoDB:", error);
  });

// // Base routes
app.get("/", (req, res) => {
  res.send("You can access the API");
});

app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
