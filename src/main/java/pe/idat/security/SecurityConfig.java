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
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                
                // RUTAS PÚBLICAS
                .requestMatchers("/", "/index").permitAll()
                .requestMatchers("/login/**", "/auth/**").permitAll()
                .requestMatchers("/cliente/nuevo", "/cliente/guardar").permitAll() 
                .requestMatchers("/productos/categoria/**", "/productos/detalle/**").permitAll()

                // --- REGLAS ESTRICTAS DE NEGOCIO ---
                
                // 1. GESTIÓN DE PRODUCTOS (Crear/Editar): Solo Admin y Almacenero. 
                // EL VENDEDOR YA NO PUEDE ENTRAR AQUÍ.
                .requestMatchers("/productos/nuevo", "/productos/guardar", "/productos/editar/**", "/productos/eliminar/**")
                    .hasAnyAuthority("Administrador", "Almacenero")

                // 2. RECEPCIÓN DE MERCADERÍA Y FACTURAS: Exclusivo de Almacén (y Admin)
                // El usuario de 'Compras' ya no puede hacer esto.
                .requestMatchers("/compras/recepcionar/**", "/compras/facturar")
                    .hasAnyAuthority("Administrador", "Almacenero")

                // 3. MOVIMIENTOS DE ALMACÉN: Exclusivo de Almacén (y Admin)
                .requestMatchers("/movimientos/**")
                    .hasAnyAuthority("Administrador", "Almacenero")

                // 4. USUARIOS Y ROLES: Solo Admin
                .requestMatchers("/usuarios/**", "/roles/**").hasAuthority("Administrador")

                // Todo lo demás requiere autenticación genérica
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login/logincliente") 
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/login/login-success", true)
                .failureHandler((request, response, exception) -> {
                    String tipoAcceso = request.getParameter("tipoAcceso");
                    String contextPath = request.getContextPath(); 
                    if (tipoAcceso != null && tipoAcceso.equals("admin")) {
                        response.sendRedirect(contextPath + "/login/loginusuario?error");
                    } else {
                        response.sendRedirect(contextPath + "/login/logincliente?error");
                    }
                })
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/logincliente?logout")
                .permitAll()
            );

        return http.build();
    }
}