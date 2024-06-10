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
    const { username, password } = req.body;
    console.log(username, password);
    const user = await User.findOne({ username });
    console.log(user);

    if (!user || !(await bcrypt.compare(password, user.password))) {
      return res.status(401).json({ message: "Invalid username or password" });
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
            "defaultLocation followedFlights"
          );
          for (flight of user.followedFlights) {
            await flight.populate("gate");
          }
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

// Add a flight to the followed flights
router.post("/:userId/follow-flight/:flightId", async (req, res) => {
  const { userId, flightId } = req.params;
  try {
    const user = await User.findById(userId);
    if (!user) return res.status(404).send("User not found");

    if (!user.followedFlights.includes(flightId)) {
      user.followedFlights.push(flightId);
      await user.save();
    }

    await user.populate("followedFlights");
    res.status(200).send(user);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Remove a flight from the followed flights
router.post("/:userId/unfollow-flight/:flightId", async (req, res) => {
  const { userId, flightId } = req.params;
  try {
    const user = await User.findById(userId);
    if (!user) return res.status(404).send("User not found");

    user.followedFlights = user.followedFlights.filter(
      (id) => id.toString() !== flightId
    );
    await user.save();

    await user.populate("followedFlights");
    res.status(200).send(user);
  } catch (error) {
    console.log(error);
    res.status(500).send(error);
  }
});

// Get all followed flights
router.get("/:userId/followed-flights", async (req, res) => {
  const { userId } = req.params;
  try {
    const user = await User.findById(userId).populate("followedFlights");
    if (!user) return res.status(404).send("User not found");
    res.status(200).send(user.followedFlights);
  } catch (error) {
    res.status(500).send(error);
  }
});

module.exports = router;
