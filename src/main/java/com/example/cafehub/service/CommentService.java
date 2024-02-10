package com.example.cafehub.service;

import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.dto.comment.CreateCommentRequestDto;
import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(Long userId, Long cafeId, CreateCommentRequestDto commentDto);

    List<CommentResponseDto> getAllCommentByUserId(Long userId);

    CommentResponseDto update(Long userId, Long commentId, CreateCommentRequestDto commentDto);

    void deleteComment(Long commentId);
}
