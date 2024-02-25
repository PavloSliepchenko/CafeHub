package com.example.cafehub.controller;

import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.dto.comment.CreateCommentRequestDto;
import com.example.cafehub.model.User;
import com.example.cafehub.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Comments management",
        description = "Provides endpoints for CRUD operations with comments")
public class CommentController {
    private final CommentService commentService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(value = "/{cafeId}")
    @Operation(summary = "Add a new comment",
            description = "Adds a new comment to cafe. Available to all authenticated in users")
    public CommentResponseDto addComment(Authentication authentication,
                                         @PathVariable Long cafeId,
                                         @RequestBody CreateCommentRequestDto commentDto) {
        User user = (User) authentication.getPrincipal();
        return commentService.addComment(user.getId(), cafeId, commentDto);
    }

    @GetMapping(value = "/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all comments by user id",
            description = "Returns all comment left by certain user. "
                    + "Available to admin users only")
    public List<CommentResponseDto> getAllCommentsByUserId(@PathVariable Long userId) {
        return commentService.getAllCommentByUserId(userId);
    }

    @GetMapping(value = "/mine")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get all my comments",
            description = "Returns all comments of a user. "
                    + "Available to all authenticated in users")
    public List<CommentResponseDto> getAllComments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return commentService.getAllCommentByUserId(user.getId());
    }

    @GetMapping(value = "{commentId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get comment by id",
            description = "Returns comment by id if this comment belongs to the user. "
                    + "Available to all authenticated in users")
    public CommentResponseDto getCommentById(Authentication authentication,
                                             @PathVariable Long commentId) {
        User user = (User) authentication.getPrincipal();
        return commentService.getCommentById(user.getId(), commentId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping(value = "/{commentId}")
    @Operation(summary = "Update comment", description = "Updates a chosen comment if this comment "
            + "belongs to the user. Available to all authenticated in users")
    public CommentResponseDto updateComment(Authentication authentication,
                                            @PathVariable Long commentId,
                                            @RequestBody CreateCommentRequestDto commentDto) {
        User user = (User) authentication.getPrincipal();
        return commentService.update(user.getId(), commentId, commentDto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/{commentId}")
    @Operation(summary = "Delete comment by id",
            description = "Deletes comment by id. Available to admin users only")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(value = "/user/{commentId}")
    @Operation(summary = "Delete comment by id",
            description = "Deletes comment by id if this comment belongs to the user. "
                    + "Available to all authenticated in users")
    public void deleteCommentByUser(Authentication authentication, @PathVariable Long commentId) {
        User user = (User) authentication.getPrincipal();
        commentService.deleteCommentByUser(user.getId(), commentId);
    }
}
