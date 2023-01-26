package com.example.vape_shop.models;

import com.example.vape_shop.custom_annotation.DateOfBirth;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "man")
public class Man {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_email")
    @Email(message = "Адрес электронной почты должен быть валидным!")
    @NotEmpty(message = "Адрес электронной почты не может быть пустым!")
    private String userEmail;

    @Column(name = "user_name")
    @Size(min = 2, max = 30, message = "Имя должно иметь от 2 до 30 символов!")
    @NotEmpty(message = "Это поле не может быть пустым!")
    private String userName;

    @Column(name = "user_surname")
    @Size(min = 2, max = 30, message = "Фамилия должна иметь от 2 до 30 символов!")
    @NotEmpty(message = "Это поле не может быть пустым!")
    private String userSurname;

    @Column(name = "user_date_of_birth")
//    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Дата рождения не может быть пустой!")
    @DateOfBirth(message = "Вам должно быть больше 18-ти лет!")
    private Date userDateOfBirth;

    @Column(name = "user_password")
    @Size(min = 5, max = 100, message = "Пароль должен содержать от 5 до 100 символов!")
    private String userPassword;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "count_stars")
    private int userCountStars;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "telegram_link")
    private String telegramLink;

    @OneToMany(mappedBy = "man", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private Set<Item> items;

    @OneToMany(mappedBy = "man", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private Set<Comment> comments;

    @OneToMany(mappedBy = "man", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private Set<PurchaseRequest> purchaseRequests;

    @OneToMany(mappedBy = "buyer", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<Item> BoughtItems;

    @OneToMany(mappedBy = "manToFeedback", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private Set<Feedback> manToFeedback;

    @OneToMany(mappedBy = "manFromFeedback", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private Set<Feedback> manFromFeedback;

    public Man() {
    }

    public Man(int userId, String userEmail, String userName, String userSurname, Date userDateOfBirth, String userPassword, String userRole, String activationCode, int userCountStars, String avatar, String telegramLink, Set<Item> items, Set<Comment> comments, Set<PurchaseRequest> purchaseRequests, List<Item> boughtItems, Set<Feedback> manToFeedback, Set<Feedback> manFromFeedback) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userDateOfBirth = userDateOfBirth;
        this.userPassword = userPassword;
        this.userRole = userRole;
        this.activationCode = activationCode;
        this.userCountStars = userCountStars;
        this.avatar = avatar;
        this.telegramLink = telegramLink;
        this.items = items;
        this.comments = comments;
        this.purchaseRequests = purchaseRequests;
        BoughtItems = boughtItems;
        this.manToFeedback = manToFeedback;
        this.manFromFeedback = manFromFeedback;
    }

    public String getTelegramLink() {
        return telegramLink;
    }

    public void setTelegramLink(String telegramLink) {
        this.telegramLink = telegramLink;
    }

    public Set<Feedback> getManToFeedback() {
        return manToFeedback;
    }

    public void setManToFeedback(Set<Feedback> manToFeedback) {
        this.manToFeedback = manToFeedback;
    }

    public Set<Feedback> getManFromFeedback() {
        return manFromFeedback;
    }

    public void setManFromFeedback(Set<Feedback> manFromFeedback) {
        this.manFromFeedback = manFromFeedback;
    }

    public Set<PurchaseRequest> getPurchaseRequests() {
        return purchaseRequests;
    }

    public void setPurchaseRequests(Set<PurchaseRequest> purchaseRequests) {
        this.purchaseRequests = purchaseRequests;
    }

    public List<Item> getBoughtItems() {
        return BoughtItems;
    }

    public void setBoughtItems(List<Item> boughtItems) {
        BoughtItems = boughtItems;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public int getUserCountStars() {
        return userCountStars;
    }

    public void setUserCountStars(int userCountStars) {
        this.userCountStars = userCountStars;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public Date getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public void setUserDateOfBirth(Date userDateOfBirth) {
        this.userDateOfBirth = userDateOfBirth;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


    @Override
    public String toString() {
        return "Man{" +
                "userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", userSurname='" + userSurname + '\'' +
                ", userDateOfBirth=" + userDateOfBirth +
                ", userPassword='" + userPassword + '\'' +
                ", userRole='" + userRole + '\'' +
                ", activationCode='" + activationCode + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
