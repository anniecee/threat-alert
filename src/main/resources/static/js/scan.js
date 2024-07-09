const apiKey = 'APY0a4HjcIefePwJW2xbNj33ZBsFbMDRsaizaOmO6vs4Qo3K0rpO7AXsiHDsA2XFk4f2huVGe9';

// This function is called when user submits form
function scanLink(event) {
    event.preventDefault(); // prevent page to reload on submit by default

    const scannedUrl = document.getElementById("scannedUrl").value;
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