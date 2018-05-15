package hr.foi.thesis.configuration;

import hr.foi.thesis.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private SecurityPropertiesConfiguration securityConfiguration;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
            .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser(securityConfiguration.getUsername())
            .password(securityConfiguration.getPassword())
            .roles(securityConfiguration.getRole())
                .and()
            .passwordEncoder(new PasswordEncoder() {
                @Override
                public String encode(CharSequence cs) {
                    return PasswordUtil.getEncryptedPassword(cs.toString(), securityConfiguration.getSalt(),
                           securityConfiguration.getIterationCount(), securityConfiguration.getKeyLength());
                }

                @Override
                public boolean matches(CharSequence cs, String string) {
                    return encode(cs).equals(string);
                }
            });
    }
}
