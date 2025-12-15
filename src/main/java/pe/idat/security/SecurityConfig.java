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

                // RUTAS PÚBLICAS (Login y Tienda)
                .requestMatchers("/", "/index").permitAll()
                .requestMatchers("/login/**", "/auth/**").permitAll()
                .requestMatchers("/cliente/nuevo", "/cliente/guardar").permitAll()
                // Catálogo público
                .requestMatchers("/productos/categoria/**", "/productos/detalle/**").permitAll() 
                // Carrito público (ver)
                .requestMatchers("/carrito", "/carrito/agregar/**", "/carrito/eliminar/**").permitAll()

                // --- REGLAS DE NEGOCIO ---

                // 1. GESTIÓN DE PRODUCTOS Y CATEGORÍAS (Crear/Editar)
                // CAMBIO: Ahora es 'Compras' quien define el producto, no Almacén.
                .requestMatchers("/productos/nuevo", "/productos/guardar", "/productos/editar/**", "/productos/eliminar/**", 
                                 "/categorias/**")
                .hasAnyAuthority("Administrador", "Compras") 

                // 2. COMPRAS Y PROVEEDORES (Gestión)
                .requestMatchers("/compras/cotizaciones/**", "/proveedor/**")
                .hasAnyAuthority("Administrador", "Compras")

                // 3. RECEPCIÓN EN ALMACÉN (Solo registrar ingreso)
                .requestMatchers("/compras/recepcion/**", "/movimientos/**")
                .hasAnyAuthority("Administrador", "Almacenero")
                
                // 4. VENTAS WEB (Checkout cliente)
                .requestMatchers("/carrito/checkout", "/carrito/procesar-pago", "/mi-cuenta/**")
                .hasAuthority("CLIENTE")

                // 5. VENTAS POS (Vendedor en tienda)
                .requestMatchers("/ventas/**")
                .hasAnyAuthority("Administrador", "Vendedor")
                
                // 6. GESTIÓN DE USUARIOS
                .requestMatchers("/usuarios/**", "/roles/**")
                .hasAuthority("Administrador")

                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login/loginusuario") // Login empleados
                .loginProcessingUrl("/auth/login-process")
                .defaultSuccessUrl("/login/menu", true)
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