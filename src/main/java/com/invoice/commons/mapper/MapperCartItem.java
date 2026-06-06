package com.invoice.commons.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.invoice.api.dto.DtoCartItemOut;
import com.invoice.api.dto.DtoProduct;
import com.invoice.api.entity.CartItem;

@Service
public class MapperCartItem {

    public DtoCartItemOut toDto(CartItem item, DtoProduct product) {
        return new DtoCartItemOut(
            item.getCart_item_id(),
            item.getGtin(),
            product != null ? product.getProduct() : "N/A",
            product != null ? product.getPrice().doubleValue() : 0.0,
            item.getQuantity()
        );
    }

    public List<DtoCartItemOut> toDtoList(List<CartItem> items, List<DtoProduct> products) {
        List<DtoCartItemOut> result = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            DtoProduct product = i < products.size() ? products.get(i) : null;
            result.add(toDto(items.get(i), product));
        }

        return result;
    }
}