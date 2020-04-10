package io.terrible.search.services;

import reactor.core.publisher.Flux;

public interface DataService {

    Flux<Void> get(String index, String path);

}
