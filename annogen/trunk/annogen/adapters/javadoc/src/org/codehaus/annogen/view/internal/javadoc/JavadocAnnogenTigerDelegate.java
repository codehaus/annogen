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
package org.codehaus.annogen.view.internal.javadoc;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ProgramElementDoc;
import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.jam.internal.TigerDelegate;
import org.codehaus.jam.provider.JamLogger;

/**
 * Provides an interface to 1.5-specific functionality.  The impl of
 * this class is loaded by-name at runtime.
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public abstract class JavadocAnnogenTigerDelegate extends TigerDelegate {

  // ========================================================================
  // Constants

  private static final String IMPL =
    "org.codehaus.annogen.view.internal.JavadocAnnogenTigerDelegateImpl_150";

  // ========================================================================
  // Factory

  public static JavadocAnnogenTigerDelegate create(JamLogger logger) {
    if (!isTigerJavadocAvailable(logger)) return null;
    // ok, if we could load that, let's new up the extractor delegate
    try {
      JavadocAnnogenTigerDelegate out = (JavadocAnnogenTigerDelegate)
        Class.forName(IMPL).newInstance();
      out.init(logger);
      return out;
    } catch (ClassNotFoundException e) {
      issue14BuildWarning(e,logger);
    } catch (IllegalAccessException e) {
      logger.error(e);
    } catch (InstantiationException e) {
      logger.error(e);
    }
    return null;
  }

  // ========================================================================
  // Public methods

  /**
   *
   */
  public abstract boolean extractAnnotations(AnnoBeanSet out,
                                             ProgramElementDoc src);

  /**
   *
   */
  public abstract boolean extractAnnotations(AnnoBeanSet out,
                                             ExecutableMemberDoc method,
                                             int paramNum);
}
