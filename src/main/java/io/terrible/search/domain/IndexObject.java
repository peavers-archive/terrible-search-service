/* Licensed under Apache-2.0 */
package io.terrible.search.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexObject {

  private String id;

  private String name;

  private String absolutePath;

  private String mimeType;

  private long size;

  private long lastAccessTime;

  private long lastModifiedTime;

  private long importedTime;
}
