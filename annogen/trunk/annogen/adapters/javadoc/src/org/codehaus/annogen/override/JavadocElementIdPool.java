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

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ProgramElementDoc;
import org.codehaus.annogen.override.internal.javadoc.JavadocElementIdPoolImpl;
import org.codehaus.jam.internal.JamLoggerImpl;
import org.codehaus.jam.provider.JamLogger;

/**
 * Creates ElementIds using the
 * <a href='http://java.sun.com/j2se/javadoc' target='_blank'>Doclet</a>
 * API.
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public interface JavadocElementIdPool {

  // ========================================================================
  // Factory

  /**
   * Static factory for JavadocElementIdPools.
   */
  public static class Factory {
    public static JavadocElementIdPool create(JamLogger logger) {
      return new JavadocElementIdPoolImpl(logger);
    }

    public static JavadocElementIdPool create() {
      return new JavadocElementIdPoolImpl(new JamLoggerImpl());
    }
  }


  // ========================================================================
  // Public methods

  public ElementId getIdFor(ProgramElementDoc ped);

  public ElementId getIdFor(ExecutableMemberDoc emd, int paramNum);

}
