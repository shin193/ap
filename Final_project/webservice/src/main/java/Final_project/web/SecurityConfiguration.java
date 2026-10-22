package Final_project.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain security(HttpSecurity http, Sessions sessions, ObjectMapper json) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Authentication is an explicit Bearer header, not a browser cookie.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .requestCache(cache -> cache.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/api/health", "/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/books", "/api/books/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/students").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(errors -> errors
                        .authenticationEntryPoint((request, response, error) -> writeError(json, response, 401,
                                "UNAUTHORIZED", "A valid Bearer token is required"))
                        .accessDeniedHandler((request, response, error) -> writeError(json, response, 403,
                                "FORBIDDEN", "This role cannot perform that action")))
                .addFilterBefore(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                                    FilterChain chain) throws ServletException, IOException {
                        String header = request.getHeader("Authorization");
                        if (header != null && header.startsWith("Bearer ")) {
                            try {
                                sessions.authenticate(header.substring(7)).ifPresent(account -> {
                                    var auth = new UsernamePasswordAuthenticationToken(account, null,
                                            List.of(new SimpleGrantedAuthority("ROLE_" + account.role().name())));
                                    SecurityContextHolder.getContext().setAuthentication(auth);
                                });
                            } catch (org.springframework.dao.DataAccessException error) {
                                writeError(json, response, 503, "STORAGE_UNAVAILABLE", "Database is unavailable");
                                return;
                            }
                        }
                        chain.doFilter(request, response);
                    }
                }, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private static void writeError(ObjectMapper json, HttpServletResponse response,
                                   int status, String code, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        json.writeValue(response.getOutputStream(), Map.of("code", code, "message", message));
    }
}
