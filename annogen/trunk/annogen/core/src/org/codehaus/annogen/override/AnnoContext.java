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

import org.codehaus.annogen.override.internal.AnnoContextImpl;
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

    public static AnnoContext newInstance() { return new AnnoContextImpl(); }
  }

  // ========================================================================
  // Public methods

  public JamLogger getLogger();

  public AnnoBeanMapping getAnnoBeanMapping();

  public ClassLoader getClassLoader();

  public AnnoBean createAnnoBeanFor(Class annoBeanClass);

}
