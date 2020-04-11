/* Licensed under Apache-2.0 */
package io.terrible.search.services;

import io.terrible.search.domain.IndexObject;
import io.terrible.search.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.common.Strings.isNullOrEmpty;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

  public static final String SETTINGS_JSON = "es_settings.json";

  private final RestHighLevelClient restHighLevelClient;

  private final BulkProcessor bulkProcessor;

  @Override
  public Mono<Void> createIndex(final String index) throws IOException {

    if (isExistingIndex(index)) {
      return Mono.empty();
    }

    final Resource resource = new ClassPathResource(SETTINGS_JSON);
    final String indexSettings =
        FileUtils.readFileToString(resource.getFile(), StandardCharsets.UTF_8);
    final CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);

    if (!isNullOrEmpty(indexSettings)) {
      createIndexRequest.source(indexSettings, XContentType.JSON);
    }

    try {
      restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
    } catch (final ElasticsearchStatusException e) {
      log.error("Unable to create index {} {}", e.getMessage(), e);
    }

    restHighLevelClient
        .cluster()
        .health(new ClusterHealthRequest(index).waitForYellowStatus(), RequestOptions.DEFAULT);

    return Mono.empty();
  }

  @Override
  public Mono<Void> flush() {

    bulkProcessor.flush();

    return Mono.empty();
  }

  @Override
  public Mono<Void> index(final String index, final String id, final String json) {

    bulkProcessor.add(new IndexRequest(index).id(id).source(json, XContentType.JSON));

    return Mono.empty();
  }

  @Override
  public Flux<IndexObject> search(final String index, final String query) throws IOException {

    log.info("Query {}", query);

    if (!isExistingIndex(index)) {
      log.info("{} does not exist, skipping search", index);
      return Flux.empty();
    }

    final SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.query(QueryBuilders.wildcardQuery("absolutePath", "*" + query + "*"));
    sourceBuilder.from(0);
    sourceBuilder.size(500);
    sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

    final SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices(index);
    searchRequest.source(sourceBuilder);

    final SearchResponse searchResponse =
        restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

    final ArrayList<IndexObject> results = new ArrayList<>();
    searchResponse
        .getHits()
        .forEach(searchHit -> results.add(JsonUtil.convertSourceMap(searchHit)));

    log.info("Count: {} Hits: {}", results.size(), results);

    return Flux.fromIterable(results);
  }

  @Override
  public Mono<Void> deleteIndex(final String index) {

    restHighLevelClient
        .indices()
        .deleteAsync(new DeleteIndexRequest(index), RequestOptions.DEFAULT, null);

    return Mono.empty();
  }

  private boolean isExistingIndex(final String index) throws IOException {

    return restHighLevelClient.indices().exists(new GetIndexRequest(index), RequestOptions.DEFAULT);
  }
}
