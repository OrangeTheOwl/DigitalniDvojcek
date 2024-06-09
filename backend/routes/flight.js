const express = require("express");
const Flight = require("../models/Flight");

const router = express.Router();

// Create a flight
router.post("/", async (req, res) => {
  try {
    const flight = new Flight(req.body);
    await flight.save();
    res.status(200).json({ msg: "Flight created successfully" });
  } catch (err) {
    console.error(`An error has occurred while creating a flight: ${err}`);
    res
      .status(500)
      .json({ msg: "An error has occurred while creating a flight" });
  }
});

// Get a flight
router.get("/:id", async (req, res) => {
  const id = req.params.id;
  try {
    const flight = await Flight.findById(id);
    if (flight) {
      res.status(200).json(flight);
    } else {
      res.status(404).json({ msg: "Flight not found" });
    }
  } catch (err) {
    console.error(`An error has occurred while getting a flight: ${err}`);
    res
      .status(500)
      .json({ msg: "An error has occurred while getting a flight" });
  }
});

// Get all flights
router.get("/", async (req, res) => {
  try {
    let flights = await Flight.find();
    flights = await Promise.all(
      flights.map(async (flight) => {
        return await Flight.populate(flight, { path: "airport gate" });
      })
    );
    res.status(200).json(flights);
  } catch (err) {
    console.error(`An error has occurred while getting all flights: ${err}`);
    res
      .status(500)
      .json({ msg: "An error has occurred while getting all flights" });
  }
});

// Get a flight by airport's location id
router.get("/location/:id", async (req, res) => {
  const id = req.params.id;
  try {
    let flights = await Flight.find();
    flights = await Promise.all(
      flights.map(async (flight) => {
        return await Flight.populate(flight, { path: "airport gate" });
      })
    );
    flights = flights.filter((flight) => flight.airport.location == id);

    console.log(flights);
    res.status(200).json(flights);
  } catch (err) {
    console.error(
      `An error has occurred while getting all flights by location id: ${err}`
    );
    res.status(500).json({
      msg: "An error has occurred while getting all flights by location id",
    });
  }
});

module.exports = router;
