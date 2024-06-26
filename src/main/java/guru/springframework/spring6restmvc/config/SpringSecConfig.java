package guru.springframework.spring6restmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecConfig {
    private final HttpSecurity httpSecurity;

    public SpringSecConfig(HttpSecurity httpSecurity) {
        this.httpSecurity = httpSecurity;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      //  http.csrf().ignoringRequestMatchers("/api/**");
        return http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}
