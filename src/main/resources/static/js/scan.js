const apiKey = '5796597306b3edb03cf9aa494b58a755431d95042f093fc3cc46375e52be9687';

// This function is called when user submits form
function scanLink(event) {
    event.preventDefault(); // prevent page to reload on submit by default

    const scannedUrl = document.getElementById("scannedUrl").value;
    sendUrl(scannedUrl).then(analysisId => getAnalysis(analysisId));
} 

// POST request to send scanned URL and get Analysis ID
async function sendUrl(scannedUrl) {
    const request = new Request('https://www.virustotal.com/api/v3/urls', {
        method: 'POST',
        headers: {
            accept: 'application/json',
            'x-apikey': apiKey,
            'content-type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({url: scannedUrl})
    })
    try {
        const response = await fetch(request);
        const data = await response.json();
        console.log(data);
        return data.data.id;
    } catch (error) {
        console.error('Error:', error);
    }
}

// GET request to get Analysis from Analysis ID
async function getAnalysis(analysisId) {
    const request = new Request(`https://www.virustotal.com/api/v3/analyses/${analysisId}`, {
        method: 'GET',
        headers: {
            accept: 'application/json',
            'x-apikey': apiKey,
        },
    })
    const result = await fetch(request)
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            displayThreatLevel(data);
            displayScanReport(data);
            displayVendors(data);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

// Display Threat Level as Warning if more than 5 vendors flag the website
function displayThreatLevel(data) {
    const malicious = data.data.attributes.stats.malicious;
    const suspicious = data.data.attributes.stats.suspicious;
    const element = document.getElementById("threatLevel");

    if (malicious + suspicious > 5) {
        const content = "<h3> Threat Level: Warning! </h3>";
        element.innerHTML = content;
        element.style.color = 'orange';
    } else {
        const content = "<h3> Threat Level: Clean! </h3>";
        element.innerHTML = content;
        element.style.color = 'green';
    } 
}

// display the 5 types of result
function displayScanReport(data) {
    const reportElement = document.getElementById('scanReport');
    const title = "<h4>Scan Report:</h4>"

    const dataElement = document.getElementById('dataDisplay');
    let content = "";

    const stats = data.data.attributes.stats;
    for (const type in stats) {
        content += type.toUpperCase() + ': ' + stats[type] + '<br>';  
    }

    reportElement.innerHTML = title;
    dataElement.innerHTML = content;
}

//display the specific result of each vendor
function displayVendors(data) {
    const vendorsTitle = document.getElementById('vendorsTitle');
    const title = "<h4>Security Vendors' Details</h4>"

    const vendorsAnalysis = document.getElementById('vendorsAnalysis');
    let content = "";

    const categories = ['malicious', 'suspicious', 'harmless', 'undetected', 'timeout'];

    for (const category of categories) {
        const results = data.data.attributes.results;
        for (const vendor in results) {
            if (results[vendor].category === category) {
                content += '<b>' + vendor + '</b> (' + category + '): ' + results[vendor].result.toUpperCase() + '<br>'; 
            }
        }
    }

    vendorsTitle.innerHTML = title;
    vendorsAnalysis.innerHTML = content;
}