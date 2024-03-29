package com.example.cafehub.service;

import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.dto.comment.CreateCommentRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    List<CommentResponseDto> getAllComments(Pageable pageable);

    CommentResponseDto addComment(Long userId, Long cafeId, CreateCommentRequestDto commentDto);

    List<CommentResponseDto> getAllCommentByUserId(Long userId);

    CommentResponseDto getCommentById(Long userId, Long commentId);

    CommentResponseDto update(Long userId, Long commentId, CreateCommentRequestDto commentDto);

    void deleteComment(Long commentId);

    void deleteCommentByUser(Long userId, Long commentId);
}
