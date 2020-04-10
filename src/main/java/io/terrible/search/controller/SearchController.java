/* Licensed under Apache-2.0 */

package io.terrible.search.controller;

import io.terrible.search.domain.MediaFile;
import io.terrible.search.services.DataService;
import io.terrible.search.services.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static io.terrible.search.utils.JsonUtil.toJson;

/**
 * @author Chris Turner (chris@forloop.space)
 */
@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    private final DataService dataService;

    @PostMapping("/search/create-index")
    public Mono<Void> createIndex(@RequestParam final String index) throws IOException {

        return searchService.createIndex(index);
    }

    @PostMapping("/search/index-single")
    public Mono<Void> indexSingle(@RequestParam final String index, @RequestBody final MediaFile mediaFile) {

        return searchService.index(index, mediaFile.getId(), toJson(mediaFile));
    }

    @PostMapping("/search/index")
    public Flux<Void> index(@RequestParam final String index) {

        return dataService.get(index, "media-files");
    }

    @GetMapping("/search")
    public Flux<MediaFile> search(@RequestParam final String index, @RequestParam final String query)
            throws IOException {

        return Flux.fromIterable(searchService.search(index, query));
    }

}
