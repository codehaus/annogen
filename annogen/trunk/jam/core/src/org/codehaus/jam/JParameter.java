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

package org.codehaus.jam;



/**
 * Represents a single parameter of a Java method or constructor.
 * This type and name information as well as associated metadata.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface JParameter extends JAnnotatedElement {

  /**
   * Returns the type of this parameter.
   */
  public JClass getType();


  /**
   * Returns the position of this parameter in its enclosing method's
   * or constructor's list of parameters.
   *
   * This is commented out because as a general rule, it's probably bad
   * to expose this information; it violates the notion that a contained
   * object shouldn't know anything about how it is contained.  I waver
   * on it a little because there are a some samples where (esp. in annogen)
   * where it is useful to know this, primarily due to the fact that
   * the reflection API does not treat parameters as first-class citizens.
   *
   * @return
   */
  //public int getNumber();



  // REVIEW: This would be nice, but there is currently no way to find
  // this out via either javadoc or reflection.  I guess it's not
  // clear that this is going to be very useful anyway.
  /**
   * Return true if this parameter is final.
   */
  //  public boolean isFinal();
}

