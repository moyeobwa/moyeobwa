package momo.app.auth.dto;

import java.util.ArrayList;
import java.util.Collection;
import momo.app.user.domain.Role;
import momo.app.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUser implements UserDetails {

    private final Long id;
    private final String email;
    private final Role role;

    public static AuthUser createAuthUser(User user) {
        return new AuthUser(user);
    }

    private AuthUser(User user) {
        email = user.getEmail();
        role = user.getRole();
        id = user.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(() -> role.getKey());
        return collection;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
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

    public Long getId() {
        return id;
    }

    public Role getRole() { return role; }
}
