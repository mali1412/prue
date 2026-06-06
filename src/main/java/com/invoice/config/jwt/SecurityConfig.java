// package com.invoice.config.jwt;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// @Configuration
// public class SecurityConfig {

// 	@Autowired
// 	private JwtAuthFilter jwtFilter;
	
// 	@Bean
// 	SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfig corsConfig) throws Exception {
	
// 		http.csrf(AbstractHttpConfigurer::disable)
// 		.authorizeHttpRequests(
// 				auth -> auth
// 				.requestMatchers("/error", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/info", "/actuator/health").permitAll()
// 				.requestMatchers("/cart-item/**").hasAuthority("CUSTOMER")
// 				.requestMatchers(HttpMethod.GET, "/invoice/**").hasAnyAuthority("ADMIN", "CUSTOMER")
// 				.requestMatchers(HttpMethod.POST,"/invoice").hasAuthority("CUSTOMER")
// 				)
// 		.cors(cors -> cors.configurationSource(corsConfig))
// 		.httpBasic(Customizer.withDefaults())
// 		.formLogin(form -> form.disable())
// 		.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// 		.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
			
// 		return http.build();
// 	}
// }

package com.invoice.config.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // -------------------------------------------------------
    // Para pruebas sin auth-service: todos los endpoints son
    // accesibles sin token. El user_id viene fijo desde
    // JwtDecoder (ver abajo).
    // Cuando quieras reactivar seguridad, descomenta el bloque
    // original y elimina el permiteAll().
    // -------------------------------------------------------

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }
}
