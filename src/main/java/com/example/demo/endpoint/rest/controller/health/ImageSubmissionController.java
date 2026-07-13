package com.example.demo.endpoint.rest.controller.health;

import com.example.demo.Entity.ImageSubmission;
import com.example.demo.Services.ImageSubmissionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/images")
public class ImageSubmissionController {
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png");

    private final ImageSubmissionService service;
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ImageSubmission> submit(
            @RequestParam("file") MultipartFile file, @RequestParam("email") String email) {

        if (file.isEmpty() || !ALLOWED_TYPES.contains(file.getContentType())) {
            return ResponseEntity.badRequest().build();
        }

        var submission = service.submit(file, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(submission);
    }
}
