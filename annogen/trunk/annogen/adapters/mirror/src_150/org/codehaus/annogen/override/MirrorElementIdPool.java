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

import com.sun.mirror.declaration.Declaration;
import org.codehaus.jam.provider.JamLogger;

/**
 * Creates ElementIds using the Mirror API.  To maximize
 * efficiency you should try to share instances of IdPools as much as possible.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface MirrorElementIdPool {

  // ========================================================================
  // Factory

  /**
   * Static factory for MirrorElementIdPools.
   */
  public static class Factory {
    public static MirrorElementIdPool create(JamLogger logger) {
      throw new IllegalStateException("NYI");
    }

    public static MirrorElementIdPool create() {
      throw new IllegalStateException("NYI");
    }
  }


  // ========================================================================
  // Public methods

  public ElementId getIdFor(Declaration mirrorElement);

}
