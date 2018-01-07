package edu.mit.controller;


import edu.mit.properties.PropertiesConfigurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http.authorizeRequests()
                .antMatchers("/", "/add**","/singleitem**", "/results").hasRole("USER").and().formLogin()
                .loginPage("/login").and().logout().permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        final String user = PropertiesConfigurationUtil.getCredentials().getUsername_app();
        final String password = PropertiesConfigurationUtil.getCredentials().getPassword_app();
        auth
                .inMemoryAuthentication()
                .withUser(user).password(password).roles("USER");
    }
}
