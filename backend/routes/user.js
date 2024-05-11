const express = require("express");
const User = require("../models/User");

const router = express.Router();

// Create a user
router.post("/", async (req, res) => {
  try {
    const user = new User(req.body);
    await user.save();
    res.status(200).json({ msg: "User created successfully" });
  } catch (err) {
    console.error(`An error has occurred while creating a user: ${err}`);
    res
      .status(500)
      .json({ msg: "An error has occurred while creating a user" });
  }
});

// Get a user
router.get("/:id", async (req, res) => {
  const id = req.params.id;
  try {
    const user = await User.findById(id);
    if (user) {
      res.status(200).json(user);
    } else {
      res.status(404).json({ msg: "User not found" });
    }
  } catch (err) {
    console.error(`An error has occurred while getting a user: ${err}`);
    res.status(500).json({ msg: "An error has occurred while getting a user" });
  }
});

// Get all users
router.get("/", async (req, res) => {
  try {
    const users = await User.find();
    res.status(200).json(users);
  } catch (err) {
    console.error(`An error has occurred while getting all users: ${err}`);
    res
      .status(500)
      .json({ msg: "An error has occurred while getting all users" });
  }
});

module.exports = router;
