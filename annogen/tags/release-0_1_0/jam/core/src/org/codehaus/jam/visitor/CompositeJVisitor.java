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

/**
 * <p>Composite implementation of JVisitor.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class CompositeJVisitor extends JVisitor {

  // ========================================================================
  // Variables

  private JVisitor[] mVisitors;

  // ========================================================================
  // Constructors

  public CompositeJVisitor(JVisitor[] visitors) {
    if (visitors == null) throw new IllegalArgumentException("null visitors");
    mVisitors = visitors;
  }

  // ========================================================================
  // JVisitor implementation

  public void visit(JPackage pkg) {
    for(int i=0; i<mVisitors.length; i++) mVisitors[i].visit(pkg);
  }

  public void visit(JClass clazz) {
    for(int i=0; i<mVisitors.length; i++) mVisitors[i].visit(clazz);
  }

  public void visit(JConstructor ctor) {
    for(int i=0; i<mVisitors.length; i++) mVisitors[i].visit(ctor);
  }

  public void visit(JField field) {
    for(int i=0; i<mVisitors.length; i++) mVisitors[i].visit(field);
  }

  public void visit(JMethod method) {
    for(int i=0; i<mVisitors.length; i++) mVisitors[i].visit(method);
  }

  public void visit(JParameter param) {
    for(int i=0; i<mVisitors.length; i++) mVisitors[i].visit(param);
  }

  public void visit(JAnnotation ann) {
    for(int i=0; i<mVisitors.length; i++) mVisitors[i].visit(ann);
  }

  public void visit(JComment comment) {
    for(int i=0; i<mVisitors.length; i++) mVisitors[i].visit(comment);
  }
}
