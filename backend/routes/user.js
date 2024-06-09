const express = require("express");
const User = require("../models/User");
const jwt = require("jsonwebtoken");
const { generateJWT, checkJWT } = require("../helper-functions");
const bcrypt = require("bcrypt");

const router = express.Router();

// Get a user
router.get("find/:id", async (req, res) => {
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

// Create a user
router.post("/", async (req, res) => {
  try {
    const user = new User(req.body);
    await user.save();
    const token = generateJWT(user._id);
    res.cookie("jwt", token, { maxAge: 259200000 });
    res.status(200).json({ msg: "User created successfully" });
  } catch (err) {
    console.error(`An error has occurred while creating a user: ${err}`);
    res
      .status(500)
      .json({ msg: "An error has occurred while creating a user" });
  }
});

// Login with a user account
router.post("/login", async (req, res) => {
  try {
    const { email, password } = req.body;
    const user = await User.findOne({ email });

    if (!user || !(await bcrypt.compare(password, user.password))) {
      return res.status(401).json({ message: "Invalid email or password" });
    }

    const token = generateJWT(user._id);
    res.cookie("jwt", token, { maxAge: 259200000 });
    res
      .status(200)
      .json({ message: "You have been logged in", user: user._id });
  } catch (err) {
    console.error(err);
    res
      .status(500)
      .json({ message: "An error has occured while trying to log you in" });
  }
});

// Check if user is logged in
router.get("/check", async (req, res) => {
  try {
    const token = req.cookies.jwt;
    if (token) {
      jwt.verify(token, process.env.JWT_SECRET, async (err, decodedToken) => {
        if (!err) {
          const user = await User.findById(decodedToken.id).populate(
            "defaultLocation"
          );
          res.status(200).json(user);
        }
      });
    } else res.status(401).json({ message: "Not logged in" });
  } catch (err) {
    console.error(err);
  }
});

// Logout
router.get("/logout", (req, res) => {
  try {
    res.cookie("jwt", "", { maxAge: 1 });
    res.status(200).json({ message: "Logged out" });
  } catch (err) {
    console.error(err);
    res
      .status(500)
      .json({ message: "An error has occured while trying to log you out" });
  }
});

// Change default location
router.post("/changeDefaultLocation", async (req, res) => {
  try {
    const { userId, location } = req.body;
    const user = await User.findById(userId);
    if (user) {
      user.defaultLocation = location;
      await user.save();
      res.status(200).json({ message: "Default location changed" });
    } else {
      res.status(404).json({ message: "User not found" });
    }
  } catch (err) {
    if (
      err.message ===
      `Cast to ObjectId failed for value "" (type string) at path "_id" for model "user"`
    ) {
      return res.status(200);
    }
    console.error(
      `An error has occurred while changing default location: ${err}`
    );
    res.status(500).json({
      message: "An error has occurred while changing default location",
    });
  }
});

module.exports = router;
