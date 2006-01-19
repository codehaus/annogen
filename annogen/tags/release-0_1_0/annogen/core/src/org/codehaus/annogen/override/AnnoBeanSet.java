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
 * Encapsualtes a set of AnnoBeans which apply to some element in the java
 * type system, such as a class, method, or field.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface AnnoBeanSet {

  public boolean containsBeanFor(Class annoClass);

  public AnnoBean findOrCreateBeanFor(Class annoClass);

  public AnnoBean removeBeanFor(Class annoClass);

  public AnnoBean[] getAll();

  public int size();

  public void put(AnnoBean ap);

}
