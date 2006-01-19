/*   Copyright 2004 The Apache Software Foundation
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.codehaus.annogen.override;

import org.codehaus.annogen.override.internal.StoredAnnoOverriderImpl;

/**
 * <p>
 * Convenience implementation of AnnoOverrider to which you simply add
 * annotations that should apply to given elements.  Note that this is
 * a fairly blunt instrunmennt - the annotations you specify are simply
 * and unconditionally superimposed onto each element.
 * </p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface StoredAnnoOverrider extends AnnoOverrider {

  // ========================================================================
  // Factory

  /**
   * Static factory for StoredAnnoOverriders.
   */
  public static class Factory {

    public static StoredAnnoOverrider create() {
      return new StoredAnnoOverriderImpl();
    }

  }

  // ========================================================================
  // Public methods

  /**
   * <p>
   * Returns an AnnoBeanSet from this StoredAnnoOverrider which applies to
   * the given ElementId.  This will always create a new AnnoBeanSet unless
   * findOrCreate... has been called previously for the given id on
   * this instance of StoredAnnoOverrider.
   * </p>
   *
   * <p>
   * <b>Important:</b> The AnnoBeanSet returned by this method will NOT
   * contain any indigenous annotation information from the given element.
   * The AnnoBeanSets returned here are ALWAYS EMPTY - it's up to you to
   * fill them in.
   * </p>
   *
   * <p>
   * If you need to be able to dynamically read indigenous values before
   * making changes, then you must implement AnnoViewer directly yourself -
   * StoredAnnoViewer is a simple extension and not designed to handle
   * that sort of thing.
   * </p>
   */
  public AnnoBeanSet findOrCreateStoredAnnoSetFor(ElementId id);
  
}
