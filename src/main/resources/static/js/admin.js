function deleteUser(event, uid) {
    event.preventDefault();

    const row = event.target.closest('tr');
    // const websiteUrl = row.cells[0].innerHTML.trim();

    for (const cell of row.cells) {
        cell.innerHTML = "";
    }

    row.cells[1].innerHTML = "Removed";

    uid = uid.toString();
    console.log("uid: " + uid);

    const url = "/user/delete";
    const options = {
        method: "DELETE",
        headers: {
            "Content-Type": "text/plain"
        },
        body: uid
    }

    fetch(url, options)
        .then(response => response.text())
        .then(response => console.log(response))
        .catch(err => console.log(err))
}
