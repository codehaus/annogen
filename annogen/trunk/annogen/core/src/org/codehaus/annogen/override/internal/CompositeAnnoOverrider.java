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
package org.codehaus.annogen.override.internal;

import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.annogen.override.AnnoContext;
import org.codehaus.annogen.override.AnnoOverrider;
import org.codehaus.annogen.override.ElementId;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class CompositeAnnoOverrider implements AnnoOverrider {

  // ========================================================================
  // Variables

  private AnnoOverrider[] mPops;

  // ========================================================================
  // Constructors

  public CompositeAnnoOverrider(AnnoOverrider[] pops) {
    if (pops == null) throw new IllegalArgumentException("null pops");
    mPops = pops;
  }

  // ========================================================================
  // Annoatation populator implementation

  public void init(AnnoContext pc) {
    for(int i=0; i<mPops.length; i++) mPops[i].init(pc);
  }

  public void modifyAnnos(ElementId id, AnnoBeanSet currentAnnos) {
    for(int i=0; i<mPops.length; i++) mPops[i].modifyAnnos(id,currentAnnos);
  }

}
