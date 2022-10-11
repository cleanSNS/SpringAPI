package cleanbook.com.config;

import cleanbook.com.jwt.*;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // WebSecurity Configure
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                        .antMatchers(
                        "/h2-console/**"
                        ,"/favicon.ico"
                        ,"/error"
                );
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // enable h2-console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                .antMatchers("/user/auth/signup/**").permitAll() // 회원가입, 이메일 인증
                .antMatchers("/user/auth/login").permitAll()
                .antMatchers("/user/auth/refresh").permitAll()
                .antMatchers("/social/**").permitAll()
                .antMatchers("/page/{^[^0]\\d*}/detail").permitAll() // 게시글 상세보기
                .antMatchers("/page/user/{^[^0]\\d*}").permitAll() // 유저 게시글 조회
                .antMatchers("/page/search/hashtag/**").permitAll() // 해시태그로 게시글 검색
                .antMatchers("/user/search").permitAll() // 유저 검색
                .antMatchers(HttpMethod.GET, "/page/{^[^0]\\d*}/comment").permitAll()
                .antMatchers("/page/{^[^0]\\d*}/nested").permitAll()
                .antMatchers("/user/{^[^0]\\d*}/profile").permitAll()

                .antMatchers("/local/user/auth/signup/**").permitAll() // 회원가입, 이메일 인증
                .antMatchers("/local/user/auth/login").permitAll()
                .antMatchers("/local/user/auth/refresh").permitAll()
                .antMatchers("/local/social/**").permitAll()
                .antMatchers("/local/page/{^[^0]\\d*}/detail").permitAll() // 게시글 상세보기
                .antMatchers("/local/page/user/{^[^0]\\d*}").permitAll() // 유저 게시글 조회
                .antMatchers("/local/page/search/hashtag/**").permitAll() // 해시태그로 게시글 검색
                .antMatchers("/local/user/search").permitAll() // 유저 검색
                .antMatchers(HttpMethod.GET, "/local/page/{^[^0]\\d*}/comment").permitAll()
                .antMatchers("/local/page/{^[^0]\\d*}/nested").permitAll()
                .antMatchers("/local/user/{^[^0]\\d*}/profile").permitAll()

                .antMatchers("/ws/**").permitAll()

                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}