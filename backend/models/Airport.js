const mongoose = require("mongoose");

const airportSchema = new mongoose.Schema(
  {
    name: { type: String, required: true },
    location: { type: mongoose.Schema.Types.ObjectId, ref: "location" },
    gates: [{ type: mongoose.Schema.Types.ObjectId, ref: "gate" }],
  },
  { strict: "throw" }
);

const Airport = mongoose.model("airport", airportSchema);

module.exports = Airport;
