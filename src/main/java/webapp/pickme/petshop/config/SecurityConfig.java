package webapp.pickme.petshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import webapp.pickme.petshop.data.model.user.Role;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = Role.ADMIN.name();

    private static final String[] CSRF_IGNORE = {"/user/login", "user/create"};

    private final DataSource dataSource;

    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager() {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        authorizationConfig(http);
        corsConfig(http);
        csrfConfig(http);
    }

    private void authorizationConfig(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/product/all").permitAll()
                .antMatchers("/product/photo/*").permitAll()
                .antMatchers("/product/filter").permitAll()
                .antMatchers("/user/create").permitAll()
                .antMatchers("/user/login").authenticated()
                .antMatchers("/product/get-by-id-list").hasAuthority(ADMIN)
                .antMatchers("/product/delete/*").hasAuthority(ADMIN)
                .antMatchers("/product/edit/*").hasAuthority(ADMIN)
                .antMatchers("/product/add").hasAuthority(ADMIN)
                .antMatchers("/order/all/*").hasAuthority(ADMIN)
                .antMatchers("/order/change-status/*").hasAuthority(ADMIN)
                .antMatchers("/order/delete/*").hasAuthority(ADMIN)
                .antMatchers("/order/accept/*").hasAuthority(ADMIN)
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/user/logout").permitAll()
                .and()
                .httpBasic();
    }

    private void corsConfig(HttpSecurity http) throws Exception {
        http.cors(c -> {
            CorsConfigurationSource cs = r -> {
                var cc = new CorsConfiguration();
                cc.addAllowedOrigin("http://localHost:4200");
                cc.setAllowedMethods(List.of("HEAD",
                        "GET", "POST", "PUT", "DELETE", "PATCH"));
                cc.setAllowCredentials(true);
                cc.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
                return cc;
            };
            c.configurationSource(cs);
        });
    }

    private void csrfConfig(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.csrf()
//            .ignoringAntMatchers(CSRF_IGNORE)
//            .csrfTokenRepository(csrfTokenRepository())
//            .and()
//            .addFilterAfter(new CustomCsrfFilter(), CsrfFilter.class);
    }

    private CsrfTokenRepository csrfTokenRepository() {
        var repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName(CustomCsrfFilter.CSRF_COOKIE_NAME);
        return repository;
    }

}
