const mongoose = require("mongoose");

const flightSchema = new mongoose.Schema(
  {
    arrival: { type: Date, required: true },
    departure: { type: Date, required: true },
    delay: { type: Number, required: true },
    numberOfPassengers: { type: Number, required: true },
    cancelled: { type: Boolean, required: true },
    destination: { type: String, required: true },
    airport: { type: mongoose.Schema.Types.ObjectId, ref: "airport" },
    gate: { type: mongoose.Schema.Types.ObjectId, ref: "gate" },
  },
  { strict: "throw" }
);

const Flight = mongoose.model("flight", flightSchema);

module.exports = Flight;
