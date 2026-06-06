package com.invoice.api.dto;

public class DtoProduct {
    private String gtin;
    private String product;  // nombre del producto
    private Float price;
    private Integer stock;

    public DtoProduct() {}

    public String getGtin() { return gtin; }
    public void setGtin(String gtin) { this.gtin = gtin; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}