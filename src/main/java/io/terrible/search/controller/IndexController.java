/* Licensed under Apache-2.0 */
package io.terrible.search.controller;

import io.terrible.search.services.DataService;
import io.terrible.search.services.SearchService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/index")
@RequiredArgsConstructor
public class IndexController {

  private final SearchService searchService;

  private final DataService dataService;

  @PostMapping
  public Mono<Void> create(@RequestParam final String name) throws IOException {

    return searchService.createIndex(name);
  }

  @DeleteMapping
  public Mono<Void> delete(@RequestParam final String name) {

    return searchService.deleteIndex(name);
  }

  @PostMapping("/populate")
  public Flux<Void> populate(@RequestParam final String name) {

    return dataService.get(name, "media-files");
  }
}
