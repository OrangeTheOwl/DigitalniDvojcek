const express = require('express');
const WeatherCondition = require('../models/WeatherCondition');

const router = express.Router();

// Create a new weather condition
router.post('/', async (req, res) => {
    try {
        const weatherCondition = new WeatherCondition(req.body);
        await weatherCondition.save();
        res.status(201).json(weatherCondition);
    } catch (err) {
        res.status(400).json({ message: err.message });
    }
});

// Get all weather conditions
router.get('/', async (req, res) => {
    try {
        const weatherConditions = await WeatherCondition.find();
        res.json(weatherConditions);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

// Get a specific weather condition
router.get('/:id', getWeatherCondition, (req, res) => {
    res.json(res.weatherCondition);
});

// Update a weather condition
router.patch('/:id', getWeatherCondition, async (req, res) => {
    if (req.body.name != null) {
        res.weatherCondition.name = req.body.name;
    }
    if (req.body.description != null) {
        res.weatherCondition.description = req.body.description;
    }
    try {
        const updatedWeatherCondition = await res.weatherCondition.save();
        res.json(updatedWeatherCondition);
    } catch (err) {
        res.status(400).json({ message: err.message });
    }
});

// Delete a weather condition
router.delete('/:id', getWeatherCondition, async (req, res) => {
    try {
        await res.weatherCondition.remove();
        res.json({ message: 'Weather condition deleted' });
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

// Middleware function to get a specific weather condition by ID
async function getWeatherCondition(req, res, next) {
    let weatherCondition;
    try {
        weatherCondition = await WeatherCondition.findById(req.params.id);
        if (weatherCondition == null) {
            return res.status(404).json({ message: 'Weather condition not found' });
        }
    } catch (err) {
        return res.status(500).json({ message: err.message });
    }

    res.weatherCondition = weatherCondition;
    next();
}

module.exports = router;