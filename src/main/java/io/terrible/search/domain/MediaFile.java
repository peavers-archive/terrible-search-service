/* Licensed under Apache-2.0 */
package io.terrible.search.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaFile {

  private String id;

  private String name;

  private String path;

  private String extension;

  private long size;

  private ArrayList<String> thumbnails = new ArrayList<>();

  private LocalDateTime createdTime;

  private LocalDateTime lastAccessTime;

  private LocalDateTime lastModifiedTime;

  private LocalDateTime importedTime;
}
