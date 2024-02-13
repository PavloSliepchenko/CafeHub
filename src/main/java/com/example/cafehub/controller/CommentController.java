package com.example.cafehub.controller;

import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.dto.comment.CreateCommentRequestDto;
import com.example.cafehub.model.User;
import com.example.cafehub.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(value = "/{cafeId}")
    public CommentResponseDto addComment(Authentication authentication,
                                         @PathVariable Long cafeId,
                                         @RequestBody CreateCommentRequestDto commentDto) {
        User user = (User) authentication.getPrincipal();
        return commentService.addComment(user.getId(), cafeId, commentDto);
    }

    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CommentResponseDto> getAllCommentsByUserId(@PathVariable Long userId) {
        return commentService.getAllCommentByUserId(userId);
    }

    @GetMapping(value = "{commentId}")
    @PreAuthorize("hasAuthority('USER')")
    public CommentResponseDto getCommentById(Authentication authentication,
                                             @PathVariable Long commentId) {
        User user = (User) authentication.getPrincipal();
        return commentService.getCommentById(user.getId(), commentId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping(value = "/{commentId}")
    public CommentResponseDto updateComment(Authentication authentication,
                                            @PathVariable Long commentId,
                                            @RequestBody CreateCommentRequestDto commentDto) {
        User user = (User) authentication.getPrincipal();
        return commentService.update(user.getId(), commentId, commentDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(value = "/user/{commentId}")
    public void deleteCommentByUser(Authentication authentication, @PathVariable Long commentId) {
        User user = (User) authentication.getPrincipal();
        commentService.deleteCommentByUser(user.getId(), commentId);
    }
}
