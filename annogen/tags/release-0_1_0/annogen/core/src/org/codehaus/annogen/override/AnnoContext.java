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

import org.codehaus.annogen.view.internal.AnnoViewerParamsImpl;
import org.codehaus.jam.provider.JamLogger;

/**
 * Provides some context services for overriders.  REVIEW does this actually
 * need to be exposed?  
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface AnnoContext {

  // ========================================================================
  // Factory implementation

  public static class Factory {

    public static AnnoContext newInstance() {
      return new AnnoViewerParamsImpl();
    }
  }

  // ========================================================================
  // Public methods

  /**
   * <p>If the given class is a 175 annotation type, returns the annogen'ed
   * class which acts as a proxy to it.  If the given class already is a
   * annobean class, simply returns it.</p>
   *
   * @throws ClassNotFoundException if the bean class could not be loaded
   */
  Class getAnnobeanClassFor(Class declOrAnnoBeanClass)
    throws ClassNotFoundException;

  /**
   * <p>Returns the 175 annotation class which corresponds to the given
   * annobean class. </p>
   *
   * @throws IllegalArgumentException if the given class is not an annobean class.
   * @throws IllegalStateException if the current vm is pre-1.5.
   * @throws ClassNotFoundException if the 175 type could not be loaded
   */

  Class getJsr175ClassForAnnobeanClass(Class annoBeanClass)
    throws ClassNotFoundException;

  public JamLogger getLogger();

  public ClassLoader getClassLoader();

  public AnnoBean createAnnoBeanFor(Class annoBeanClass);

}
