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
package org.codehaus.annogen.test.samples.elements.annotated;

import org.codehaus.annogen.test.samples.annotations.BugAnnotation;
import org.codehaus.annogen.test.samples.annotations.BugAnnotation;
import org.codehaus.annogen.test.samples.annotations.BugAnnotation;


/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public interface Igloo {

  @BugAnnotation(id = 43,
                 synopsis = "REVIEW: don't all igloos have only one door?",
                 enteredBy = "codehaus",
                 date = "November 10, 2004")
  public int getDoorCount();


  public void setDoorCount(int foo);
  
}
