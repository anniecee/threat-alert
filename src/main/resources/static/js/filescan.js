// function inputFileScan(event) {

//     var fileInput = document.getElementById('fileInput');
//     if (fileInput.files.length > 0) {

//         var file = fileInput.files[0];
        
//         var data = new FormData();
//         data.append("inputFile", file, file.name);

//         var xhr = new XMLHttpRequest();
//         xhr.withCredentials = true;

//         xhr.addEventListener("readystatechange", function() {
//             if (this.readyState === 4) {
//                 console.log(this.responseText);
//             }
//         });

//         xhr.open("POST", "https://api.cloudmersive.com/virus/scan/file");
//         xhr.setRequestHeader("Apikey", "827356e0-de55-4ff5-b4c2-fc77a06c3a28");
//         xhr.send(data);

//     }

// }