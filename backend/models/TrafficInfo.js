const mongoose = require("mongoose");

const trafficInfoSchema = new mongoose.Schema({
  time: {
    type: Date,
    required: true,
  },
  location: {
    type: String,
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
