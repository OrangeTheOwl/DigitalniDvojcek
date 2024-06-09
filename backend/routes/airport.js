const express = require("express");
const Airport = require("../models/Airport");

const router = express.Router();

// Create an airport
router.post("/", async (req, res) => {
  try {
    const airport = new Airport(req.body);
    await airport.save();
    res.status(200).json({ msg: "Airport created successfully" });
  } catch (err) {
    console.error(`An error has occurred while creating an airport: ${err}`);
    res
      .status(500)
      .json({ msg: "An error has occurred while creating an airport" });
  }
});

// Get an airport
router.get("/:id", async (req, res) => {
  const id = req.params.id;
  try {
    const airport = await Airport.findById(id);
    if (airport) {
      res.status(200).json(airport);
    } else {
      res.status(404).json({ msg: "Airport not found" });
    }
  } catch (err) {
    console.error(`An error has occurred while getting an airport: ${err}`);
    res
      .status(500)
      .json({ msg: "An error has occurred while getting an airport" });
  }
});

// Get all airports
router.get("/", async (req, res) => {
  try {
    let airports = await Airport.find();
    airports = await Promise.all(
      airports.map(async (airport) => {
        return await Airport.populate(airport, { path: "location gates" });
      })
    );
    res.status(200).json(airports);
  } catch (err) {
    console.error(`An error has occurred while getting all airports: ${err}`);
    res
      .status(500)
      .json({ msg: "An error has occurred while getting all airports" });
  }
});

module.exports = router;
