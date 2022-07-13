package cleanbook.com.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class UserDetailsVO implements UserDetails {

    @Delegate
    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAccountState() == AccountState.ACTIVE;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccountState() == AccountState.ACTIVE;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getAccountState() == AccountState.ACTIVE;
    }

    @Override
    public boolean isEnabled() {
        return user.getAccountState() == AccountState.ACTIVE;
    }
}
