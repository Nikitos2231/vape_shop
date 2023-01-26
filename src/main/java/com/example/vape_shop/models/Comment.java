package com.example.vape_shop.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "comment")
public class Comment implements Comparable<Comment> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int commentId;

    @Column(name = "comment_text")
    @Size(min = 5, max = 100, message = "Символов в сообщении должно быть от 5 до 100")
    private String commentText;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Man man;

    @ManyToOne()
    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    private Item item;

    @Column(name = "comment_date_of_create")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentDateOfCreate;

    public Comment() {
    }

    public Comment(int commentId, Date commentDateOfCreate) {
        this.commentId = commentId;
        this.commentDateOfCreate = commentDateOfCreate;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Man getMan() {
        return man;
    }

    public void setMan(Man man) {
        this.man = man;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Date getCommentDateOfCreate() {
        return commentDateOfCreate;
    }

    public void setCommentDateOfCreate(Date commentDateOfCreate) {
        this.commentDateOfCreate = commentDateOfCreate;
    }

    @Override
    public int compareTo(Comment o) {
        return this.commentDateOfCreate.compareTo(o.getCommentDateOfCreate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return commentId == comment.commentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }
}
