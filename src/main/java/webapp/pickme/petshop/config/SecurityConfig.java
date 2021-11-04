package webapp.pickme.petshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import webapp.pickme.petshop.data.model.user.Role;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ADMIN = Role.ADMIN.name();

    private static final String USER = Role.USER.name();

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
    }

    private static void authorizationConfig(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/product/all").hasAnyRole(ADMIN, USER)
                .antMatchers("/product/filter").hasAnyRole(ADMIN, USER)
                .antMatchers("/product/delete/**").hasRole(ADMIN)
                .antMatchers("/product/edit/**").hasRole(ADMIN)
                .antMatchers("/product/add").hasRole(ADMIN)
                .antMatchers("/order/add").hasAnyRole(ADMIN, USER)
                .antMatchers("/order/all/**").hasRole(ADMIN)
                .antMatchers("/order/change-status/**").hasRole(ADMIN)
                .antMatchers("/order/delete/**").hasRole(ADMIN)
                .antMatchers("/order/accept/**").hasRole(ADMIN)
                .and()
                .httpBasic();
    }

    private static void corsConfig(HttpSecurity http) throws Exception {
        http.cors(c -> {
            CorsConfigurationSource cs = r -> {
                var cc = new CorsConfiguration();
                cc.addAllowedOrigin("http://localHost:4200");
                cc.addAllowedMethod(HttpMethod.GET);
                cc.addAllowedMethod(HttpMethod.POST);
                cc.addAllowedMethod(HttpMethod.PUT);
                cc.addAllowedMethod(HttpMethod.DELETE);
                return cc;
            };
            c.configurationSource(cs);
        });
    }
}
