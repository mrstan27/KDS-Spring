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

                // --- REGLAS DE NEGOCIO CORREGIDAS ---

                // 1. APROBACIÓN DE ÓRDENES (Solo Administrador) - ¡CRÍTICO!
                // Esto evita que 'Compras' pueda aprobar.
                .requestMatchers("/compras/aprobar/**", "/compras/rechazar/**")
                .hasAuthority("Administrador")

                // 2. RECEPCIÓN Y FACTURACIÓN (Admin y Almacenero)
                // Ahora el almacenero tiene permiso explícito para facturar y recepcionar.
                .requestMatchers("/compras/recepcionar/**", "/compras/facturar")
                .hasAnyAuthority("Administrador", "Almacenero")

                // 3. GESTIÓN DE PRODUCTOS (Admin y Compras)
                .requestMatchers("/productos/nuevo", "/productos/guardar", "/productos/editar/**", "/productos/eliminar/**", 
                                 "/categorias/**")
                .hasAnyAuthority("Administrador", "Compras") 

                // 4. GESTIÓN DE COMPRAS/COTIZACIONES (Admin y Compras)
                // Nota: '/compras' (listado) cae aquí si no se especifica antes, permitiendo ver la lista a ambos.
                .requestMatchers("/compras/cotizaciones/**", "/proveedor/**")
                .hasAnyAuthority("Administrador", "Compras")

                // 5. MOVIMIENTOS (Kardex)
                .requestMatchers("/movimientos/**")
                .hasAnyAuthority("Administrador", "Almacenero")
                
                // 6. VENTAS WEB Y POS
                .requestMatchers("/carrito/checkout", "/carrito/procesar-pago", "/mi-cuenta/**").hasAuthority("CLIENTE")
                .requestMatchers("/ventas/**").hasAnyAuthority("Administrador", "Vendedor")
                
                // 7. REPORTES
                .requestMatchers("/reportes/**").hasAnyAuthority("Administrador", "Vendedor", "Almacenero")
                
                // 8. ADMIN
                .requestMatchers("/usuarios/**", "/roles/**").hasAuthority("Administrador")

                // Permite acceso general a /compras para ver la lista (filtrado visualmente en JSP)
                .requestMatchers("/compras").authenticated() 

                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login/loginusuario")
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