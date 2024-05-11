const mongoose = require("mongoose");

const airportSchema = new mongoose.Schema(
  {
    location: {
      address: { type: String, required: true },
      city: { type: String, required: true },
      postalCode: { type: String, required: true },
    },
    gates: [{ type: mongoose.Schema.Types.ObjectId, ref: "gate" }],
  },
  { strict: "throw" }
);

const Airport = mongoose.model("airport", airportSchema);

module.exports = Airport;
