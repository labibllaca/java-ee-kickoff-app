package org.example.kickoff.config.auth;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.auth.message.AuthException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.AutoApplySession;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.authentication.mechanism.http.RememberMe;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Authentication mechanism that authenticates according to the Servlet spec defined FORM
 * authentication mechanism. See Servlet spec for further details.
 *
 * @author Arjan Tijms
 */
@AutoApplySession // For "Is user already logged-in?"
@RememberMe(cookieMaxAgeSeconds = 60 * 60 * 24 * 14) // 14 days
@LoginToContinue(loginPage = "/login?continue=true", errorPage = "", useForwardToLogin = false)
@ApplicationScoped
public class KickoffFormAuthenticationMechanism implements HttpAuthenticationMechanism {

    @Inject
    private IdentityStore identityStore;

	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext context) throws AuthException {
		Credential credential = context.getAuthParameters().getCredential();

		if (credential != null) {
            return context.notifyContainerAboutLogin(identityStore.validate(credential));
        }
		else {
			return context.doNothing();
		}
	}

}