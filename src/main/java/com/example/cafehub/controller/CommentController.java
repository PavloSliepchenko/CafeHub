package com.example.cafehub.controller;

import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.dto.comment.CreateCommentRequestDto;
import com.example.cafehub.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping(value = "/{cafeId}/{userId}")
    public CommentResponseDto addComment(@PathVariable Long userId,
                                         @PathVariable Long cafeId,
                                         @RequestBody CreateCommentRequestDto commentDto) {
        return commentService.addComment(userId, cafeId, commentDto);
    }

    @GetMapping(value = "/{userId}")
    public List<CommentResponseDto> getAllCommentsByUserId(@PathVariable Long userId) {
        return commentService.getAllCommentByUserId(userId);
    }

    @PutMapping(value = "/{userId}/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long userId,
                                            @PathVariable Long commentId,
                                            @RequestBody CreateCommentRequestDto commentDto) {
        return commentService.update(userId, commentId, commentDto);
    }

    @DeleteMapping(value = "/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
