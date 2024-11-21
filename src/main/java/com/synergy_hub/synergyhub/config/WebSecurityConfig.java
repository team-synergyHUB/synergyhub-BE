package com.synergy_hub.synergyhub.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import com.synergy_hub.synergyhub.member.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public WebSecurityCustomizer webCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers(toH2Console()) // H2 콘솔 무시
            .requestMatchers("/static/**"); // 정적 리소스 무시
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "signup").permitAll()
                .anyRequest().permitAll()  //모든 경로 허용
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/main")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()) //CSRF 비활성화
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder,
        AuthenticationManagerBuilder authenticationManagerBuilder)
        throws Exception {
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        managerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    @Bean  //Bcrypt 암호화 방식 사용 인코더
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
