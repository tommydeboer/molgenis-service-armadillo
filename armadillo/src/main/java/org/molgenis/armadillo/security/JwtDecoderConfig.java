package org.molgenis.armadillo.security;

import static org.springframework.security.oauth2.jwt.JwtClaimNames.AUD;

import java.util.Collection;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@ConditionalOnProperty(
    prefix = "spring.security.oauth2.resourceserver",
    value = {"jwt.issuer-uri", "opaquetoken.client-id"})
@Configuration
public class JwtDecoderConfig {

  @Bean
  public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
    String issuerUri = properties.getJwt().getIssuerUri();
    NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerUri);

    var audienceValidator =
        new JwtClaimValidator<Collection<String>>(
            AUD, aud -> aud != null && aud.contains(properties.getOpaquetoken().getClientId()));
    OAuth2TokenValidator<Jwt> jwtValidator =
        new DelegatingOAuth2TokenValidator<>(
            JwtValidators.createDefaultWithIssuer(issuerUri), audienceValidator);

    jwtDecoder.setJwtValidator(jwtValidator);
    return jwtDecoder;
  }
}
