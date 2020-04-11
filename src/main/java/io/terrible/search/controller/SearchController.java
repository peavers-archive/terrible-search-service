/* Licensed under Apache-2.0 */
package io.terrible.search.controller;

import io.terrible.search.domain.IndexObject;
import io.terrible.search.services.SearchService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

  private final SearchService searchService;

  @GetMapping
  public Flux<IndexObject> search(
      @RequestParam final String index, @RequestParam final String query) throws IOException {

    return searchService.search(index, query);
  }
}
