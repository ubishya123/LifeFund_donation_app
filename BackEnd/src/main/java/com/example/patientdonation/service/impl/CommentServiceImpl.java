package com.example.patientdonation.service.impl;

import com.example.patientdonation.entity.Comment;
import com.example.patientdonation.entity.Patient;
import com.example.patientdonation.repository.CommentRepository;
import com.example.patientdonation.repository.PatientRepository;
import com.example.patientdonation.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public List<Comment> getCommentsForPatient(Long patientId) {
        return commentRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
    }

    @Override
    public Comment postComment(Long patientId, String commenterName, String content, boolean isAnonymous) {
        // Find the patient to associate the comment with.
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        Comment newComment = new Comment();
        newComment.setPatient(patient);
        newComment.setContent(content);
        newComment.setAnonymous(isAnonymous);

        // If the comment is anonymous, save the name as "Anonymous", otherwise use the provided name.
        if (isAnonymous) {
            newComment.setCommenterName("Anonymous");
        } else {
            newComment.setCommenterName(commenterName);
        }

        return commentRepository.save(newComment);
    }
}