package it.polimi.gma.webutils;

import javax.servlet.http.HttpSession;

import it.polimi.gma.entities.Admin;
import it.polimi.gma.entities.Consumer;
import it.polimi.gma.entities.User;
import it.polimi.gma.enums.AuthType;

public final class AuthenticationChecker {
	public static AuthType checkAuth(HttpSession session) {
		if(session.getAttribute("user")!=null) {
			User u = (User) session.getAttribute("user");
			if (u.getClass().equals(Admin.class)){
				return AuthType.ADMIN;
			}
			else if (u.getClass().equals(Consumer.class)) {
				return AuthType.CONSUMER;
			}
		}
		return AuthType.UNAUTHENTICATED;
	}
}
