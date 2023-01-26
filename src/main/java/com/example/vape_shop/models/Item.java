package com.example.vape_shop.models;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "item")
public class Item implements Comparable<Item> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;

    @Column(name = "item_name")
    @Size(min = 5, max = 40, message = "Количество символов в названии должно быть от 5 до 40")
    private String itemName;

    @Column(name = "item_describe")
    @Size(min = 10, max = 500, message = "Количество символов в описании должно быть от 10 до 500")
    private String itemDescribe;

    @Column(name = "item_avatar")
    private String itemAvatar;

    @Column(name = "item_avatar2")
    private String itemAvatar2;

    @Column(name = "item_avatar3")
    private String itemAvatar3;

    @Column(name = "is_sold")
    private boolean isSold = false;

    @Column(name = "item_price")
    @Min(value = 0, message = "Цена не может быть меньше нуля!")
    private int itemPrise;

    @Column(name = "checked")
    private String itemChecked;

    @Column(name = "date_of_create")
    @Temporal(TemporalType.TIMESTAMP)
    private Date itemDateOfCreate;

    @Column(name = "date_of_last_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date itemDateOfLastUpdate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "item_date_of_sale")
    private Date itemDateOfSale;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private Man man;

    @ManyToOne()
    @JoinColumn(name = "buyer_id", referencedColumnName = "user_id")
    private Man buyer;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.REMOVE)
    private List<PurchaseRequest> purchaseRequests;

    public Item() {
    }

    public Item(int itemId) {
        this.itemId = itemId;
    }

    public Item(int itemId, Date itemDateOfCreate) {
        this.itemId = itemId;
        this.itemDateOfCreate = itemDateOfCreate;
    }

    public Item(int itemId, boolean isSold, Date itemDateOfCreate) {
        this.itemId = itemId;
        this.isSold = isSold;
        this.itemDateOfCreate = itemDateOfCreate;
    }

    public Item(int itemId, boolean isSold, String itemChecked) {
        this.itemId = itemId;
        this.isSold = isSold;
        this.itemChecked = itemChecked;
    }

    public Item(Date itemDateOfCreate) {
        this.itemDateOfCreate = itemDateOfCreate;
    }



    public Date getItemDateOfSale() {
        return itemDateOfSale;
    }

    public Date getItemDateOfLastUpdate() {
        return itemDateOfLastUpdate;
    }

    public void setItemDateOfLastUpdate(Date itemDateOfLastUpdate) {
        this.itemDateOfLastUpdate = itemDateOfLastUpdate;
    }

    public void setItemDateOfSale(Date itemDateOfSale) {
        this.itemDateOfSale = itemDateOfSale;
    }

    public Man getBuyer() {
        return buyer;
    }

    public void setBuyer(Man buyer) {
        this.buyer = buyer;
    }

    public boolean isSold() {
        return isSold;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    public List<PurchaseRequest> getPurchaseRequests() {
        return purchaseRequests;
    }

    public void setPurchaseRequests(List<PurchaseRequest> purchaseRequests) {
        this.purchaseRequests = purchaseRequests;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getItemChecked() {
        return itemChecked;
    }

    public void setItemChecked(String itemChecked) {
        this.itemChecked = itemChecked;
    }

    public Date getItemDateOfCreate() {
        return itemDateOfCreate;
    }

    public void setItemDateOfCreate(Date itemDateOfCreate) {
        this.itemDateOfCreate = itemDateOfCreate;
    }

    public int getItemPrise() {
        return itemPrise;
    }

    public void setItemPrise(int itemPrise) {
        this.itemPrise = itemPrise;
    }

    public String getItemAvatar() {
        return itemAvatar;
    }

    public void setItemAvatar(String itemAvatar) {
        this.itemAvatar = itemAvatar;
    }

    public String getItemAvatar2() {
        return itemAvatar2;
    }

    public void setItemAvatar2(String itemAvatar2) {
        this.itemAvatar2 = itemAvatar2;
    }

    public String getItemAvatar3() {
        return itemAvatar3;
    }

    public void setItemAvatar3(String itemAvatar3) {
        this.itemAvatar3 = itemAvatar3;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescribe() {
        return itemDescribe;
    }

    public void setItemDescribe(String itemDescribe) {
        this.itemDescribe = itemDescribe;
    }

    public Man getMan() {
        return man;
    }

    public void setMan(Man man) {
        this.man = man;
    }

    @Override
    public int compareTo(Item o) {
        return this.itemDateOfCreate.compareTo(o.getItemDateOfCreate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemId == item.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
