package com.example.cafehub.repository;

import com.example.cafehub.model.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserId(Long userId);

    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);
}
