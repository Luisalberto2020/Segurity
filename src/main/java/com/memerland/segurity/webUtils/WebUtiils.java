package com.memerland.segurity.webUtils;

import com.memerland.segurity.Errors.TokenException;
import com.memerland.segurity.daos.UserDao;
import com.memerland.segurity.model.User;
import com.memerland.segurity.utils.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;


import java.util.List;
import java.util.Optional;

public class WebUtiils {
    //FIXME arreglar validaciondel token
    public static Optional<Token> verifyToken(HttpServletRequest req) throws TokenException {
        List<Cookie> cookies = List.of(req.getCookies());
        Optional<Cookie> tokenCookie = cookies.stream().filter(cookie -> cookie.getName().equals("token")).findFirst();
        Token token = null;
        if (tokenCookie.isPresent()) {

                 token = Token.getToken(tokenCookie.get().getValue());
                UserDao userDao = new UserDao();
                Optional<User> user = userDao.findByName(token.getName());
        }
        return Optional.ofNullable(token);
    }
}
