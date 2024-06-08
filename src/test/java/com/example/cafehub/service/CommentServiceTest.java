package com.example.cafehub.service;

import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.dto.comment.CreateCommentRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.exception.EntityNotFoundException;
import com.example.cafehub.mapper.CommentMapper;
import com.example.cafehub.model.Cafe;
import com.example.cafehub.model.Comment;
import com.example.cafehub.model.User;
import com.example.cafehub.repository.CafeRepository;
import com.example.cafehub.repository.CommentRepository;
import com.example.cafehub.service.impl.CommentServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    private Cafe cafe1;
    private Cafe cafe2;
    private Cafe cafe3;
    private User user1;
    private User user2;
    private User user3;
    private UserResponseDto userResponseDto1;
    private UserResponseDto userResponseDto2;
    private UserResponseDto userResponseDto3;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    private Comment comment4;
    private Comment comment5;
    private Comment comment6;
    private CommentResponseDto commentResponseDto1;
    private CommentResponseDto commentResponseDto2;
    private CommentResponseDto commentResponseDto3;
    private CommentResponseDto commentResponseDto4;
    private CommentResponseDto commentResponseDto5;
    private CommentResponseDto commentResponseDto6;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private CommentMapper commentMapper;
    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void init() {
        cafe1 = new Cafe();
        cafe1.setId(1L);
        cafe1.setName("First Point");
        cafe1.setCity("Kyiv");
        cafe1.setAddress("вул. Ярославська 14/20");
        cafe1.setNumberOfUsersVoted(BigDecimal.ZERO);
        cafe1.setTotalScore(BigDecimal.ZERO);

        cafe2 = new Cafe();
        cafe2.setId(2L);
        cafe2.setName("Blur");
        cafe2.setCity("Kyiv");
        cafe2.setAddress("вул. Мечникова 5");

        cafe3 = new Cafe();
        cafe3.setId(3L);
        cafe3.setName("Octo");
        cafe3.setCity("Lviv");
        cafe3.setAddress("вул. Євгена Чикаленка, 5");

        user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Marie");
        user1.setLastName("Curie");
        user1.setEmail("MarieCurie@gmail.com");

        user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Thomas");
        user2.setLastName("Edison");
        user2.setEmail("ThomasEdison@gmail.com");

        user3 = new User();
        user3.setId(3L);
        user3.setFirstName("Nikola");
        user3.setLastName("Tesla");
        user3.setEmail("NikolaTesla@gmail.com");

        userResponseDto1 = new UserResponseDto();
        userResponseDto1.setId(user1.getId());
        userResponseDto1.setEmail(user1.getEmail());
        userResponseDto1.setFirstName(user1.getFirstName());
        userResponseDto1.setLastName(user1.getLastName());

        userResponseDto2 = new UserResponseDto();
        userResponseDto2.setId(user2.getId());
        userResponseDto2.setEmail(user2.getEmail());
        userResponseDto2.setFirstName(user2.getFirstName());
        userResponseDto2.setLastName(user2.getLastName());

        userResponseDto3 = new UserResponseDto();
        userResponseDto3.setId(user3.getId());
        userResponseDto3.setEmail(user3.getEmail());
        userResponseDto3.setFirstName(user3.getFirstName());
        userResponseDto3.setLastName(user3.getLastName());

        comment1 = new Comment();
        comment1.setId(1L);
        comment1.setUser(user3);
        comment1.setCafe(cafe3);
        comment1.setComment("Super cafe. I love it!");

        comment2 = new Comment();
        comment2.setId(2L);
        comment2.setUser(user1);
        comment2.setCafe(cafe2);
        comment2.setComment("Really delicious coffee and bakery");

        comment3 = new Comment();
        comment3.setId(3L);
        comment3.setUser(user2);
        comment3.setCafe(cafe1);
        comment3.setComment("The best service ever!");

        comment4 = new Comment();
        comment4.setId(4L);
        comment4.setUser(user3);
        comment4.setCafe(cafe1);
        comment4.setComment("Responsible stuff. Tasty treats");

        comment5 = new Comment();
        comment5.setId(5L);
        comment5.setUser(user2);
        comment5.setCafe(cafe2);
        comment5.setComment("Great atmosphere");

        comment6 = new Comment();
        comment6.setId(6L);
        comment6.setUser(user1);
        comment6.setCafe(cafe3);
        comment6.setComment("The best cafe I've ever been to!");

        commentResponseDto1 = new CommentResponseDto();
        commentResponseDto1.setId(comment1.getId());
        commentResponseDto1.setUser(userResponseDto3);
        commentResponseDto1.setCafeId(cafe3.getId());
        commentResponseDto1.setCafeName(cafe3.getName());
        commentResponseDto1.setComment(comment1.getComment());

        commentResponseDto2 = new CommentResponseDto();
        commentResponseDto2.setId(comment2.getId());
        commentResponseDto2.setUser(userResponseDto1);
        commentResponseDto2.setCafeId(cafe2.getId());
        commentResponseDto2.setCafeName(cafe2.getName());
        commentResponseDto2.setComment(comment2.getComment());

        commentResponseDto3 = new CommentResponseDto();
        commentResponseDto3.setId(comment3.getId());
        commentResponseDto3.setUser(userResponseDto2);
        commentResponseDto3.setCafeId(cafe1.getId());
        commentResponseDto3.setCafeName(cafe1.getName());
        commentResponseDto3.setComment(comment3.getComment());

        commentResponseDto4 = new CommentResponseDto();
        commentResponseDto4.setId(comment4.getId());
        commentResponseDto4.setUser(userResponseDto3);
        commentResponseDto4.setCafeId(cafe1.getId());
        commentResponseDto4.setCafeName(cafe1.getName());
        commentResponseDto4.setComment(comment4.getComment());

        commentResponseDto5 = new CommentResponseDto();
        commentResponseDto5.setId(comment5.getId());
        commentResponseDto5.setUser(userResponseDto2);
        commentResponseDto5.setCafeId(cafe2.getId());
        commentResponseDto5.setCafeName(cafe2.getName());
        commentResponseDto5.setComment(comment5.getComment());

        commentResponseDto6 = new CommentResponseDto();
        commentResponseDto6.setId(comment6.getId());
        commentResponseDto6.setUser(userResponseDto1);
        commentResponseDto6.setCafeId(cafe3.getId());
        commentResponseDto6.setCafeName(cafe3.getName());
        commentResponseDto6.setComment(comment6.getComment());
    }

    @Test
    @DisplayName("Get all comments")
    void getAllComments_ValidRequest_ShouldReturnListOfDtos() {
        List<Comment> commentList =
                List.of(comment1, comment2, comment3, comment4, comment5, comment6);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> commentPage = new PageImpl<>(commentList, pageable, commentList.size());
        Mockito.when(commentRepository.findAll(pageable)).thenReturn(commentPage);
        Mockito.when(commentMapper.toDto(comment1)).thenReturn(commentResponseDto1);
        Mockito.when(commentMapper.toDto(comment2)).thenReturn(commentResponseDto2);
        Mockito.when(commentMapper.toDto(comment3)).thenReturn(commentResponseDto3);
        Mockito.when(commentMapper.toDto(comment4)).thenReturn(commentResponseDto4);
        Mockito.when(commentMapper.toDto(comment5)).thenReturn(commentResponseDto5);
        Mockito.when(commentMapper.toDto(comment6)).thenReturn(commentResponseDto6);

        List<CommentResponseDto> expected = List.of(commentResponseDto1, commentResponseDto2,
                commentResponseDto3, commentResponseDto4, commentResponseDto5, commentResponseDto6);
        List<CommentResponseDto> actual = commentService.getAllComments(pageable);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    @DisplayName("Add comment")
    void addComment_ValidRequest_ShouldAddComment() {
        CreateCommentRequestDto commentRequestDto = new CreateCommentRequestDto();
        commentRequestDto.setComment("I love the interior of this cafe!");
        commentRequestDto.setScore(BigDecimal.valueOf(5));

        Comment comment = new Comment();
        comment.setComment(commentRequestDto.getComment());
        comment.setScore(commentRequestDto.getScore());
        comment.setCafe(cafe1);
        comment.setUser(user1);
        comment.setId(7L);

        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setUser(userResponseDto1);
        commentResponseDto.setComment(comment.getComment());
        commentResponseDto.setCafeId(comment.getCafe().getId());

        Mockito.when(cafeRepository.findById(cafe1.getId())).thenReturn(Optional.of(cafe1));
        Mockito.when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        Mockito.when(commentMapper.toDto(comment)).thenReturn(commentResponseDto);

        CommentResponseDto actual =
                commentService.addComment(user1.getId(), cafe1.getId(), commentRequestDto);

        Assertions.assertEquals(commentRequestDto.getComment(), actual.getComment());
        Assertions.assertEquals(comment.getId(), actual.getId());
    }

    @Test
    @DisplayName("Add comment. Wrong cafe id. Throws exception")
    void addComment_WrongCafeId_ShouldThrowException() {
        Mockito.when(cafeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.addComment(
                        1L, 1L, new CreateCommentRequestDto()));
    }

    @Test
    @DisplayName("Get all comments by user id")
    void getAllCommentByUserId_ValidRequest_ShouldReturnListOfDtos() {
        Long userId = 1L;
        List<Comment> usersComments = List.of(comment2, comment6);
        Mockito.when(commentRepository.findAllByUserId(userId)).thenReturn(usersComments);
        Mockito.when(commentMapper.toDto(comment2)).thenReturn(commentResponseDto2);
        Mockito.when(commentMapper.toDto(comment6)).thenReturn(commentResponseDto6);

        List<CommentResponseDto> expected = List.of(commentResponseDto2, commentResponseDto6);
        List<CommentResponseDto> actual = commentService.getAllCommentByUserId(userId);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));

        userId = 2L;
        usersComments = List.of(comment3, comment5);
        Mockito.when(commentRepository.findAllByUserId(userId)).thenReturn(usersComments);
        Mockito.when(commentMapper.toDto(comment3)).thenReturn(commentResponseDto3);
        Mockito.when(commentMapper.toDto(comment5)).thenReturn(commentResponseDto5);

        expected = List.of(commentResponseDto3, commentResponseDto5);
        actual = commentService.getAllCommentByUserId(userId);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));

        userId = 3L;
        usersComments = List.of(comment1, comment4);
        Mockito.when(commentRepository.findAllByUserId(userId)).thenReturn(usersComments);
        Mockito.when(commentMapper.toDto(comment1)).thenReturn(commentResponseDto1);
        Mockito.when(commentMapper.toDto(comment4)).thenReturn(commentResponseDto4);

        expected = List.of(commentResponseDto1, commentResponseDto4);
        actual = commentService.getAllCommentByUserId(userId);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    @DisplayName("Get comment by id")
    void getCommentById_ValidRequest_ShouldReturnDto() {
        Mockito.when(commentRepository.findByIdAndUserId(
                comment3.getId(), comment3.getUser().getId())).thenReturn(Optional.of(comment3));
        Mockito.when(commentMapper.toDto(comment3)).thenReturn(commentResponseDto3);

        CommentResponseDto actual =
                commentService.getCommentById(comment3.getUser().getId(), comment3.getId());

        Assertions.assertEquals(commentResponseDto3.getId(), actual.getId());
        Assertions.assertEquals(commentResponseDto3, actual);
    }

    @Test
    @DisplayName("Get comment by id. Wrong id. Throws exception")
    void getCommentById_WrongId_ShouldThrowException() {
        Mockito.when(commentRepository.findByIdAndUserId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.getCommentById(1L, 1L));
    }

    @Test
    @DisplayName("Update comment")
    void update_ValidRequest_ShouldReturnDto() {
        CreateCommentRequestDto commentRequestDto = new CreateCommentRequestDto();
        commentRequestDto.setComment("New comment");
        commentResponseDto1.setComment(commentRequestDto.getComment());

        Comment testComment = new Comment();
        testComment.setId(comment1.getId());
        testComment.setUser(comment1.getUser());
        testComment.setCafe(comment1.getCafe());
        testComment.setComment(commentRequestDto.getComment());

        Mockito.when(commentRepository.findByIdAndUserId(
                comment1.getId(), comment1.getUser().getId())).thenReturn(Optional.of(comment1));
        Mockito.when(commentRepository.save(testComment)).thenReturn(testComment);
        Mockito.when(commentMapper.toDto(testComment)).thenReturn(commentResponseDto1);

        CommentResponseDto actual = commentService.update(
                comment1.getUser().getId(), comment1.getId(), commentRequestDto);

        Assertions.assertEquals(commentResponseDto1.getId(), actual.getId());
        Assertions.assertEquals(commentResponseDto1.getComment(), actual.getComment());
    }

    @Test
    @DisplayName("Delete comment by id. Wrong id. Throws exception")
    void deleteComment_WrongId_ShouldThrowException() {
        Mockito.when(commentRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.deleteComment(1L));
    }
}
