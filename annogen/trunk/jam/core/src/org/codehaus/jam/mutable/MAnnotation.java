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

import org.codehaus.jam.JAnnotation;
import org.codehaus.jam.JClass;

/**
 * <p>Mutable version of JAnnotation.</p>
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public interface MAnnotation extends JAnnotation, MElement {

  // ========================================================================
  // MAnnotation implementation

  public void setAnnotationInstance(Object o);

  public void setSimpleValue(String name, Object value, JClass declaredType);

  public MAnnotation createNestedValue(String name, String annTypeName);

  public MAnnotation[] createNestedValueArray(String name,
                                              String componentTypeName,
                                              int dimensions);

  //public AnnotationProxy getMutableProxy();//i think this is bad

}
