const jwt = require('jsonwebtoken');

const generateJWT = id => {
    return jwt.sign({ id }, process.env.JWT_SECRET, {
        expiresIn: 259200
    });
}

const checkJWT = req => {
    const token = req.cookies.jwt;
    return new Promise((resolve, reject) => {
        if (token) {
            jwt.verify(token, process.env.JWT_SECRET, (err, decodedToken) => {
                if (err) 
                    resolve(false);
                else
                    resolve(true);
            });
        } else {
            resolve(false);
        }
    });
}

module.exports = { generateJWT, checkJWT };