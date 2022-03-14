package com.company.topaloq.config.jwt;

import com.company.topaloq.exceptions.ForbiddenException;
import com.company.topaloq.exceptions.UnauthorizedException;
import com.company.topaloq.entity.enums.UserRole;
import io.jsonwebtoken.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public class JwtUtil {
    private final static String key = "SearchingKey";

    public static String generateJwt(Long id, UserRole role){
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + 1000 * 60 * 60);

        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(String.valueOf(id))
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, key)
                .setIssuer("Topaloq")
                .claim("role", role);

        return jwtBuilder.compact();
    }

    public static UserJwtDTO parseJwt(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody();

        Long id = Long.valueOf(claims.getSubject());
        UserRole role = UserRole.valueOf((String) claims.get("role"));

        return new UserJwtDTO(id, role);
    }

    public static UserJwtDTO getCurrentUser(HttpServletRequest request){

        UserJwtDTO jwtDTO = (UserJwtDTO) request.getAttribute("jwtDto");

        if (jwtDTO == null)
            throw new UnauthorizedException("Not Authorized (JwtDto is Null)");

        return jwtDTO;
    }

    public static UserJwtDTO getCurrentUser(HttpServletRequest request, UserRole... roles){

        UserJwtDTO jwtDTO = (UserJwtDTO) request.getAttribute("jwtDto");

        if (jwtDTO == null)
            throw new UnauthorizedException("Not Authorized (JwtDto is Null)");

        if (!List.of(roles).contains(jwtDTO.getRole())){
            throw new ForbiddenException("This is Forbidden for you, allowed to " + roles);
        }

        return jwtDTO;
    }

}
