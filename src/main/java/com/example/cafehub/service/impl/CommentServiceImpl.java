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
import com.example.cafehub.service.CommentService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CafeRepository cafeRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentResponseDto> getAllComments(Pageable pageable) {
        return commentRepository.findAll(pageable).stream()
                .map(commentMapper::toDto)
                .toList();
    }

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
        User user = new User();
        user.setId(userId);
        Comment commentEntity = new Comment();
        commentEntity.setCafe(cafe);
        commentEntity.setUser(user);
        commentEntity.setTime(LocalDateTime.now());
        commentEntity.setComment(commentDto.getComment());
        commentEntity.setScore(commentDto.getScore());
        commentEntity = commentRepository.save(commentEntity);
        cafe.setNumberOfUsersVoted(cafe.getNumberOfUsersVoted().add(BigDecimal.ONE));
        cafe.setTotalScore(cafe.getTotalScore().add(commentDto.getScore()));
        cafe.setScore(cafe.getTotalScore().divide(cafe.getNumberOfUsersVoted()));
        cafeRepository.save(cafe);
        return commentMapper.toDto(commentEntity);
    }

    @Override
    public CommentResponseDto update(Long userId,
                                     Long commentId,
                                     CreateCommentRequestDto commentDto) {
        Comment comment = getCommentByIdAndUserId(commentId, userId);
        if (commentDto.getComment() != null) {
            comment.setComment(commentDto.getComment());
        }
        if (commentDto.getScore() != null) {
            Cafe cafe = comment.getCafe();
            cafe.setTotalScore(cafe.getTotalScore()
                    .subtract(comment.getScore())
                    .add(commentDto.getScore()));
            cafe.setScore(cafe.getTotalScore().divide(cafe.getNumberOfUsersVoted()));
            cafeRepository.save(cafe);
            comment.setScore(commentDto.getScore());
        }
        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException("There is no comment with id " + commentId);
        }
        deleteScoreByCommentId(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        Comment comment = getCommentByIdAndUserId(commentId, userId);
        deleteScoreByCommentId(commentId);
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

    private void deleteScoreByCommentId(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            Cafe cafe = comment.getCafe();
            cafe.setNumberOfUsersVoted(cafe.getNumberOfUsersVoted().subtract(BigDecimal.ONE));
            cafe.setTotalScore(cafe.getTotalScore().subtract(comment.getScore()));
            cafe.setScore(cafe.getTotalScore().divide(cafe.getNumberOfUsersVoted()));
            cafeRepository.save(cafe);
        }
    }
}
