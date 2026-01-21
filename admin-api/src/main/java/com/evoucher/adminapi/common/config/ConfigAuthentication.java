package com.evoucher.adminapi.common.config;

import com.evoucher.adminapi.auth.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;


@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class ConfigAuthentication extends WebSecurityConfigurerAdapter {

    private final AdminService userService;
    private final PasswordEncoder encoder;
    private final LoggedInUserContextFilter loggedInUserContextFilter;

    @Bean
    public DaoAuthenticationProvider authProvider() {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_OPERATOR " +
                "\n ROLE_OPERATOR > ROLE_SUPPLIER " +
                "\n ROLE_SUPPLIER > ROLE_BRAND " +
                "\n ROLE_BRAND > ROLE_STORE " +
                "\n ROLE_OPERATOR > ROLE_CUSTOMER ";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandlera() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/download/images").authenticated()

                .antMatchers("/customer/search", "/customer/search/{page}/{pageSize}").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_SUPPLIER")
                .antMatchers(HttpMethod.PATCH, "/customer/{id}").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST,"/customer").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/customer/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/customer/{id}").hasAuthority("ROLE_CUSTOMER")
                .antMatchers(HttpMethod.GET, "/customer/{id}").hasAuthority("ROLE_CUSTOMER")

                .antMatchers(HttpMethod.PATCH, "/suppliers/{id}").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST,"/suppliers").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/suppliers/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/suppliers/{id}").hasAuthority("ROLE_SUPPLIER")
                .antMatchers(HttpMethod.GET, "/suppliers/{id}").hasAuthority("ROLE_SUPPLIER")
                .antMatchers("/searches/supplier", "/searches/supplier/{page}/{pageSize}").hasAuthority("ROLE_SUPPLIER")

                .antMatchers(HttpMethod.POST,"/brands").hasAuthority("ROLE_SUPPLIER")
                .antMatchers(HttpMethod.DELETE,"/brands/{id}").hasAuthority("ROLE_SUPPLIER")
                .antMatchers(HttpMethod.PUT, "/brands/{id}").hasAuthority("ROLE_BRAND")
                .antMatchers(HttpMethod.GET, "/brands/{id}").hasAuthority("ROLE_BRAND")
                .antMatchers("/searches/brand", "/searches/brand/{page}/{pageSize}").hasAuthority("ROLE_SUPPLIER")

                .antMatchers(HttpMethod.POST,"/stores").hasAuthority("ROLE_BRAND")
                .antMatchers(HttpMethod.DELETE,"/stores/{id}").hasAuthority("ROLE_BRAND")
                .antMatchers(HttpMethod.PUT, "/stores/{id}").hasAuthority("ROLE_BRAND")
                .antMatchers(HttpMethod.GET, "/stores/{id}").hasAuthority("ROLE_BRAND")
                .antMatchers("/searches/store", "/searches/store/{page}/{pageSize}").hasAuthority("ROLE_BRAND")

                .antMatchers(HttpMethod.POST,"/category").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/category/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/category/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/category/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/category/search", "/category/search/{page}/{pageSize}").authenticated()

                .antMatchers(HttpMethod.POST,"/goods").hasAuthority("ROLE_BRAND")
                .antMatchers(HttpMethod.DELETE,"/goods/{id}").hasAuthority("ROLE_BRAND")
                .antMatchers(HttpMethod.PUT, "/goods/{id}").hasAuthority("ROLE_BRAND")
                .antMatchers(HttpMethod.GET, "/goods/{id}").hasAuthority("ROLE_BRAND")
                .antMatchers(HttpMethod.GET, "/goods/search", "/goods/search/{page}/{pageSize}").hasAnyAuthority("ROLE_BRAND")
                .antMatchers(HttpMethod.GET, "/goods/search/ignore-permissions/{page}/{pageSize}").authenticated()

                .antMatchers(HttpMethod.PATCH, "/supplier-contracts/{id}").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST,"/supplier-contracts").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/supplier-contracts/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/supplier-contracts/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/supplier-contracts/{id}").hasAnyAuthority("ROLE_SUPPLIER")
                .antMatchers(HttpMethod.GET, "/supplier-contracts/search", "/supplier-contracts/search/{page}/{pageSize}").hasAnyAuthority("ROLE_SUPPLIER")

                .antMatchers(HttpMethod.PATCH, "/customer-contracts/{id}").hasAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST,"/customer-contracts").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/customer-contracts/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/customer-contracts/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/customer-contracts/{id}").hasAnyAuthority("ROLE_CUSTOMER")
                .antMatchers(HttpMethod.GET, "/customer-contracts/search", "/customer-contracts/search/{page}/{pageSize}").hasAnyAuthority("ROLE_CUSTOMER")

                .antMatchers(HttpMethod.GET, "/campaigns/search").hasAnyAuthority("ROLE_OPERATOR","ROLE_CUSTOMER", "ROLE_SUPPLIER")
                .antMatchers(HttpMethod.PATCH, "/campaigns/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.POST,"/campaigns").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/campaigns/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/campaigns/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/campaigns/{id}").hasAnyAuthority("ROLE_OPERATOR","ROLE_CUSTOMER", "ROLE_SUPPLIER")
                .antMatchers(HttpMethod.GET, "/campaigns/search/{page}/{pageSize}").hasAnyAuthority("ROLE_OPERATOR","ROLE_CUSTOMER", "ROLE_SUPPLIER")

                .antMatchers(HttpMethod.GET, "/message_template/get-all").hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")

                .antMatchers(HttpMethod.GET, "/publishes/search", "/publishes/search/{page}/{pageSize}").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_SUPPLIER")
                .antMatchers(HttpMethod.PATCH, "/publishes/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.POST,"/publishes").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/publishes/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/publishes/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/publishes/{id}").hasAnyAuthority("ROLE_CUSTOMER", "ROLE_SUPPLIER")

                .antMatchers(HttpMethod.POST,"/admins").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/admins/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/admins/change-password").authenticated()
                .antMatchers(HttpMethod.PUT, "/admins/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/admins").authenticated()
                .antMatchers(HttpMethod.GET, "/admins/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/admins/search", "/admins/search/{{page}}/{{pageSize}}").hasAuthority("ROLE_BRAND")

                .antMatchers(HttpMethod.POST,"/roles").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/roles/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/roles/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/roles/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/roles/search", "/roles/search/{{page}}/{{pageSize}}").authenticated()

                .antMatchers(HttpMethod.POST,"/codes").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/codes/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/codes/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/codes/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/codes/search", "/codes/search/{{page}}/{{pageSize}}").authenticated()

                .antMatchers(HttpMethod.POST,"/code-groups").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/code-groups/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/code-groups/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/code-groups/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/code-groups/search", "/code-groups/search/{{page}}/{{pageSize}}").authenticated()

                .antMatchers(HttpMethod.POST,"/menus").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/menus/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/menus/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/menus/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/menus/search", "/menus/search/{{page}}/{{pageSize}}").authenticated()

                .antMatchers(HttpMethod.POST,"/menu-groups").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.DELETE,"/menu-groups/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.PUT, "/menu-groups/{id}").hasAuthority("ROLE_OPERATOR")
                .antMatchers(HttpMethod.GET, "/menu-groups/{id}").authenticated()
                .antMatchers(HttpMethod.GET, "/menu-groups/search", "/menu-groups/search/{{page}}/{{pageSize}}").authenticated()

                .antMatchers(HttpMethod.GET,"/settlements/search/{{page}}/{{pageSize}}").hasAnyAuthority("ROLE_BRAND", "ROLE_CUSTOMER", "ROLE_SUPPLIER")
                .antMatchers(HttpMethod.GET,"/settlements/all-data").hasAnyAuthority("ROLE_BRAND", "ROLE_CUSTOMER", "ROLE_SUPPLIER")

                .antMatchers(HttpMethod.POST,"/external-pin-upload").hasAnyAuthority("ROLE_OPERATOR", "ROLE_SUPPLIER")
                .antMatchers(HttpMethod.GET,"/external-pin-upload/{id}").hasAnyAuthority("ROLE_OPERATOR", "ROLE_SUPPLIER")
                .antMatchers(HttpMethod.GET, "/external-pin-upload/search/{page}/{pageSize}").hasAnyAuthority("ROLE_OPERATOR", "ROLE_SUPPLIER")


                .antMatchers(HttpMethod.GET, "/dashboard/item-table/{offset}/{pageSize}").hasAnyAuthority("ROLE_BRAND", "ROLE_SUPPLIER")
                .antMatchers(HttpMethod.GET, "/dashboard/store-table/{offset}/{pageSize}").hasAnyAuthority("ROLE_BRAND", "ROLE_SUPPLIER")
                .antMatchers(HttpMethod.GET, "/dashboard/brand-table/{offset}/{pageSize}").hasAnyAuthority("ROLE_SUPPLIER")
                .antMatchers(HttpMethod.GET, "/dashboard/goods-chart").hasAnyAuthority("ROLE_SUPPLIER", "ROLE_BRAND", "ROLE_STORE")
                .antMatchers(HttpMethod.GET, "/dashboard/campaign-table/{offset}/{pageSize}").hasAnyAuthority("ROLE_CUSTOMER")
                .antMatchers(HttpMethod.GET, "/dashboard/delivery-table/{offset}/{pageSize}").hasAnyAuthority("ROLE_CUSTOMER")
                .antMatchers(HttpMethod.GET, "/dashboard/**").hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")


                .antMatchers(HttpMethod.GET, "/cs-management/*"/*, "/cs-management/search/"*/).hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")

                .antMatchers(HttpMethod.GET, "/vnpt-epay/*"/*, "/cs-management/search/"*/).hasAnyAuthority("ROLE_OPERATOR", "ROLE_ADMIN")

                .antMatchers("/partner/*").hasAuthority("ROLE_CUSTOMER")
                .antMatchers("/operator-request*").hasAnyAuthority("ROLE_ADMIN")

                .anyRequest().authenticated()
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(loggedInUserContextFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());
        http.csrf().disable()
                .cors().configurationSource(request -> getCorsConfiguration());

    }


    private CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(List.of("*"));
        return corsConfiguration;
    }

}
