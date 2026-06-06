package com.invoice.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.invoice.api.entity.CartItem;

@Repository
public interface RepoCartItem extends JpaRepository<CartItem, Integer> {

    //Obtener todos los items del carrito
	@Query("SELECT i FROM CartItem i WHERE i.user_id = :user_id")
    List<CartItem> findAllByUserId(@Param("user_id") Integer user_id);	


    // Buscar un item específico por usuario + gtin (para saber si ya existe en el carrito)
    @Query("SELECT i FROM CartItem i WHERE i.user_id = :user_id AND i.gtin = :gtin AND i.status = 1")
    CartItem findByUserIdAndGtin(@Param("user_id") Integer user_id, @Param("gtin") String gtin);

    // Actualizar cantidad cuando el producto ya estaba en el carrito
    @Modifying
    @Transactional
    @Query("UPDATE CartItem i SET i.quantity = :quantity WHERE i.cart_item_id = :id")
    void updateQuantity(@Param("id") Integer id, @Param("quantity") Integer quantity);

    // Vaciar carrito (soft delete con status = 0)
    @Modifying
    @Transactional
    @Query("UPDATE CartItem i SET i.status = 0 WHERE i.user_id = :user_id")
    void deleteAllByUserId(@Param("user_id") Integer user_id);
}
