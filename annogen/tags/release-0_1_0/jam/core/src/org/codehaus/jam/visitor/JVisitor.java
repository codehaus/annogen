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

import org.codehaus.jam.JAnnotation;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JComment;
import org.codehaus.jam.JConstructor;
import org.codehaus.jam.JField;
import org.codehaus.jam.JMethod;
import org.codehaus.jam.JPackage;
import org.codehaus.jam.JParameter;
import org.codehaus.jam.JProperty;
import org.codehaus.jam.JTag;

//REVIEW I think this should be an interface
/**
 * <p>To be implemented by classes which wish to traverse a JElement tree.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public abstract class JVisitor {

  public void visit(JPackage pkg) {}

  public void visit(JClass clazz) {}

  public void visit(JConstructor ctor) {}

  public void visit(JField field) {}

  public void visit(JMethod method) {}

  public void visit(JParameter param) {}

  public void visit(JAnnotation ann) {}

  public void visit(JComment comment) {}

  public void visit(JProperty property) {}

  public void visit(JTag tag) {}

}
