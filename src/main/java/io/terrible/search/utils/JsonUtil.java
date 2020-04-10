/* Licensed under Apache-2.0 */

package io.terrible.search.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.terrible.search.domain.MediaFile;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.SearchHit;

@Slf4j
@UtilityClass
public class JsonUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static String toJson(final MediaFile mediaFile) {

        try {
            return objectMapper.writeValueAsString(mediaFile);
        } catch (final JsonProcessingException e) {
            log.error("Unable to parse to json {}", e.getMessage(), e);

            throw new RuntimeException(e.getMessage());
        }
    }

    public static MediaFile convertSourceMap(final SearchHit searchHit) {

        final MediaFile mediaFile = objectMapper.convertValue(searchHit.getSourceAsMap(), MediaFile.class);
        mediaFile.setId(searchHit.getId());

        return mediaFile;
    }

}
