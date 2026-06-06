package com.invoice.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoCartItemIn;
import com.invoice.api.dto.DtoCartItemOut;
import com.invoice.api.service.SvcCartItem;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart-item")
@Tag(name = "Cart Item", description = "Administración del carrito de compras")
public class CtrlCartItem {

    @Autowired
    SvcCartItem svc;

    @PostMapping
    @Operation(summary = "Agregar producto al carrito", description = "Agrega o actualiza un producto en el carrito del cliente")
    public ResponseEntity<ApiResponse> addItem(@Valid @RequestBody DtoCartItemIn dto) {
        return ResponseEntity.ok(svc.addItem(dto));
    }

    @GetMapping
    @Operation(summary = "Consultar carrito", description = "Obtiene los productos en el carrito del cliente")
    public ResponseEntity<List<DtoCartItemOut>> getItems() {
        return ResponseEntity.ok(svc.getItems());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar artículo", description = "Elimina un artículo del carrito del cliente")
    public ResponseEntity<ApiResponse> deleteItem(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(svc.deleteItem(id));
    }

    @DeleteMapping
    @Operation(summary = "Vaciar carrito", description = "Elimina todos los artículos del carrito del cliente")
    public ResponseEntity<ApiResponse> deleteAll() {
        return ResponseEntity.ok(svc.deleteAll());
    }
}