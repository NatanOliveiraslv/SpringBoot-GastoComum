package com.br.gasto_comum.services;

import com.br.gasto_comum.enums.Provider;
import com.br.gasto_comum.models.User;
import com.br.gasto_comum.repositorys.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        String firstName = oidcUser.getAttribute("given_name");
        String lastName = oidcUser.getAttribute("family_name");
        String providerId = oidcUser.getSubject();

        String nameProvider = userRequest.getClientRegistration().getRegistrationId();
        Provider provider = Provider.valueOf(nameProvider.toUpperCase());

        User user = userRepository.findByUsername(email)
                .orElseGet(() -> new User(email, email, firstName, lastName, provider, providerId));

        userRepository.save(user);

        return new CustomOAuth2User(oidcUser, user);
    }
}
