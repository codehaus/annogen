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
package org.codehaus.annogen.view;

import org.codehaus.annogen.override.AnnoOverrider;
import org.codehaus.annogen.view.internal.AnnoViewerParamsImpl;

/**
 * Encapsulates a set of parameters to be used in instantiating an
 * AnnoViewer, including the set of AnnoOverriders to be used.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface AnnoViewerParams {

  // ========================================================================
  // Factory

  /**
   * Static factory for AnnoViewerParams.
   */
  public static class Factory {
    public static AnnoViewerParams create() {
      return new AnnoViewerParamsImpl();
    }
  }

  // ========================================================================
  // Public methods

  /**
   * Adds an anno overrider that will be consulted by the AnnoViewer
   * constructed from these params.
   */
  public void addOverrider(AnnoOverrider ao);

  /**
   * Sets the classloader from which Annobeans will be loaded.  By default,
   * the system classloader is used.
   */
  public void setClassLoader(ClassLoader c);

  /**
   * </p>Enables verbose debugging output from all instances of the given
   * class.</p>
   */
  public void setVerbose(Class c);



}
