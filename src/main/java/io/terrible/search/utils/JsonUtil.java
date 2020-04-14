/* Licensed under Apache-2.0 */
package io.terrible.search.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.terrible.search.domain.MediaFile;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonUtil {

  private static ObjectMapper objectMapper;

  @Autowired
  public JsonUtil(final ObjectMapper objectMapper) {

    JsonUtil.objectMapper = objectMapper;
  }

  public static String toJson(final MediaFile mediaFile) {

    try {
      return objectMapper.writeValueAsString(mediaFile);
    } catch (final JsonProcessingException e) {
      log.error("Unable to parse to json {}", e.getMessage(), e);

      throw new RuntimeException(e.getMessage());
    }
  }

  public static MediaFile convertSourceMap(final SearchHit searchHit) {

    final MediaFile mediaFile =
        objectMapper.convertValue(searchHit.getSourceAsMap(), MediaFile.class);
    mediaFile.setId(searchHit.getId());

    return mediaFile;
  }
}
