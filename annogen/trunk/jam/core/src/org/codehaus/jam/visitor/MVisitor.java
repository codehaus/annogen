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
package org.codehaus.jam.visitor;

import org.codehaus.jam.mutable.MAnnotation;
import org.codehaus.jam.mutable.MClass;
import org.codehaus.jam.mutable.MComment;
import org.codehaus.jam.mutable.MConstructor;
import org.codehaus.jam.mutable.MField;
import org.codehaus.jam.mutable.MMethod;
import org.codehaus.jam.mutable.MPackage;
import org.codehaus.jam.mutable.MParameter;

/**
 * <p>To be extended by classes which wish to traverse an MElement tree.</p>
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public abstract class MVisitor {

  public void visit(MPackage pkg) {}

  public void visit(MClass clazz) {}

  public void visit(MConstructor ctor) {}

  public void visit(MField field) {}

  public void visit(MMethod method) {}

  public void visit(MParameter param) {}

  public void visit(MAnnotation ann) {}

  public void visit(MComment comment) {}

}
