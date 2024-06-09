const mongoose = require("mongoose");

const flightSchema = new mongoose.Schema(
  {
    arrivalPlanned: { type: Date },
    arrivalExact: { type: Date },
    departurePlanned: { type: Date },
    departureExact: { type: Date },
    changeTime: { type: Number },
    status: { type: String },
    destination: { type: String },
    airport: { type: mongoose.Schema.Types.ObjectId, ref: "airport" },
    gate: { type: mongoose.Schema.Types.ObjectId, ref: "gate" },
    airline: { type: String },
    flightNumber: { type: String },
  },
  { strict: "throw" }
);

const Flight = mongoose.model("flight", flightSchema);

module.exports = Flight;
