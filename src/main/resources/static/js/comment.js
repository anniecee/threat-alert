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
            var tbody = table.getElementsByTagName('tbody')[0];

            // Check if the table only has 1 row and 0 comment
            if (tbody.rows.length === 1 && tbody.rows[0].cells[0].colSpan === 4) {
                tbody.deleteRow(0);
            }

            var row = tbody.insertRow(0); 
            row.id = "comment-" + commentId;

            // Insert new cells
            var cell1 = row.insertCell(0);
            var cell2 = row.insertCell(1);
            var cell3 = row.insertCell(2);
            var cell4 = row.insertCell(3);

            // Add the data to the new cells
            cell1.innerHTML = '<h6 style="color: blue;">' + user + '</h6>';
            cell2.innerHTML = comment;
            cell3.innerHTML = formattedDate;
            
            // Create delete button
            var deleteButton = document.createElement('button');
            deleteButton.textContent = 'Delete';
            deleteButton.type = 'button';
            deleteButton.className = 'btn btn-danger';
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
            var table = document.getElementById('CommentTable');
            var tbody = table.getElementsByTagName('tbody')[0];
            if (tbody.rows.length === 0) {
                var newRow = tbody.insertRow(0);
                var newCell = newRow.insertCell(0);
                newCell.colSpan = 4;
                newCell.className = 'text-center';
                newCell.textContent = 'Be the first to leave a comment';
            }
        } else {
            console.error('Failed to delete comment:', response.statusText);
        }
    } catch (error) {
        console.error('Error deleting comment:', error);
    }
}
