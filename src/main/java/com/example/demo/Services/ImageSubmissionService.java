package com.example.demo.Services;

import static java.nio.file.Files.createTempFile;

import com.example.demo.Entity.ImageSubmission;
import com.example.demo.ImageSubmissionRepository;
import com.example.demo.endpoint.event.EventProducer;
import com.example.demo.endpoint.event.model.GrayscaleImageRequested;
import com.example.demo.file.bucket.BucketComponent;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class ImageSubmissionService {

  private final ImageSubmissionRepository repository;
  private final BucketComponent bucketComponent;
  private final EventProducer<GrayscaleImageRequested> eventProducer;

  @SneakyThrows
  public ImageSubmission submit(MultipartFile file, String email) {
    var originalFilename = file.getOriginalFilename();
    var suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));

    // 1. Enregistrement SYNCHRONE en base (id, nom du fichier, email)
    var submission =
        repository.save(ImageSubmission.builder().fileName(originalFilename).email(email).build());

    var tempFile = createTempFile("image-" + submission.getId(), suffix);
    file.transferTo(tempFile);
    var bucketKey = "original/" + submission.getId() + suffix;
    bucketComponent.upload(tempFile.toFile(), bucketKey);
    var event =
        GrayscaleImageRequested.builder()
            .id(submission.getId())
            .bucketKey(bucketKey)
            .fileName(originalFilename)
            .email(email)
            .build();

    eventProducer.accept(List.of(event));

    return submission;
  }

  public List<ImageSubmission> findAll() {
    return repository.findAll();
  }
}
