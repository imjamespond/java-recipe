package com.sd.web;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class MyUserDetailsService implements UserDetailsService {

    public PasswordEncoder passwordEncoder = new PasswordEncoder() {
        // remember me要用密码encode token， BCryptPasswordEncoder每次encode的结果不同导致token不一致
        @Override
        public String encode(CharSequence charSequence) {
            return charSequence.toString();
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            return charSequence.toString().equals(s);
        }
    };

//    @Autowired
//    private UserDao userDao;

    @Value("${args.admin-pwd:1212}")
    public String adminPwd;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 超级管理员后台
        if ("root".equals(username)) {
            return new MyUser(
                    "root",0l,
                    username,
                    passwordEncoder.encode(DigestUtils.md5Hex(adminPwd)),
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_NORMAL,ROLE_MEDIUM"));
        }




        throw new UsernameNotFoundException("Invalid username or password.");
    }


    public class MyUser extends User {

        public long userId;
        public String type;

        public MyUser(String type, long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
            this.type = type;
            this.userId = userId;
        }

        public MyUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        }

        public MyUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
        }
    }
}