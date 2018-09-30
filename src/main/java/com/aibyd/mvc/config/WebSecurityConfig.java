package com.aibyd.mvc.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.aibyd.appsys.filter.RabcFilterSecurityInterceptor;
import com.aibyd.appsys.service.AppsysUserDetailsService;
import com.aibyd.appsys.utils.MD5Utils;
import com.aibyd.appsys.utils.CommonUtils;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AppsysUserDetailsService userDetailsService;

	@Autowired
	private RabcFilterSecurityInterceptor rabcFilterSecurityInterceptor;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/css/**", "/font-awesome/**", "/fonts/**", "/images/**", "/img/**", "/js/**",
						"/editor/**", "/login")
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage("/login")
				.defaultSuccessUrl("/index")
				// .successHandler(loginSuccessHandler())
				.and().headers().frameOptions().sameOrigin().and().logout().addLogoutHandler(logoutHandler())
				.logoutSuccessUrl("/login").logoutSuccessHandler(logoutSuccessHandler()).invalidateHttpSession(true)
				.and().rememberMe().tokenValiditySeconds(1209600);
		http.addFilterBefore(rabcFilterSecurityInterceptor, FilterSecurityInterceptor.class);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new PasswordEncoder() {

			@Override
			public String encode(CharSequence rawPassword) {
				return MD5Utils.encode((String) rawPassword);
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return encodedPassword.equals(MD5Utils.encode((String) rawPassword));
			}
		});
		auth.eraseCredentials(false);
	}

	// @Bean
	// public BCryptPasswordEncoder passwordEncoder() {
	// return new BCryptPasswordEncoder(4);
	// }

	// @Bean
	// public LoginSuccessHandler loginSuccessHandler() {
	// return new LoginSuccessHandler();
	// }

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new LogoutSuccessHandler() {

			private final Logger LOGGER = LoggerFactory.getLogger(LogoutSuccessHandler.class);

			@Override
			public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				User userDetails = (User) authentication.getPrincipal();
				StringBuffer sb = new StringBuffer();
				sb.append(userDetails.getUsername());
				sb.append(" logout success from ");
				sb.append(CommonUtils.getIpFromRequest(request));
				sb.append(".");
				LOGGER.info(sb.toString());
			}
		};
	}

	private LogoutHandler logoutHandler() {
		return new LogoutHandler() {

			@Override
			public void logout(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) {
				authentication.setAuthenticated(false);
				try {
					response.sendRedirect("/");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		};
	}

}
