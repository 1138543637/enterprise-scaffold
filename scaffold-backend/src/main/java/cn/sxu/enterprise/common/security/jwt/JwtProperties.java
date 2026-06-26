package cn.sxu.enterprise.common.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "enterprise.security.jwt")
public class JwtProperties {

    private String issuer;

    private String secret;

    private Long expireMinutes;

    public String getIssuer() {
        return issuer;
    }

    public String getSecret() {
        return secret;
    }

    public Long getExpireMinutes() {
        return expireMinutes;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setExpireMinutes(Long expireMinutes) {
        this.expireMinutes = expireMinutes;
    }
}