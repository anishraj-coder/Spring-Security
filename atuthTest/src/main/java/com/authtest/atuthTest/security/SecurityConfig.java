package com.authtest.atuthTest.security;

import com.authtest.atuthTest.security.oauth2.OAuth2FailureHandler;
import com.authtest.atuthTest.security.oauth2.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration){
        return configuration.getAuthenticationManager();
    }
//    @Bean
//    public UserDetailsService  userDetailsService(){
//        User.UserBuilder userBuilder=User.withDefaultPasswordEncoder();
//        UserDetails user1=userBuilder.username("anish").password("123").roles("ADMIN").build();
//        UserDetails user2=userBuilder.username("ankit").password("122").roles("ADMIN").build();
//        return new InMemoryUserDetailsManager(user1,user2);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.cors(Customizer.withDefaults())
                .csrf(csrf->csrf.disable())
                .sessionManagement(session->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->
                        auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/user/**").authenticated()
                                .anyRequest().authenticated()
                ).exceptionHandling(ex->
                        ex.authenticationEntryPoint((request,
                                                     response,
                                                     authError)->{
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{ \"error\": \"Unauthorized\", \"message\": \"" +
                            authError.getMessage() + "\" }");
                }))
                .oauth2Login(oauth2->oauth2
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                ).logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}