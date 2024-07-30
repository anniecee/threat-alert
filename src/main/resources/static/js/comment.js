// Calls java post mapping func and add a row containing comment to table
async function addCommentjs(event) {
    event.preventDefault();
    console.log("addCommentjs function called");

    var comment = document.getElementById("commentInput").value;

    var formData = {
        content: comment
    };

    try {
        const response = await fetch('/addComment', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            throw new Error('Bad Request');
        }

        const commentId = await response.json();
        
        if (commentId === -1) {
            console.error("Error: No content in comment");
        } else {
            console.log("Comment ID: " + commentId);

            // Get the current date and format
            var date = new Date();
            var formattedDate = date.toISOString().split('T')[0];

            // Get the current user's name
            var user = document.getElementById('userName').value;

            // Create a new row
            var table = document.getElementById('CommentTable');
            var row = table.insertRow(1); 
            row.id = "comment-" + commentId;

            // Insert new cells
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell3 = row.insertCell(2);
            var cell4 = row.insertCell(3);

            // Add the data to the new cells
            cell1.innerHTML = user;
            cell2.innerHTML = comment;
            cell3.innerHTML = formattedDate;
            
            // Create delete button
            var deleteButton = document.createElement('button');
            deleteButton.textContent = 'Delete';
            deleteButton.onclick = function(event) {
                deleteComment(event, commentId);
            };
            cell4.appendChild(deleteButton);

            // Clear the comment input field
            document.getElementById('commentInput').value = '';
        }

    } catch (error) {
        console.error('Error:', error);
    }
}

// Calls java delete mapping func and remove from comments table
async function deleteComment(event, commentId) {
    event.preventDefault();
    console.log(commentId);
    try {
        const response = await fetch('/deleteComment', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: commentId
        });
        // console.log(response.status);
        // console.log(response);
        if (response.ok) {
            document.getElementById(`comment-${commentId}`).remove();
        } else {
            console.error('Failed to delete comment:', response.statusText);
        }
    } catch (error) {
        console.error('Error deleting comment:', error);
    }
}
