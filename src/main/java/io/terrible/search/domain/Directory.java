/* Licensed under Apache-2.0 */
package io.terrible.search.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author Chris Turner (chris@forloop.space) */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Directory {

  private String id;

  private String path;
}
