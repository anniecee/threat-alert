const apiKey = 'APY0a4HjcIefePwJW2xbNj33ZBsFbMDRsaizaOmO6vs4Qo3K0rpO7AXsiHDsA2XFk4f2huVGe9';

// This function is called when user submits form
function scanLink(event) {
    event.preventDefault(); // prevent page to reload on submit by default

    const scannedUrl = document.getElementById("scannedUrl").value;
    website["link"] = scannedUrl; //add link to website json (see below)
    sendUrl(scannedUrl);
    
} 

// POST request to send scanned URL
async function sendUrl(scannedUrl) {
    const request = new Request('https://api.apyhub.com/extract/url/preview', {
        method: 'POST',
        headers: {
            'apy-token': apiKey,
            'Content-Type': 'application/json'
        },
        body: `{"url": "${scannedUrl}"}`
    })
    const result = await fetch(request)
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            displayThreatLevel(data);
            displayScanReport(data);
            displayScreenshot(data);
            //displayCommentButton();

            // Sending Post request
            console.log("saving to user history")
            addHistory();

            console.log("saving website object and display comments")
            createWebsite();
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function displayThreatLevel(data) {
    const malicious = data.data.reported_malicious;
    const element = document.getElementById("threatLevel");

    if (malicious === true) {
        const content = "<h3> Threat Level: Warning! </h3>";
        element.innerHTML = content;
        element.style.color = 'orange';
    } else {
        const content = "<h3> Threat Level: Clean! </h3>";
        element.innerHTML = content;
        element.style.color = 'green';
    } 

    website["threatlevel"] = malicious.toString();
}

function displayScanReport(data) {
    const reportElement = document.getElementById('scanReport');
    const dataElement = document.getElementById('dataDisplay');
    
    const title = data.data.title;
    const siteName = data.data.siteName;
    const description = data.data.description;

    const heading = "<h4>Scan Report:</h4>"
    const content = '<b>Title:</b> ' + title + '<br> <b>Site Name:</b> ' + siteName + '<br><b>Description:</b> ' + description + '<br>';  

    reportElement.innerHTML = heading;
    dataElement.innerHTML = content;
}

function displayScreenshot(data) {
    const screenshotTitle = document.getElementById('screenshotTitle');
    const screenshot = document.getElementById('screenshot');

    const images = data.data.images;

    const heading = "<h4>Page Screenshot</h4>"
    let content = "" ;
    for (const img of images) {
        content += "<img src='" + img + "' alt='screenshot' width='600'> " + '<br>'; 
    }

    screenshotTitle.innerHTML = heading;
    screenshot.innerHTML = content;
}

// function displayCommentButton() {
//     const commentButton = document.getElementById('commentButton');
//     commentButton.innerHTML = "<a href='/viewcomment' class='btn btn-info' role='button'>Click here to view comments</a>";
//     // "<button class='btn btn-primary ml-2' href='/comment' type='submit' style='border: 2px solid;'>Comments</button> ";
// }

// Below code to be executed when a url has been scanned
// Add a Website object to User history

// Website object in JSON format
var website = {};

async function addHistory() {
    console.log(JSON.stringify(website));

    const url = "/user/addhistory";
    // Request options for fetch
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(website)
    }

    try {
        const response = await fetch(url, options);
        const result = await response.json();
        console.log("Success:", result);
    }
    catch (error) {
        console.log("Error:", error);
    }
}

async function createWebsite() {
    console.log(JSON.stringify(website));

    const url = "/scanning";
    // Request options for fetch
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(website)
    }

    try {
        const response = await fetch(url, options);
        const result = await response.json();
        console.log("Success:", result);

        // Update the DOM here with the result
        // For example, if you have an element with id 'website-info'
        document.getElementById('comments').innerText = 'TESTING:' + JSON.stringify(result);
    }
    catch (error) {
        console.log("Error:", error);
    }
}


