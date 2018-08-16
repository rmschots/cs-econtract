package be.cipalschaubroeck.econtract.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * SystemAuthentication.
 *
 * @Author: Frédéric Henry
 */
class SystemAuthentication extends UsernamePasswordAuthenticationToken {

    private static SystemAuthentication INSTANCE;

    public static SystemAuthentication getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SystemAuthentication();
        }
        return INSTANCE;
    }

    private SystemAuthentication() {
        super("system", "system");
    }
}
