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
package org.codehaus.jam.test.samples;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public abstract class VarietyOfModifiers {

  public transient String iAmPublicTransient;

  protected volatile int iAmProtectedVolatile;

  
  public abstract void iAmPublicAbstract();

  protected final void iAmProtectedFinal() {}

  private synchronized void iAmPrivateSynchronized() {}

  /*package*/ static void iAmPackageStatic() {}

}
