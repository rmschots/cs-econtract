package be.cipalschaubroeck.econtract.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * AuthenticationUtil.
 *
 * @Author: Frédéric Henry
 */
public class AuthenticationUtil {

    public static void authenticateAsSystemUser() {
        authenticateAs(SystemAuthentication.getInstance());
    }

    public static void authenticateAs(final Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static Authentication getAuthenticatedUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Optional<String> getLoggedInUser() {
        final Authentication authentication = getAuthenticatedUser();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return Optional.ofNullable(authentication.getName());
        }
        return Optional.empty();
    }

    public static boolean isLoggedInAsSystemUser() {
        return SystemAuthentication.getInstance().equals(getAuthenticatedUser());
    }

}
