const express = require('express');
const app = express();
app.post('/webhook', express.json({type: 'application/json'}), (request, response) => {
response.status(202).send('Accepted');

const githubEvent = request.headers['x-github-event'];

if (githubEvent == 'push') {
    console.log(`works`);
	const { exec } = require("child_process");

	exec("sudo docker stop serverContainerWompWomp && sudo docker pull orangetheowl/sistemske:latest && sudo docker rm serverContainerWompWomp && sudo docker run --name serverContainerWompWomp -dp 8080:3000 orangetheowl/sistemske:latest", (error, stdout, stderr) => {
    		if (error) {
        		console.log(`error: ${error.message}`);
        		return;
    		}
    		if (stderr) {
        		console.log(`stderr: ${stderr}`);
        		return;
    		}
    		console.log(`stdout: ${stdout}`);
	});	
  }else {
    console.log(`Unhandled event: ${githubEvent}`);
  }
});

const port = 3000;

app.listen(port, () => {
  console.log('Server is running on port ${port}');
});
