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

import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.annogen.override.AnnoContext;
import org.codehaus.annogen.override.ElementId;
import org.codehaus.annogen.override.StoredAnnoOverrider;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class which lets you create annotations that will be
 * used on specific elements.
 *
 * Note that this is a fairly blunt instrument - an annotation
 * added here will completely override that annotation in
 * the original code, if it appears there.
 *
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class StoredAnnoOverriderImpl implements StoredAnnoOverrider {

  // ========================================================================
  // Variables

  private Map mId2APS = new HashMap();
  private AnnoContext mContext;

  // ========================================================================
  // Constructors


  public StoredAnnoOverriderImpl(AnnoContext ctx) {
    if (ctx == null) throw new IllegalArgumentException("null ctx");
    mContext = ctx;
  }

  public StoredAnnoOverriderImpl() {
    mContext = AnnoContext.Factory.newInstance();
  }

  // ========================================================================
  // StoredAnnoOverrider implementation

  public AnnoBeanSet findOrCreateStoredAnnoSetFor(ElementId id) {
    AnnoBeanSet out = (AnnoBeanSet)mId2APS.get(id);
    if (out == null) {
      out = new AnnoBeanSetImpl(mContext);
      mId2APS.put(id,out);
    }
    return  out;
  }

  // ========================================================================
  // AnnoOverrider implementation

  public void init(AnnoContext ctx) { mContext = ctx; }

  public void modifyAnnos(ElementId id, AnnoBeanSet currentAnnos) {
    AnnoBeanSet stored = (AnnoBeanSet)mId2APS.get(id);
    if (stored == null) return;
    AnnoBean[] proxies = stored.getAll();
    if (proxies == null || proxies.length == 0) return;
    for(int i=0; i<proxies.length; i++) {
      currentAnnos.put(proxies[i]);
    }
  }
}
