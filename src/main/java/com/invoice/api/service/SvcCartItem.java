package com.invoice.api.service;

import java.util.List;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCartItemIn;
import com.invoice.api.dto.DtoCartItemOut;


public interface SvcCartItem {

    public ApiResponse addItem(DtoCartItemIn dto);
    public List<DtoCartItemOut> getItems();
    public ApiResponse deleteItem(Integer cart_item_id);
    public ApiResponse deleteAll();
}
