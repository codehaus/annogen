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



/**
 * <p>
 * Modifes the annotations which apply to some annotated java element.
 * AnnoOverrider is essentially a callback mechanism - it is consulted when
 * an AnnnoViewer receives requests to view the annotations on a particular
 * element.  This model was chosen in the interests of efficiency, since
 * the overrider doesn't have to worry about whether it's wasting time
 * overriding annotations that will never be used anyway.
 * </p>
 *
 * <p>
 * However, the callback approach can be ungainly, in the information
 * you need to override annotations may not be readily available to you
 * when it comes time to view the annotations.  To help with this problem,
 * see the StoredAnnoOverrider, which lets you determine your annotation
 * overrides in a single pass and store them for later use in the View phase.
 * </p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface AnnoOverrider {

  // ========================================================================
  // Public methods

  /**
   * <p>Provides the Populator with a context object to access various services
   * such as logging.  For a given instance, this method is guranteed to be
   * called exactly once and before any other methods in this interface.</p>
   */
  public void init(AnnoContext pc);

  /**
   * <p>Called to give this Overrider a chance to modify the annotations which
   * apply to a given element.</p>
   *
   * @param id  Element to which the annotations apply.
   * @param currentAnnos Currently viewed annotations.
   */

  public void modifyAnnos(ElementId id, AnnoBeanSet currentAnnos);
}
