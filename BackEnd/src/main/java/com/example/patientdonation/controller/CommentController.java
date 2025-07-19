package com.example.patientdonation.controller;

import com.example.patientdonation.entity.Comment;
import com.example.patientdonation.entity.User;
import com.example.patientdonation.service.CommentService; // Import the service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService; // Use the service

    /**
     * Retrieves all comments for a specific patient.
     */
    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long patientId) {
        List<Comment> comments = commentService.getCommentsForPatient(patientId);
        return ResponseEntity.ok(comments);
    }

    /**
     * Allows a logged-in user to post a new comment.
     */
    @PostMapping
    public ResponseEntity<Comment> postComment(@PathVariable Long patientId, @RequestBody Comment commentRequest) {
        // Get the currently authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        // Call the service to handle the business logic
        Comment savedComment = commentService.postComment(
                patientId,
                currentUser.getName(),
                commentRequest.getContent(),
                commentRequest.isAnonymous()
        );

        return ResponseEntity.ok(savedComment);
    }
}