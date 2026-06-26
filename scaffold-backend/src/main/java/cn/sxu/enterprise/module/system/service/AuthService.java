package cn.sxu.enterprise.module.system.service;

import cn.sxu.enterprise.common.core.exception.BusinessException;
import cn.sxu.enterprise.common.security.jwt.JwtTokenProvider;
import cn.sxu.enterprise.module.system.entity.SysUser;
import cn.sxu.enterprise.module.system.vo.LoginRequest;
import cn.sxu.enterprise.module.system.vo.LoginResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final SysUserService sysUserService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(SysUserService sysUserService,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.sysUserService = sysUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(LoginRequest request) {
        SysUser user = sysUserService.lambdaQuery()
                .eq(SysUser::getUsername, request.getUsername())
                .one();

        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        if (!Integer.valueOf(0).equals(user.getStatus())) {
            throw new BusinessException(403, "账号已被停用");
        }

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!passwordMatches) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        String token = jwtTokenProvider.generateToken(user);

        return new LoginResponse(
                token,
                "Bearer",
                jwtTokenProvider.getExpireSeconds(),
                user.getId(),
                user.getUsername(),
                user.getNickname()
        );
    }
}