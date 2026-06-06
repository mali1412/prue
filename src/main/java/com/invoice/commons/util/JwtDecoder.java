// package com.invoice.commons.util;

// import java.util.Map;

// import org.springframework.http.HttpStatus;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Component;

// import com.invoice.exception.ApiException;

// @Component
// public class JwtDecoder {
	
// 	public boolean isAdmin() {
// 		try {
// 			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

// 	        if (authentication != null && authentication.isAuthenticated()) {
// 	            return authentication.getAuthorities()
// 	                .stream()
// 	                .anyMatch(authority -> "ADMIN".equals(authority.getAuthority()));
// 	        }

// 	        return false;
// 		}catch(Exception e) {
// 			System.out.println("El usuario no es administrador");
// 			return false;
// 		}
// 	}
	
// 	public Integer getUserId() {
// 		try {
// 			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
// 			Map<String, Object> payload = (Map<String, Object>) authentication.getCredentials();
// 			return (Integer) payload.get("id");
// 		}catch(Exception e) {
// 			throw new ApiException(HttpStatus.PRECONDITION_FAILED, "El usuario es inválido");
// 		}
// 	}
// }

package com.invoice.commons.util;

import org.springframework.stereotype.Component;

/**
 * Versión simplificada para pruebas sin auth-service.
 *
 * getUserId() devuelve siempre el mismo user_id fijo (1).
 * Cambia el valor de TEST_USER_ID según el cliente que quieras simular.
 *
 * isAdmin() devuelve false (comportamiento de cliente normal).
 *
 * Cuando integres el auth-service real, restaura la implementación
 * original que lee del SecurityContext.
 */
@Component
public class JwtDecoder {

    // ← Cambia este valor para simular distintos clientes
    private static final Integer TEST_USER_ID = 1;

    public boolean isAdmin() {
        return false;
    }

    public Integer getUserId() {
        return TEST_USER_ID;
    }
}
