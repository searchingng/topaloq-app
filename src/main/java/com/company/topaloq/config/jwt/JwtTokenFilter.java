package com.company.topaloq.config.jwt;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.persistence.GeneratedValue;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty() || !authHeader.startsWith("Bearer ")){
            response.setStatus(401);
            response.setHeader("Message", "Please, write a Token");
            return;
        }

        try {
            String jwt = authHeader.split(" ")[1];

            UserJwtDTO dto = JwtUtil.parseJwt(jwt);
            request.setAttribute("jwtDto", dto);

        } catch (RuntimeException ex){
            ex.printStackTrace();
            response.setStatus(401);
            return;
        }

        filterChain.doFilter(request, response);

    }
}
