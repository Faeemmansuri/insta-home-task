package com.instagram.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.instagram.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils implements Serializable {

	private static final long serialVersionUID = -8523733591476115285L;
	
    private static final int JWT_TOKEN_EXPIRATION_PERIOD = 5 * 60 * 60 * 1000;
    

    @Value("${jsonwebtoken.secretKey}")
    private String jwtSecret;

    @Value("${jsonwebtoken.issuer}")
    private String issuer;
    
    private String generateToken(Map<String, Object> claims, String subject) {
        Date currentDate = new Date(System.currentTimeMillis());
        
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(currentDate)
            .setExpiration(DateUtils.addSeconds(currentDate, JWT_TOKEN_EXPIRATION_PERIOD))
            .setIssuer(issuer)
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
    }

    public <T> T getClaimFromToken(String jwtToken, Function<Claims, T> claimsResolver) {
        Claims claims = getClaimsFromJwt(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims getClaimsFromJwt(String jwtToken) {
        return Jwts.parser().setSigningKey(jwtSecret)
            .parseClaimsJws(jwtToken).getBody();
    }

    public Date getExpirationOfJwt(String jwtToken) {
        return getClaimFromToken(jwtToken, Claims::getExpiration);
    }

    public String getUsernameFromToken(String jwtToken) {
        return getClaimFromToken(jwtToken, Claims::getSubject);
    }

    private Boolean isExpired(String jwtToken) {
        return getExpirationOfJwt(jwtToken).before(new Date());
    }

    public String generateJwt(UserEntity user) {
    	Map<String, Object> claims = new HashMap() {{
    		put("userId", user.getId());
    	}};
//    			Map.of("userId", user.getId());
    	String subject = user.getEmail();
        return generateToken(claims, subject);
    }
    
    public Boolean validateJwt(String jwtToken, UserEntity user) {
        return StringUtils.equals(getUsernameFromToken(jwtToken), user.getEmail()) 
            && BooleanUtils.isFalse(isExpired(jwtToken));
    }

}
