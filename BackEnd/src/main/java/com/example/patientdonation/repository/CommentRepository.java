package com.example.patientdonation.repository;

import com.example.patientdonation.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}