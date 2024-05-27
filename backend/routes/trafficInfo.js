const express = require("express");
const TrafficInfo = require("../models/TrafficInfo");

const router = express.Router();

// Get all traffic info
router.get("/", async (req, res) => {
  try {
    const trafficInfo = await TrafficInfo.find();
    res.json(trafficInfo);
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
});

// Get a specific traffic info
router.get("/:id", getTrafficInfo, (req, res) => {
  res.json(res.trafficInfo);
});

// Create a new traffic info
router.post("/", async (req, res) => {
  const trafficInfo = new TrafficInfo(req.body);

  try {
    const newTrafficInfo = await trafficInfo.save();
    res.status(201).json(newTrafficInfo);
  } catch (err) {
    res.status(400).json({ message: err.message });
  }
});

// Update a specific traffic info
router.patch("/:id", getTrafficInfo, async (req, res) => {
  // Update the properties of the traffic info object based on the request body
  // For example: res.trafficInfo.property1 = req.body.property1;

  try {
    const updatedTrafficInfo = await res.trafficInfo.save();
    res.json(updatedTrafficInfo);
  } catch (err) {
    res.status(400).json({ message: err.message });
  }
});

// Delete a specific traffic info
router.delete("/:id", getTrafficInfo, async (req, res) => {
  try {
    await res.trafficInfo.remove();
    res.json({ message: "Traffic info deleted" });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
});

// Middleware function to get a specific traffic info by ID
async function getTrafficInfo(req, res, next) {
  let trafficInfo;
  try {
    trafficInfo = await TrafficInfo.findById(req.params.id);
    if (trafficInfo == null) {
      return res.status(404).json({ message: "Traffic info not found" });
    }
  } catch (err) {
    return res.status(500).json({ message: err.message });
  }

  res.trafficInfo = trafficInfo;
  next();
}

module.exports = router;
