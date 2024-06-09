const mongoose = require("mongoose");

const trafficInfoSchema = new mongoose.Schema({
  time: {
    type: Date,
    required: true,
  },
  location: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "location",
    required: true,
  },
  delay: {
    type: Number,
    required: true,
  },
  status: {
    type: String,
    enum: ["brez zastojev", "zastoj", "nesreča", "delo na cesti"],
    required: true,
  },
});

const TrafficInfo = mongoose.model("TrafficInfo", trafficInfoSchema);

module.exports = TrafficInfo;
