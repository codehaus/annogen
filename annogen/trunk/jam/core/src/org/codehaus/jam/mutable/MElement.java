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

package org.codehaus.jam.mutable;

import org.codehaus.jam.JElement;
import org.codehaus.jam.JamClassLoader;
import org.codehaus.jam.visitor.MVisitor;

/**
 * <p>Mutable version of JElement.</p>
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public interface MElement extends JElement {

  public JamClassLoader getClassLoader();

  //DOCME
  public void setSimpleName(String name);

  //DOCME
  public MSourcePosition createSourcePosition();

  //DOCME
  public void removeSourcePosition();

  //DOCME
  public MSourcePosition getMutableSourcePosition();

  //DOCME
  public void accept(MVisitor visitor);

  //DOCME
  public void setArtifact(Object o);
}