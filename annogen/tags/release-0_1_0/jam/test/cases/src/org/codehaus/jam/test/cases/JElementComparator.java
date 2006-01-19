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
package org.codehaus.jam.test.cases;

import org.codehaus.jam.JElement;

import java.util.Comparator;

/**
 * <p>Comparator for sorting JElements by qualified name.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class JElementComparator implements Comparator {

  // ========================================================================
  // Singleton

  private static final Comparator INSTANCE = new JElementComparator();

  public static final Comparator getInstance() { return INSTANCE; }

  private JElementComparator() {}

  // ========================================================================
  // Comparator implementation

  public int compare(Object o1, Object o2) {
    return ((JElement)o1).getQualifiedName().compareTo(
            ((JElement)o2).getQualifiedName());
  }
}
