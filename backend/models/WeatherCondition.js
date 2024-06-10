const mongoose = require("mongoose");

const weatherConditionSchema = new mongoose.Schema({
  time: {
    type: Date,
  },
  location: {
    type: mongoose.Schema.Types.ObjectId,
    ref: "location",
  },
  temperature: {
    type: Number,
  },
  humidity: {
    type: Number,
  },
  humidityStatus: {
    type: String,
  },
  windSpeed: {
    type: Number,
  },
  windStatus: {
    type: String,
  },
  status: {
    type: String,
    enum: ["sončno", "oblačno", "dež", "megla"],
  },
});

const WeatherCondition = mongoose.model(
  "WeatherCondition",
  weatherConditionSchema
);

module.exports = WeatherCondition;
