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
                
                // --- AQUÍ ESTÁ EL ARREGLO ---
                // Permitimos entrar al formulario de registro y guardar sin estar logueado
                .requestMatchers("/cliente/nuevo", "/cliente/guardar").permitAll() 
                // -----------------------------

                // Permitimos ver productos
                .requestMatchers("/productos/categoria/**", "/productos/detalle/**").permitAll()

                // Todo lo demás cerrado
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
            	    .loginPage("/login/logincliente") // O tu página de login
            	    .loginProcessingUrl("/login")
            	    
            	    // ESTO ES LO IMPORTANTE:
            	    // "true" fuerza a ir a esta ruta siempre que el login sea exitoso
            	    .defaultSuccessUrl("/login/login-success", true)
            	    
            	    .usernameParameter("email")
            	    .passwordParameter("password")
            	    .permitAll()
            	)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login/loginusuario?logout")
                .permitAll()
            );

        return http.build();
    }
}