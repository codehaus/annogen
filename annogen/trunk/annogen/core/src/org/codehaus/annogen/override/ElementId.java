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

/**
 * Generic identifier for some annotatable java element such as a class or
 * method.
 * This abstraction allows annogen to work with an arbitrary set of
 * java type system APIs, e.g. reflection and javadoc.  Note that
 * various ElementIdPools are provided for creating and pooling Ids using
 * various APIs.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface ElementId {

  // ========================================================================
  // Constants

  //would be nice to use enums, but it's not an option since we want to support 1.4
  public static final int PACKAGE_TYPE = 0;
  public static final int CLASS_TYPE = 1;
  public static final int FIELD_TYPE = 2;
  public static final int METHOD_TYPE = 3;
  public static final int CONSTRUCTOR_TYPE = 4;
  public static final int PARAMETER_TYPE = 5;
  public static final int ANNOTATION_TYPE = 6;

  public static final int NO_PARAMETER = -1;

  // ========================================================================
  // Public methods

  //public ElementId getEnclosingElement();

  public int getType();

  public String getName();

  public String getContainingClass();

  //public String getContainingPackage();

  public String[] getSignature();

  public int getParameterNumber();

}
