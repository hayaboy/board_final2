package org.zerock.b01.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;  //HttpServletRequest와 일치할 수 있는 필터 체인을 정의합니다. 해당 요청에 적용되는지 여부를 결정하기 위해.
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b01.security.CustomUserDetailsService;
import org.zerock.b01.security.handler.Custom403Handler;
import org.zerock.b01.security.handler.CustomSocialLoginSuccessHandler;

import javax.sql.DataSource;
//FilterChainProxy를 구성하는 데 사용됩니다.

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 어노테이션으로 권한 설정, ,prePostEnabled 원하는 곳에 @PreAuthorize, @PostAuthorize 어노테이션을 이용해서 사전 혹은 사후의 권한 체크
public class CustomSecurityConfig {


    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;



    //RequestMatcher :  Simple strategy to match an HttpServletRequest.
    @Bean  // HttpSecurity : 특정 http 요청에 대해 웹 기반 보안을 구성할 수 있습니다. 기본적으로 모든 요청에 적용되지만 requestMatcher(RequestMatcher) 또는 기타 유사한 메소드를 사용하여 제한할 수 있습니다.
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        log.info("------------configure-------------------");

//        log.info("httpSecurity : " + httpSecurity);
//        log.info("httpSecurity.build()" + httpSecurity.build());
//        log.info("httpSecurity.formLogin() : " + httpSecurity.formLogin());  //Adds form based authentication
//        httpSecurity.formLogin(); //양식 기반 인증을 지원하도록 지정합니다. FormLoginConfigurer.loginPage(String)가 지정되지 않은 경우 기본 로그인 페이지가 생성됩니다.


        //커스텀 로그인 페이지
        httpSecurity.formLogin().loginPage("/member/login");

        // 스프링 시큐리티는 기본적으로 GET 방식을 제외한 POST/PUT/DELETE 요청시 CSRF 토큰 요구하므로 일단 비활성화
        httpSecurity.csrf().disable();

        //rememberMe() : Allows configuring of Remember Me authentication.
        httpSecurity.rememberMe().key("12345678").tokenRepository(persistentTokenRepository()).userDetailsService(userDetailsService) .tokenValiditySeconds(60*60*24*30);


        httpSecurity.exceptionHandling().accessDeniedHandler(accessDeniedHandler());  //403

        httpSecurity.oauth2Login().loginPage("/member/login").successHandler(authenticationSuccessHandler());;


        return httpSecurity.build();
    }


    //소셜 로그인 성공시 처리
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }


    @Bean  //사용자의 영구 로그인 토큰을 저장
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }


    // 정적으로 동작하는 파일들에는 시큐리티 적용 안 함 No security for GET /css/styles.css
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("------------web configure-------------------");

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
