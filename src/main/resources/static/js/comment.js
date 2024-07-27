// function displayCommentBar(event) {

//     const commentBar = document.getElementById("commentBar");

//     // Check if the div already exists
//     const existingCommentDiv = document.getElementById('commentDiv');
//     if (existingCommentDiv) {
//         // Remove the existing div from the commentBar
//         // commentBar.removeChild(existingCommentDiv);
//         console.log('Comment div already exists');
//         return;
//     }

//     const commentDiv = document.createElement('div');
//     commentDiv.id = 'commentDiv'; 
//     commentDiv.style.display = 'flex';
//     commentDiv.style.justifyContent = 'center';
//     commentDiv.style.alignItems = 'center';
//     commentDiv.style.flexDirection = 'column';

//     // Create a text area for the comment bar
//     const comment = document.createElement('textarea');
//     comment.id = 'commentInput';
//     comment.placeholder = 'Enter your comment here...';
//     comment.style.width = '100%'; 

//     // Create a button to confirm the comment
//     const confirmButton = document.createElement('button');
//     confirmButton.innerHTML = 'Enter';
//     confirmButton.id = 'commentButton';
//     confirmButton.onclick = function(event) {
//         // Prevent the default form submission which causes a page reload
//         event.preventDefault();
    
//         // You can add functionality here to handle the comment confirmation
//         console.log('Comment confirmed: ', comment.value);
//         addComment();
//     };

//     commentDiv.appendChild(comment);
//     commentDiv.appendChild(confirmButton);

//     // Get the element where you want to add the comment bar and button
//     //const commentBar = document.getElementById("commentBar");

//     // Append the comment bar and confirm button to the element
//     commentBar.appendChild(comment);
//     commentBar.appendChild(confirmButton);
//     commentBar.appendChild(commentDiv);
// }

// Website object in JSON format
var website = {};

//Comment object in JSON format
var comment = {};

// Create website object and display comments of website being scanned
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

            // Create a table and populate it with comments
            let table = '<table id="commentTable"><tr><th>User</th><th>Comment</th><th>Date</th></tr>';
            table.id = 'commentTable';
            for (let comment of result) {
                table += `<tr><td>${comment.user}</td><td>${comment.content}</td><td>${comment.date}</td></tr>`;
            }
            table += '</table>';

            document.getElementById('comments').innerHTML = table;
    }
    catch (error) {
        console.log("Error:", error);
    }
}



async function addComment() {
    
    // Get the value from the textarea and add it to the comment object
    comment.commentInput = document.getElementById('commentInput').value;
    comment.link = document.getElementById('scannedUrl').value;

    console.log("Sending comment:", JSON.stringify(comment));

    const url = "/addComment";
    // Request options for fetch
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(comment)
    }

    try {
        const response = await fetch(url, options);
        console.log("Received response:", response);

        const result = await response.json();
        console.log("Parsed JSON result:", result);

        document.getElementById('newcomment').innerText = 'TESTING:   ' + JSON.stringify(result);
        
        var table = document.getElementById('commentTable');
        var row = table.insertRow(1);

        // Insert cells into the row
        var cell1 = row.insertCell(0);
        var cell2 = row.insertCell(1);
        var cell3 = row.insertCell(2);
        

        // Add the data to the cells
        cell1.innerHTML = result.user || 'N/A';  // If user is null, display 'N/A'
        cell2.innerHTML = result.content.replace('\\n', '<br>');  // Replace newline characters with HTML line breaks
        cell3.innerHTML = result.date;
        
    }
    catch (error) {
        console.log("Error:", error);
    }
}