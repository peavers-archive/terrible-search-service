package io.terrible.search.services;

import io.terrible.search.domain.MediaFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static io.terrible.search.utils.JsonUtil.toJson;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataServiceImpl implements DataService {

    private final WebClient webClient = WebClient.create("http://localhost:8081");

    private final SearchService searchService;

    @Override
    public Flux<Void> get(final String index, final String path) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path).build())
                .retrieve()
                .bodyToFlux(MediaFile.class)
                .flatMap(value -> searchService.index(index, value.getId(), toJson(value)))
                .doOnComplete(searchService::flush);
    }

}
