package pe.idat.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public DetalleUsuarioService detalleUsuarioService() {
        return new DetalleUsuarioService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(detalleUsuarioService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()

                // RUTAS PÚBLICAS
                .requestMatchers("/", "/index").permitAll()
                .requestMatchers("/login/**", "/auth/**").permitAll()
                .requestMatchers("/cliente/nuevo", "/cliente/guardar").permitAll()
                .requestMatchers("/productos/categoria/**", "/productos/detalle/**").permitAll() 
                .requestMatchers("/carrito", "/carrito/agregar/**", "/carrito/eliminar/**").permitAll()

                // --- REGLAS DE NEGOCIO ---
                
                // MENÚ PRINCIPAL
                .requestMatchers("/login/menu").authenticated()

                // 1. ADMIN
                .requestMatchers("/usuarios/**", "/roles/**", "/compras/aprobar/**", "/compras/rechazar/**").hasAuthority("Administrador")

                // 2. ALMACÉN
                .requestMatchers("/compras/recepcionar/**", "/compras/facturar", "/movimientos/**").hasAnyAuthority("Administrador", "Almacenero")

                // 3. COMPRAS
                .requestMatchers("/productos/nuevo", "/productos/guardar", "/productos/editar/**", "/productos/eliminar/**", 
                                 "/categorias/**", "/compras/cotizaciones/**", "/proveedor/**").hasAnyAuthority("Administrador", "Compras")

                // 4. VENTAS WEB (Cliente)
                .requestMatchers("/carrito/checkout", "/carrito/procesar-pago", "/mi-cuenta/**").hasAuthority("CLIENTE")

                // 5. VENTAS POS (Vendedor)
                .requestMatchers("/ventas/**").hasAnyAuthority("Administrador", "Vendedor")
                
                // 6. REPORTES
                .requestMatchers("/reportes/**").hasAnyAuthority("Administrador", "Vendedor", "Almacenero")
                
                // 7. BACKUP Y RESTAURACIÓN (NUEVO - Solo Admin y Soporte)
                .requestMatchers("/backup/**").hasAnyAuthority("Administrador", "Soporte")

                // Acceso general a listados
                .requestMatchers("/compras").authenticated() 

                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login/loginusuario")
                .loginProcessingUrl("/auth/login-process")
                .defaultSuccessUrl("/login/login-success", true) 
                .failureHandler((request, response, exception) -> {
                    String tipoAcceso = request.getParameter("tipoAcceso");
                    String contextPath = request.getContextPath();
                    if (tipoAcceso != null && tipoAcceso.equals("cliente")) {
                        response.sendRedirect(contextPath + "/login/logincliente?error");
                    } else {
                        response.sendRedirect(contextPath + "/login/loginusuario?error");
                    }
                })
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }
}