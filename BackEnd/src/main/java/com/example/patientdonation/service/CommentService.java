package com.example.patientdonation.service;

import com.example.patientdonation.entity.Comment;
import java.util.List;

public interface CommentService {

    /**
     * Retrieves all comments for a specific patient.
     * @param patientId The ID of the patient.
     * @return A list of comments, ordered by most recent first.
     */
    List<Comment> getCommentsForPatient(Long patientId);

    /**
     * Creates and saves a new comment for a patient.
     * @param patientId The ID of the patient receiving the comment.
     * @param commenterName The name of the person leaving the comment.
     * @param content The actual text of the comment.
     * @param isAnonymous A boolean flag to hide the commenter's name.
     * @return The saved Comment object.
     */
    Comment postComment(Long patientId, String commenterName, String content, boolean isAnonymous);
}