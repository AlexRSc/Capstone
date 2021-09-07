package de.neuefische.CapStone.backend.config;

import de.neuefische.CapStone.backend.filter.JwtAuthFilter;
import de.neuefische.CapStone.backend.service.UserEntityDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserEntityDetailsService detailsService;
    private final String[] SWAGGER_URLS = {"/v/api-docs/**", "/swagger-ui/**", "/swagger-resources/**"};

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserEntityDetailsService detailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.detailsService = detailsService;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder();    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(GET, SWAGGER_URLS).permitAll()
                .antMatchers(POST, "/user/login").permitAll()
                .antMatchers(POST, "/user/registration").permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(GET, SWAGGER_URLS);
    }

}
