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

import org.codehaus.annogen.override.internal.jam.JamElementIdPoolImpl;
import org.codehaus.jam.JAnnotatedElement;
import org.codehaus.jam.internal.JamLoggerImpl;
import org.codehaus.jam.provider.JamLogger;

/**
 * Creates ElementIds using
 * JAM.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface JamElementIdPool {

  // ========================================================================
  // Factory

  /**
   * Static factory for JamElementIdPools.
   */
  public static class Factory {
    public static JamElementIdPool create(JamLogger logger) {
      return new JamElementIdPoolImpl(logger);
    }

    public static JamElementIdPool create() {
      return new JamElementIdPoolImpl(new JamLoggerImpl());
    }
  }

  // ========================================================================
  // JAM-based public methods

  public ElementId getIdFor(JAnnotatedElement jamElement);

}
