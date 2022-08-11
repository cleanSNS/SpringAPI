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
                .antMatchers("/user/auth/signup/**").permitAll()
                .antMatchers("/user/auth/login").permitAll()
                .antMatchers("/user/auth/refresh").permitAll()
                .antMatchers("/social/**").permitAll()
                .antMatchers(HttpMethod.GET, "/page/{^[^0]\\d*}/comment").permitAll()
                .antMatchers("/page/{^[^0]\\d*}/nested").permitAll()
                .antMatchers("/page/{^[^0]\\d*}/detail").permitAll()
                .antMatchers("/page/user/{^[^0]\\d*}").permitAll()

                .antMatchers("/local/user/auth/signup/**").permitAll()
                .antMatchers("/local/user/auth/login").permitAll()
                .antMatchers("/local/user/auth/refresh").permitAll()
                .antMatchers("/local/social/**").permitAll()
                .antMatchers(HttpMethod.GET, "/local/page/{^[^0]\\d*}/comment").permitAll()
                .antMatchers("/local/page/{^[^0]\\d*}/nested").permitAll()
                .antMatchers("/local/page/{^[^0]\\d*}/detail").permitAll()
                .antMatchers("/local/page/user/{^[^0]\\d*}").permitAll()

                .antMatchers("/local/test/**").permitAll()
                .antMatchers("/ws/**").permitAll()
                .antMatchers("/send/**").permitAll()
                .antMatchers("/room/**").permitAll()
                .antMatchers("/**").permitAll()

                .anyRequest().authenticated()

                .and()
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}