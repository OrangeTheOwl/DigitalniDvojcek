const mongoose = require("mongoose");

const flightSchema = new mongoose.Schema(
  {
    arrival: { type: Date, required: true },
    departurePlanned: { type: Date, required: true },
    departureExact: { type: Date, required: true },
    departureChangeTime: { type: Number, required: true },
    numberOfPassengers: { type: Number, required: true },
    status: { type: String, required: true },
    destination: { type: String, required: true },
    airport: { type: mongoose.Schema.Types.ObjectId, ref: "airport" },
    gate: { type: mongoose.Schema.Types.ObjectId, ref: "gate" },
  },
  { strict: "throw" }
);

const Flight = mongoose.model("flight", flightSchema);

module.exports = Flight;
