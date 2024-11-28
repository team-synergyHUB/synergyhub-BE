package com.synergy_hub.synergyhub.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import com.synergy_hub.synergyhub.config.sessionconfig.CustomAccessDeniedHandler;
import com.synergy_hub.synergyhub.config.sessionconfig.CustomAuthenticationFailureHandler;
import com.synergy_hub.synergyhub.config.sessionconfig.CustomAuthenticationFilter;
import com.synergy_hub.synergyhub.config.sessionconfig.CustomAuthenticationSuccessHandler;
import com.synergy_hub.synergyhub.config.sessionconfig.CustomLoginAuthenticationEntryPoint;
import com.synergy_hub.synergyhub.member.service.UserDetailsServiceImpl;
import com.synergy_hub.synergyhub.token.jwt.JwtAuthenticationFilter;
import com.synergy_hub.synergyhub.token.jwt.JwtTokenProvider;
import com.synergy_hub.synergyhub.token.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtTokenProvider jwtTokenProvider;


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
                    .requestMatchers("members/login", "/","members/signup").permitAll()
                    .requestMatchers("members/admin").hasRole("ADMIN")
//                .anyRequest().permitAll()  //모든 경로 허용
                    .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), LoginFilter.class)
            .addFilterAt(new LoginFilter(
                    authenticationCustomManager(authenticationConfiguration), jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form.disable())
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .permitAll()
            )
            .sessionManagement(session -> session   //세션 무상태로 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .csrf(csrf -> csrf.disable()) //CSRF 비활성화
            .build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder passwordEncoder,
//        AuthenticationManagerBuilder authenticationManagerBuilder)
//        throws Exception {
//        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        managerBuilder
//            .userDetailsService(userDetailsService)
//            .passwordEncoder(passwordEncoder);
//
//        return authenticationManagerBuilder.build();
//    }

    @Bean
    public CustomAuthenticationFilter ajaxAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter();
        customAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        customAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        // **
        customAuthenticationFilter.setSecurityContextRepository(
            new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
            ));

        return customAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationCustomManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean  //Bcrypt 암호화 방식 사용 인코더
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }
}
