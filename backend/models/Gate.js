const mongoose = require("mongoose");

const gateSchema = new mongoose.Schema(
  {
    label: {
      type: String,
      required: true,
      unique: true,
    },
  },
  { strict: "throw" }
);

const Gate = mongoose.model("gate", gateSchema);

module.exports = Gate;
