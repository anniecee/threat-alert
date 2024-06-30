const apiKey = '5796597306b3edb03cf9aa494b58a755431d95042f093fc3cc46375e52be9687';
const scannedUrl = 'https://www.cercacor.com/';

// POST request to send scanned URL and get Analysis ID
async function sendUrl(callback) {
    const request = new Request('https://www.virustotal.com/api/v3/urls', {
        method: 'POST',
        headers: {
            accept: 'application/json',
            'x-apikey': apiKey,
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
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

sendUrl().then(analysisId => getAnalysis(analysisId));

 