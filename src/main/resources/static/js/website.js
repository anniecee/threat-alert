function deleteFromHistory(event, wid) {
    event.preventDefault();

    const row = event.target.closest('tr');
    const websiteUrl = row.cells[0].innerHTML.trim();

    for (const cell of row.cells) {
        cell.innerHTML = "";
    }

    row.cells[0].innerHTML = "Removed (" + websiteUrl + ")";

    wid = wid.toString();
    console.log("wid: " + wid);

    const url = "/user/deletefromhistory";
    const options = {
        method: "DELETE",
        headers: {
            "Content-Type": "text/plain"
        },
        body: wid
    }

    fetch(url, options)
        .then(response => response.text())
        .then(response => console.log(response))
        .catch(err => console.error(err));
}

function addBookmark(event, wid) {
    event.preventDefault();

    wid = wid.toString();
    const self = event.target.closest('.form-bookmark');
    self.innerHTML = "<span style='cursor: default;'>BOOKMARKED</span>"

    const url = "/user/addbookmark";
    const options = {
        method: "PUT",
        headers: {
            "Content-Type": "text/plain"
        },
        body: wid
    }

    fetch(url, options)
        .then(response => response.text())
        .then(response => console.log(response))
        .catch(err => console.log(err))
}

function removeBookmark(event, wid) {
    event.preventDefault();

    const row = event.target.closest('tr');
    const websiteUrl = row.cells[0].innerHTML.trim();

    for (const cell of row.cells) {
        cell.innerHTML = "";
    }

    row.cells[0].innerHTML = "Removed (" + websiteUrl + ")";

    wid = wid.toString();
    console.log("wid: " + wid);

    const url = "/user/removebookmark";
    const options = {
        method: "DELETE",
        headers: {
            "Content-Type": "text/plain"
        },
        body: wid
    }

    fetch(url, options)
        .then(response => response.text())
        .then(response => console.log(response))
        .catch(err => console.log(err))
}