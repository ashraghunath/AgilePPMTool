package io.ashwin.ppmtool.security;

import io.ashwin.ppmtool.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.ashwin.ppmtool.security.SecurityConstants.EXPIRATION;
import static io.ashwin.ppmtool.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    //generate token
    public String generateToken(Authentication authentication){

        User user = (User)authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());

        Date expiryDate = new Date(now.getTime()+EXPIRATION);

        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>();
        claims.put("id",Long.toString(user.getId()));
        claims.put("username",user.getUsername());
        claims.put("fullName",user.getFullName());

        return Jwts.builder()
                .setSubject(userId)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,SECRET)
                .compact();

    }

    //validate token
    public boolean validateToken(String token)
    {
        try
        {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException s)
        {
            System.out.println("Invalid JWT signature");
        }
        catch (MalformedJwtException m)
        {
            System.out.println("Invalid JWT token");
        }
        catch (ExpiredJwtException m)
        {
            System.out.println("Expired JWT token");
        }
        catch (UnsupportedJwtException m)
        {
            System.out.println("Unsupported JWT token");
        }
        catch (IllegalArgumentException m)
        {
            System.out.println("JWT claims empty");
        }
        return false;
    }

    //get User id from token
    public Long getUserIdFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        String id = (String)claims.get("id");
        return Long.parseLong(id);
    }
}
