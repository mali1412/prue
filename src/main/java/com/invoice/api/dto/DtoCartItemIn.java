package com.invoice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class DtoCartItemIn {

    @NotNull(message = "gtin es obligatorio")
    @NotBlank(message = "gtin no puede estar vacío")
    private String gtin;

    @NotNull(message = "quantity es obligatorio")
    @Min(value = 1, message = "quantity debe ser mayor a 0")
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
