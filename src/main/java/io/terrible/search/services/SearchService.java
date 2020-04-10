/* Licensed under Apache-2.0 */
package io.terrible.search.services;

import io.terrible.search.domain.IndexObject;
import java.io.IOException;
import java.util.ArrayList;
import reactor.core.publisher.Mono;

/** @author Chris Turner (chris@forloop.space) */
public interface SearchService {

  Mono<Void> createIndex(String index) throws IOException;

  Mono<Void> index(String index, String id, String json);

  Mono<Void> indexSingle(String index, String id, String json) throws IOException;

  Mono<Void> flush();

  ArrayList<IndexObject> search(String index, String query) throws IOException;
}
