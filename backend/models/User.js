const mongoose = require("mongoose");
const bcrypt = require("bcrypt");

const userSchema = new mongoose.Schema(
  {
    username: { type: String, required: true },
    email: { type: String, required: true, unique: true },
    password: { type: String, required: true },
    followedFlights: [{ type: mongoose.Schema.Types.ObjectId, ref: "flight" }],
    defaultLocation: {
      type: mongoose.Schema.Types.ObjectId,
      ref: "location",
      default: null,
    },
  },
  { strict: "throw" }
);

userSchema.pre("save", async function (next) {
  if (this.isModified("password")) {
    const salt = await bcrypt.genSalt();
    this.password = await bcrypt.hash(this.password, salt);
  }
  next();
});

const User = mongoose.model("user", userSchema);

module.exports = User;
