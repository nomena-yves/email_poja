package com.example.demo;

import com.example.demo.Entity.ImageSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageSubmissionRepository extends JpaRepository<ImageSubmission, Long> {
}
