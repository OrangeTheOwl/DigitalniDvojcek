const mongoose = require("mongoose");

const weatherConditionSchema = new mongoose.Schema({
  time: {
    type: Date,
    required: true,
  },
  location: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "location",
    required: true,
  },
  temperature: {
    type: Number,
    required: true,
  },
  humidity: {
    type: Number,
    required: true,
  },
  wind_speed: {
    type: Number,
    required: true,
  },
  status: {
    type: String,
    enum: ["sončno", "oblačno", "dež", "megla"],
    required: true,
  },
});

const WeatherCondition = mongoose.model(
  "WeatherCondition",
  weatherConditionSchema
);

module.exports = WeatherCondition;
