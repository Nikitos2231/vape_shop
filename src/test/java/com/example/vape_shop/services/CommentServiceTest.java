package com.example.vape_shop.services;

import com.example.vape_shop.models.Comment;
import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.repositories.CommentRepository;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ManService manService;
    @MockBean
    private ItemService itemService;

    @Test
    void save() {
        Comment comment = new Comment();
        Man man = new Man();
        man.setUserId(1);
        Item item = new Item(1);
        Mockito.doReturn(man).when(manService).findOne(Mockito.anyInt());
        Mockito.doReturn(item).when(itemService).findById(Mockito.anyInt());

        commentService.save(comment, man, item);

        Assertions.assertEquals(1, item.getItemId());
        Assertions.assertEquals(1, man.getUserId());
        Assertions.assertNotNull(comment.getCommentDateOfCreate());

        Mockito.verify(manService).findOne(1);
        Mockito.verify(itemService).findById(1);
        Mockito.verify(commentRepository).save(comment);
        Mockito.verify(manService).saveMan(man);
        Mockito.verify(itemService).save(item);
    }

    @Test
    void removeTheSameElementsIntoCommentList() {
        List<Comment> commentList = new ArrayList<>();
        Comment comment1 = new Comment(1, new Date());
        Comment comment2 = new Comment(1, new Date());
        Comment comment3 = new Comment(2, new Date());
        Comment comment4 = new Comment(3, new Date());
        commentList.add(comment1);
        commentList.add(comment2);
        commentList.add(comment3);
        commentList.add(comment4);

        List<Comment> comments = commentService.removeTheSameElementsIntoCommentList(commentList);

        Assertions.assertEquals(List.of(comment1, comment3, comment4), comments);
    }
}