package com.invoice.api.dto;
import jakarta.validation.constraints.NotNull;

public class DtoCartItemIn {

    @NotNull(message = "gtin es obligatorio")
    private String gtin;

    @NotNull(message = "quantity es obligatorio")
    private Integer quantity;

    public DtoCartItemIn() {
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
