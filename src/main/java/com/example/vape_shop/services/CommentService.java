package com.example.vape_shop.services;

import com.example.vape_shop.models.Comment;
import com.example.vape_shop.models.Item;
import com.example.vape_shop.models.Man;
import com.example.vape_shop.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ManService manService;
    private final ItemService itemService;

    @Autowired
    public CommentService(CommentRepository commentRepository, ManService manService, ItemService itemService) {
        this.commentRepository = commentRepository;
        this.manService = manService;
        this.itemService = itemService;
    }

    @Transactional
    public void save(Comment comment, Man man, Item item) {
        comment.setItem(item);
        comment.setMan(man);
        comment.setCommentDateOfCreate(new Date());
        Man manSelected = manService.findOne(man.getUserId());
        Item itemSelected = itemService.findById(item.getItemId());
        commentRepository.save(comment);
        manService.saveMan(manSelected);
        itemService.save(itemSelected);
    }

    public List<Comment> removeTheSameElementsIntoCommentList(List<Comment> comments) {
        Set<Comment> set = new HashSet<>(comments);
        comments.clear();
        comments.addAll(set);
        Collections.sort(comments);
        return comments;
    }
}
