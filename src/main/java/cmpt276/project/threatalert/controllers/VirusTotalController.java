// package cmpt276.project.threatalert.controllers;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;
// import cmpt276.project.threatalert.models.VirusTotal;

// import java.io.IOException;

// @RestController
// @RequestMapping("/api/v1")
// public class VirusTotalController {

//     @Autowired
//     private VirusTotal virusTotalService;

//     @PostMapping("/scan")
//     public String scanUrl(@RequestParam String url) {
//         try {
//             return virusTotalService.scanUrl(url);
//         } catch (IOException e) {
//             return "Error: " + e.getMessage();
//         }
//     }
// }