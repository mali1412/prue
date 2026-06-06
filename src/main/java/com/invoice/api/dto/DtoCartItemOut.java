package com.invoice.api.dto;

public class DtoCartItemOut {

    private Integer cart_item_id;
    private String name;        
    private Double price;       
    private Integer quantity;
    private String gtin;

    public DtoCartItemOut() {}

    public DtoCartItemOut(Integer cart_item_id, String gtin, String name, Double price, Integer quantity) {
        this.cart_item_id = cart_item_id;
        this.gtin = gtin;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        }

    public Integer getCart_item_id() {
        return cart_item_id;
    }

    public void setCart_item_id(Integer cart_item_id) {
        this.cart_item_id = cart_item_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getGtin() {
        return gtin;
    }

    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    
}
