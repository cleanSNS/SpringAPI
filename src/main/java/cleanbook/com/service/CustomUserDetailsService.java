package cleanbook.com.service;

import cleanbook.com.entity.enums.AccountState;
import cleanbook.com.entity.user.User;
import cleanbook.com.repository.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
   private final UserRepository userRepository;

   public CustomUserDetailsService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String email) {
      return userRepository.findUserByEmail(email)
              .map(user -> createUser(email, user))
              .orElseThrow(() -> new UsernameNotFoundException(email + " -> 데이터베이스에서 찾을 수 없습니다."));
   }

   private org.springframework.security.core.userdetails.User createUser(String username, User user) {
      if (!user.getAccountState().equals(AccountState.ACTIVE)) {
         throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
      }
      List<GrantedAuthority> grantedAuthorities = user.getUserAuthorityList().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().getName()))
              .collect(Collectors.toList());
      return new org.springframework.security.core.userdetails.User(user.getEmail(),
              user.getPassword(),
              grantedAuthorities);
   }
}
