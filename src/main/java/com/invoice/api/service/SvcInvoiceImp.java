package com.invoice.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.invoice.api.dto.ApiResponse;
import com.invoice.api.dto.DtoInvoiceList;
import com.invoice.api.dto.DtoProduct;
import com.invoice.api.entity.CartItem;
import com.invoice.api.entity.Invoice;
import com.invoice.api.entity.InvoiceItem;
import com.invoice.api.repository.RepoCartItem;
import com.invoice.api.repository.RepoInvoice;
import com.invoice.commons.mapper.MapperInvoice;
import com.invoice.commons.util.JwtDecoder;
import com.invoice.exception.ApiException;
import com.invoice.exception.DBAccessException;

@Service
public class SvcInvoiceImp implements SvcInvoice {

	@Autowired
	private RepoCartItem repoCartItem;

	private final RestTemplate restTemplate = new RestTemplate();
	private final String PRODUCT_URL        = "http://localhost:8083/product/gtin/";
	private final String PRODUCT_URL_UPDATE = "http://localhost:8083/product/";

	@Autowired
    private RepoInvoice repo;
	
	@Autowired
	private JwtDecoder jwtDecoder;
	
	@Autowired
	MapperInvoice mapper;

	@Override
	public List<DtoInvoiceList> findAll() {
		try {
			if(jwtDecoder.isAdmin()) {
				return mapper.toDtoList(repo.findAll());
			}else {
				Integer user_id = jwtDecoder.getUserId();
				return mapper.toDtoList(repo.findAllByUserId(user_id));
			}
		}catch (DataAccessException e) {
	        throw new DBAccessException();
	    }
	}

	@Override
	public Invoice findById(Integer id) {
		try {
			Invoice invoice = repo.findById(id).get();
			if(!jwtDecoder.isAdmin()) {
				Integer user_id = jwtDecoder.getUserId();
				if(!invoice.getUser_id().equals(user_id)) {
					throw new ApiException(HttpStatus.FORBIDDEN, "El token no es válido para consultar esta factura");
				}
			}
			return invoice;
		}catch (DataAccessException e) {
	        throw new DBAccessException();
	    }catch (NoSuchElementException e) {
			throw new ApiException(HttpStatus.NOT_FOUND, "El id de la factura no existe");
	    }
	}

	@Override
	public ApiResponse create() {
	    try {
	        Integer user_id = jwtDecoder.getUserId();

	        // Paso 1: Obtener los items del carrito del cliente
	        List<CartItem> cartItems = repoCartItem.findAllByUserId(user_id);

	        if (cartItems.isEmpty()) {
	            throw new ApiException(HttpStatus.BAD_REQUEST, "El carrito está vacío");
	        }

	        // Pasos 2 y 3: Validar stock y calcular totales
	        List<InvoiceItem> invoiceItems = new ArrayList<>();
	        double totalFactura = 0.0;
	        double taxesFactura = 0.0;

	        for (CartItem cartItem : cartItems) {
	            // Consultar producto al microservicio
	            DtoProduct product = restTemplate.getForObject(
	                PRODUCT_URL + cartItem.getGtin(), DtoProduct.class
	            );

	            if (product == null) {
	                throw new ApiException(HttpStatus.NOT_FOUND,
	                    "El producto con gtin " + cartItem.getGtin() + " no existe");
	            }

	            // Validar stock suficiente
	            if (product.getStock() < cartItem.getQuantity()) {
	                throw new ApiException(HttpStatus.BAD_REQUEST,
	                    "Stock insuficiente para el producto " + product.getProduct());
	            }

	            // Calcular totales por item
	            // total = cantidad x precio unitario
	            // taxes = total * 0.16
	            // subtotal = total - taxes
	            double itemTotal    = cartItem.getQuantity() * product.getPrice();
	            double itemTaxes    = itemTotal * 0.16;
	            double itemSubtotal = itemTotal - itemTaxes;

	            totalFactura  += itemTotal;
	            taxesFactura  += itemTaxes;

	            InvoiceItem invoiceItem = new InvoiceItem();
	            invoiceItem.setGtin(cartItem.getGtin());
	            invoiceItem.setQuantity(cartItem.getQuantity());
	            invoiceItem.setUnit_price(product.getPrice().doubleValue());
	            invoiceItem.setTotal(itemTotal);
	            invoiceItem.setTaxes(itemTaxes);
	            invoiceItem.setSubtotal(itemSubtotal);
	            invoiceItem.setStatus(1);
	            invoiceItems.add(invoiceItem);
	        }

	        double subtotalFactura = totalFactura - taxesFactura;

	        // Paso 4: Guardar la factura con sus items
	        Invoice invoice = new Invoice();
	        invoice.setUser_id(user_id);
	        invoice.setCreated_at(LocalDate.now().toString());
	        invoice.setTotal(totalFactura);
	        invoice.setTaxes(taxesFactura);
	        invoice.setSubtotal(subtotalFactura);
	        invoice.setStatus(1);
	        invoice.setItems(invoiceItems);
	        repo.save(invoice); // guarda invoice e invoice_items por el CascadeType.ALL

	        // Paso 5: Restar stock de cada producto
	        for (InvoiceItem invoiceItem : invoiceItems) {
	            restTemplate.put(
	                PRODUCT_URL_UPDATE + invoiceItem.getGtin() + "/stock",
	                invoiceItem.getQuantity()
	            );
	        }

	        // Paso 6: Vaciar el carrito
	        repoCartItem.deleteAllByUserId(user_id);

	        return new ApiResponse("La factura ha sido registrada");
		} catch (ApiException e) {
    	throw e;
	    } catch (DataAccessException e) {
	        throw new DBAccessException();
	    }
	}
}
