/* Licensed under Apache-2.0 */
package io.terrible.search.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.terrible.search.domain.IndexObject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHit;

@Slf4j
@UtilityClass
public class JsonUtil {

  private final ObjectMapper objectMapper = new ObjectMapper();

  public static String toJson(final IndexObject indexObject) {

    try {
      return objectMapper.writeValueAsString(indexObject);
    } catch (final JsonProcessingException e) {
      log.error("Unable to parse to json {}", e.getMessage(), e);

      throw new RuntimeException(e.getMessage());
    }
  }

  public static IndexObject convertSourceMap(final SearchHit searchHit) {

    final IndexObject indexObject =
        objectMapper.convertValue(searchHit.getSourceAsMap(), IndexObject.class);
    indexObject.setId(searchHit.getId());

    return indexObject;
  }
}
