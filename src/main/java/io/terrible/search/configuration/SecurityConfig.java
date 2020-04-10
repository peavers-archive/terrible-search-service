/* Licensed under Apache-2.0 */
package io.terrible.search.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Collections;

/** @author Chris Turner (chris@forloop.space) */
public class SecurityConfig {

  private static final String SECURE_ROUTE = "/*/**";

  private static final String ALLOWED_CORS = "*";

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {

    final CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    configuration.setAllowedOrigins(Collections.singletonList(ALLOWED_CORS));
    configuration.setAllowedMethods(Collections.singletonList(ALLOWED_CORS));
    configuration.setAllowedHeaders(Collections.singletonList(ALLOWED_CORS));

    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration(SECURE_ROUTE, configuration);

    return source;
  }
}
