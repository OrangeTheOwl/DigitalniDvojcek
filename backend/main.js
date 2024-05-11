// Libraries
const express = require("express");
const mongoose = require("mongoose");
require("dotenv").config();

// Global variables
const port = process.env.DEV_PORT;

// Basic setup
const app = express();
app.use(express.json());

// // Routes
const userRoute = require("./routes/user");
const gateRoute = require("./routes/gate");
const flightRoute = require("./routes/flight");
const airportRoute = require("./routes/airport");

app.use("/user", userRoute);
app.use("/gate", gateRoute);
app.use("/flight", flightRoute);
app.use("/airport", airportRoute);

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
