package cnsa.mergedate.common.security;

import cnsa.mergedate.domain.member.entity.Member;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@Builder
public record PrincipalDetails(
    String nickname,
    String password,
    String memberId
) implements UserDetails {

    public static PrincipalDetails of(
        String nickname, String memberId
    ) {
        return new PrincipalDetails(nickname, "", memberId);
    }

    public static PrincipalDetails of(Member member) {
        return new PrincipalDetails(
            member.getNickname(),
            member.getPassword(),
            member.getId()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: 역할 관련 협의 필요
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));    //TODO : Enum 처리
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
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