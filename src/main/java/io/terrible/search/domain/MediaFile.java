/* Licensed under Apache-2.0 */
package io.terrible.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

/** @author Chris Turner (chris@forloop.space) */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MediaFile {

  private String id;

  private String name;

  private String absolutePath;

  private String mimeType;

  private long size;

  private long lastAccessTime;

  private long lastModifiedTime;

  private long importedTime;

  private ArrayList<String> thumbnails;
}
