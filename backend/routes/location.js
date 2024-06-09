const express = require("express");
const Location = require("../models/Location");
const { Client } = require("@googlemaps/google-maps-services-js");

const client = new Client({});

const router = express.Router();

// Get location by address
router.get("/address/:address/:city", async (req, res) => {
  try {
    const response = await client.geocode({
      params: {
        address: `${req.params.address}, ${req.params.city}`,
        key: "AIzaSyCDbwnaWOg-UHnB3Oen5PdHsynGZHf27gM",
      },
    });

    // Find the location in the database
    const location = await Location.findOne({ address: req.params.address });

    // Add the location's id to the geocoding results
    const resultsWithId = response.data.results.map((result) => ({
      ...result,
      id: location ? location._id : null,
    }));

    // console.log(resultsWithId);
    res.json(resultsWithId);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: error.message });
  }
});

// Create a new location
router.post("/", async (req, res) => {
  try {
    const location = new Location(req.body);
    await location.save();
    res.status(200).json(location);
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
});

// Get all locations
router.get("/", async (req, res) => {
  try {
    const locations = await Location.find();
    res.json(locations);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// Get a single location
router.get("/:id", getLocation, (req, res) => {
  res.json(res.location);
});

// Update a location
router.patch("/:id", getLocation, async (req, res) => {
  if (req.body.address != null) {
    res.location.address = req.body.address;
  }
  if (req.body.city != null) {
    res.location.city = req.body.city;
  }
  if (req.body.postalCode != null) {
    res.location.postalCode = req.body.postalCode;
  }
  try {
    const updatedLocation = await res.location.save();
    res.json(updatedLocation);
  } catch (error) {
    res.status(400).json({ message: error.message });
  }
});

// Delete a location
router.delete("/:id", getLocation, async (req, res) => {
  try {
    await res.location.remove();
    res.json({ message: "Location deleted" });
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
});

// Middleware function to get a single location by ID
async function getLocation(req, res, next) {
  try {
    const location = await Location.findById(req.params.id);
    if (location == null) {
      return res.status(404).json({ message: "Location not found" });
    }
    res.location = location;
    next();
  } catch (error) {
    return res.status(500).json({ message: error.message });
  }
}

module.exports = router;
