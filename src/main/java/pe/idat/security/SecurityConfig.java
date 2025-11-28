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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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
                // Permitir forwarding interno (login y errores)
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                
                // Recursos estáticos públicos
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                
                // RUTAS PÚBLICAS ESPECÍFICAS
                .requestMatchers("/login/**").permitAll() // Login de usuario y cliente
                .requestMatchers("/index").permitAll()    // Landing page
                
                // TODO LO DEMÁS: CERRADO 🔒
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login/loginusuario") // Tu URL personalizada
                .loginProcessingUrl("/login")     // A donde hace POST el formulario
                .defaultSuccessUrl("/login/menu", true) // A donde va si es éxito
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