package com.evoucher.adminapi.auth.service.models;


import com.evoucher.adminapi.auth.dao.models.Admin;
import com.evoucher.adminapi.auth.dao.models.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserPrincipal implements UserDetails {

    private String id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> roles;
    private String adminType;
    private String adminCorpId;

    public static UserDetails build(Admin admin, Set<Role> roles) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (!CollectionUtils.isEmpty(roles)) {
            authorities.addAll(
                    roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role.getRoleCode()))
                            .collect(Collectors.toList())
            );
        }
        return UserPrincipal
                .builder()
                .id(admin.getId())
                .username(admin.getMobileNumber())
                .password(admin.getPassword())
                .roles(authorities)
                .adminType(admin.getRoleCode())
                .adminCorpId(admin.getAdminCorporationId())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
