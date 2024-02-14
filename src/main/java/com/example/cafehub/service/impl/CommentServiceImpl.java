package com.example.cafehub.service.impl;

import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.dto.comment.CreateCommentRequestDto;
import com.example.cafehub.exception.EntityNotFoundException;
import com.example.cafehub.mapper.CommentMapper;
import com.example.cafehub.model.Cafe;
import com.example.cafehub.model.Comment;
import com.example.cafehub.model.User;
import com.example.cafehub.repository.CafeRepository;
import com.example.cafehub.repository.CommentRepository;
import com.example.cafehub.repository.UserRepository;
import com.example.cafehub.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentResponseDto> getAllCommentByUserId(Long userId) {
        return commentRepository.findAllByUserId(userId).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    @Override
    public CommentResponseDto getCommentById(Long userId, Long commentId) {
        return commentMapper.toDto(getCommentByIdAndUserId(commentId, userId));
    }

    @Override
    public CommentResponseDto addComment(Long userId,
                                         Long cafeId,
                                         CreateCommentRequestDto commentDto) {
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no cafe with id " + cafeId));
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no user with id " + userId));
        Comment commentEntity = new Comment();
        commentEntity.setCafe(cafe);
        commentEntity.setUser(user);
        commentEntity.setComment(commentDto.getComment());
        return commentMapper.toDto(commentRepository.save(commentEntity));
    }

    @Override
    public CommentResponseDto update(Long userId,
                                     Long commentId,
                                     CreateCommentRequestDto commentDto) {
        Comment comment = getCommentByIdAndUserId(commentId, userId);
        comment.setComment(commentDto.getComment());
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("There is no comment with id " + commentId);
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        Comment comment = getCommentByIdAndUserId(commentId, userId);
        commentRepository.delete(comment);
    }

    private Comment getCommentByIdAndUserId(Long commentId, Long userId) {
        return commentRepository.findByIdAndUserId(commentId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format(
                                "User with id %s doesn't have a comment with id %s",
                                userId,
                                commentId
                        )));
    }
}
