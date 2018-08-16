package be.cipalschaubroeck.econtract.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor which will fetch the Keycloak realm from the JWT Token.
 */
public class RealmInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String K_NAME = KeycloakSecurityContext.class.getName();

    @Override
    public boolean preHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o) {
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o, final ModelAndView modelAndView) {
        RefreshableKeycloakSecurityContext context = (RefreshableKeycloakSecurityContext) httpServletRequest.getAttribute(K_NAME);
        if (null != context) {
            AccessToken token = context.getToken();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("------------------------------------------------------------------------------------------");
                LOGGER.debug(String.format("- URL: %s", httpServletRequest.getRequestURL().toString()));
                LOGGER.debug(String.format("- REALM NAME: %s", token.getOtherClaims().get("realm_name")));
                LOGGER.debug(String.format("- REALM UUID: %s", token.getIssuer()));
                LOGGER.debug(String.format("- SECURITY CONTEXT: %s", SecurityContextHolder.getContext().getAuthentication().toString()));
                LOGGER.debug("------------------------------------------------------------------------------------------");
            }
        }
    }

    @Override
    public void afterCompletion(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o, final Exception e) {
        //
    }

}
