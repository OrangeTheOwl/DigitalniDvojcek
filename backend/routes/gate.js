const express = require("express");
const Gate = require("../models/Gate");

const router = express.Router();

// Create a gate
router.post("/", async (req, res) => {
    try {
        const gate = new Gate(req.body);
        await gate.save();
        res.status(200).json({ msg: "Gate created successfully" });
    } catch (err) {
        console.error(`An error has occurred while creating a gate: ${err}`);
        res
            .status(500)
            .json({ msg: "An error has occurred while creating a gate" });
    }
});

// Get a gate
router.get("/:id", async (req, res) => {
    const id = req.params.id;
    try {
        const gate = await Gate.findById(id);
        if (gate) {
            res.status(200).json(gate);
        } else {
            res.status(404).json({ msg: "Gate not found" });
        }
    } catch (err) {
        console.error(`An error has occurred while getting a gate: ${err}`);
        res.status(500).json({ msg: "An error has occurred while getting a gate" });
    }
});

// Get all gates
router.get("/", async (req, res) => {
    try {
        const gates = await Gate.find();
        res.status(200).json(gates);
    } catch (err) {
        console.error(`An error has occurred while getting all gates: ${err}`);
        res
            .status(500)
            .json({ msg: "An error has occurred while getting all gates" });
    }
});

// Get id from label
router.get("/label/:label", async (req, res) => {
    const label = req.params.label;
    try {
        const gate = await Gate.findOne({ label: label });
        if (gate) {
            res.status(200).json(gate._id);
        } else {
            res.status(404).json({ msg: "Gate not found" });
        }
    } catch (err) {
        console.error(`An error has occurred while getting a gate: ${err}`);
        res.status(500).json({ msg: "An error has occurred while getting a gate" });
    }
});

module.exports = router;
