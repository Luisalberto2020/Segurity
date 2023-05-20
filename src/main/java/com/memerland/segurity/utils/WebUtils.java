package com.memerland.segurity.utils;

import java.util.Optional;

import com.memerland.segurity.Errors.TokenException;

import io.javalin.http.Context;

public class WebUtils {



        public static Optional<Token> checkToken(Context context) {

            if (context.cookie("token") == null) {
                context.redirect("/login");
                return Optional.empty();

            } else {

                try {
                    Token token = Token.getToken(context.cookie("token"));
                    return Optional.of(token);
                } catch (TokenException e) {
                    context.removeCookie("token");
                    context.redirect("/login");
                }
            }
            return Optional.empty();
        }

    }


