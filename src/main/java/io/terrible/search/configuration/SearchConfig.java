/* Licensed under Apache-2.0 */
package io.terrible.search.configuration;

import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author Chris Turner (chris@forloop.space) */
@Slf4j
@Configuration
public class SearchConfig {

  public static final String HOSTNAME = "localhost";

  public static final int PORT = 9200;

  public static final String SCHEME = "http";

  @Bean
  public RestHighLevelClient client() {

    return new RestHighLevelClient(RestClient.builder(new HttpHost(HOSTNAME, PORT, SCHEME)));
  }

  @Bean
  public BulkProcessor bulkProcessor() {

    final BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer =
        (request, bulkListener) ->
            client().bulkAsync(request, RequestOptions.DEFAULT, bulkListener);

    return BulkProcessor.builder(bulkConsumer, new DebugListener()).build();
  }

  static class DebugListener implements BulkProcessor.Listener {

    public void beforeBulk(final long executionId, final BulkRequest request) {

      log.info("Sending a bulk request of [{}] requests", request.numberOfActions());
    }

    public void afterBulk(
        final long executionId, final BulkRequest request, final BulkResponse response) {

      log.info("Executed bulk request with [{}] requests", request.numberOfActions());
      if (response.hasFailures()) {
        final int[] failures = {0};
        response
            .iterator()
            .forEachRemaining(
                bulkItemResponse -> {
                  if (bulkItemResponse.isFailed()) {
                    failures[0]++;
                    log.debug(
                        "Error caught for [{}]/[{}]/[{}]: {}",
                        bulkItemResponse.getIndex(),
                        bulkItemResponse.getType(),
                        bulkItemResponse.getId(),
                        bulkItemResponse.getFailureMessage());
                  }
                });
        log.warn("Got [{}] failures of [{}] requests", failures[0], request.numberOfActions());
      }
    }

    public void afterBulk(
        final long executionId, final BulkRequest request, final Throwable failure) {

      log.info("Got a hard failure when executing the bulk request", failure);
    }
  }
}
