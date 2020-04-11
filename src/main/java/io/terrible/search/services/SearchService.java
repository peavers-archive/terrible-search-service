/* Licensed under Apache-2.0 */
package io.terrible.search.services;

import io.terrible.search.domain.IndexObject;
import java.io.IOException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SearchService {

  Mono<Void> createIndex(String index) throws IOException;

  Mono<Void> index(String index, String id, String json);

  Mono<Void> flush();

  Flux<IndexObject> search(String index, String query) throws IOException;

  Mono<Void> deleteIndex(String index);
}
