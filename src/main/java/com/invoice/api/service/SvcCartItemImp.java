package com.invoice.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCartItemIn;
import com.invoice.api.dto.DtoCartItemOut;
import com.invoice.api.dto.DtoProduct;
import com.invoice.api.entity.CartItem;
import com.invoice.api.repository.RepoCartItem;
import com.invoice.commons.mapper.MapperCartItem;
import com.invoice.commons.util.JwtDecoder;
import com.invoice.exception.ApiException;
import com.invoice.exception.DBAccessException;

@Service
@Transactional
public class SvcCartItemImp implements SvcCartItem {
    
    @Autowired
    private RepoCartItem repo;
    
    @Autowired
    private MapperCartItem mapper;
    
    @Autowired
    private JwtDecoder jwtDecoder;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String PRODUCT_URL = "http://localhost:8083/product/gtin/";

    @Override
    public ApiResponse addItem(DtoCartItemIn dto) {
        try {
            DtoProduct product;
            try {
                product = restTemplate.getForObject(
                    PRODUCT_URL + dto.getGtin(), DtoProduct.class
                );
            } catch (HttpClientErrorException | HttpServerErrorException e) {
                throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, 
                    "El servicio de productos no está disponible");
            } catch (Exception e) {
                throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, 
                    "Error al consultar el servicio de productos");
            }

            if (product == null) {
                throw new ApiException(HttpStatus.NOT_FOUND, "El producto no existe");
            }

            if (product.getStock() < dto.getQuantity()) {
                throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Stock insuficiente para el producto " + product.getProduct());
            }

            Integer user_id = jwtDecoder.getUserId();

            CartItem existing = repo.findByUserIdAndGtin(user_id, dto.getGtin());
            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + dto.getQuantity());
                repo.save(existing);
            } else {
                CartItem item = new CartItem();
                item.setUser_id(user_id);
                item.setGtin(dto.getGtin());
                item.setQuantity(dto.getQuantity());
                item.setStatus(1);
                repo.save(item);
            }

            return new ApiResponse("Producto agregado al carrito");
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }


    @Override
    public List<DtoCartItemOut> getItems() {
        try {
            Integer user_id = jwtDecoder.getUserId();
            List<CartItem> items = repo.findAllByUserId(user_id);
            List<DtoProduct> products = new ArrayList<>();

            for (CartItem item : items) {
                try {
                    DtoProduct product = restTemplate.getForObject(
                        PRODUCT_URL + item.getGtin(), DtoProduct.class
                    );
                    products.add(product);
                } catch (Exception e) {
                    throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, 
                        "Error al consultar el servicio de productos");
                }
            }

            return mapper.toDtoList(items, products);
        } catch (DataAccessException e) {
            throw new DBAccessException(e);
        }
    }

    @Override
    public ApiResponse deleteItem(Integer cart_item_id) {
        try {
            Integer user_id = jwtDecoder.getUserId();

            CartItem item = repo.findById(cart_item_id)
                .orElseThrow(() -> new ApiException(
                    HttpStatus.NOT_FOUND, "El artículo no existe en el carrito"));

            if (!item.getUser_id().equals(user_id)) {
                throw new ApiException(HttpStatus.FORBIDDEN,
                    "No tienes permiso para eliminar este artículo");
            }

            repo.deleteById(cart_item_id);
            return new ApiResponse("Artículo eliminado del carrito");
        } catch (DataAccessException e) {
            throw new DBAccessException(e); 
        }
    }

    @Override
    public ApiResponse deleteAll() {
        try {
            Integer user_id = jwtDecoder.getUserId();
            repo.deleteAllByUserId(user_id);
            return new ApiResponse("Carrito vaciado");
        } catch (DataAccessException e) {
            throw new DBAccessException(e); 
        }
    }
}
