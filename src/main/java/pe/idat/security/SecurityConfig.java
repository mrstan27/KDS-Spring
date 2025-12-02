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

                // Todo lo demás cerrado
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login/logincliente") 
                .loginProcessingUrl("/login")
                
                // Éxito:
                .defaultSuccessUrl("/login/login-success", true)
                
                // 🔴 FALLO (AQUÍ ESTÁ EL ARREGLO PARA EL /idat)
                .failureHandler((request, response, exception) -> {
                    String tipoAcceso = request.getParameter("tipoAcceso");
                    
                    // Esto obtendrá "/idat" gracias a tu application.properties
                    String contextPath = request.getContextPath(); 
                    
                    if (tipoAcceso != null && tipoAcceso.equals("admin")) {
                        // Redirige a: /idat/login/loginusuario?error
                        response.sendRedirect(contextPath + "/login/loginusuario?error");
                    } else {
                        // Redirige a: /idat/login/logincliente?error
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