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
package org.codehaus.jam.provider;

import org.codehaus.jam.mutable.MClass;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface JamClassPopulator {

  /**
   * <p>Called by JAM to 'fill out' an instance of a given MClass with
   * things like methods and fields.  The implementing builder is responsible
   * for inspecting the source artifact (typically a source or class file)
   * to call the appropriate createX methods on the given MClass.</p>
   *
   * @param c
   */
  public abstract void populate(MClass c);

}
