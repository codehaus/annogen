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
package org.codehaus.jam.internal.javadoc;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.SourcePosition;

/**
 * This class is part of the fix/workaround for a javadoc bug
 * as described in issue 14: http://jira.codehaus.org/browse/ANNOGEN-14
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class JavadocParsingException extends Exception {

  private AnnotationDesc mBad;
  private SourcePosition mSp;

  public JavadocParsingException(AnnotationDesc desc,
                                 SourcePosition sp,
                                 Throwable nested) {
    super("Parsing failure in "+sp.file()+" at line "+sp.line()+".  "+
          "Most likely, an annotation is declared whose type has not been "+
          "imported.");
    mBad = desc;
    mSp = sp;
  }

  public SourcePosition getSourcePosition() {
    return mSp;
  }

  public AnnotationDesc getBadAnnotationDesc() {
    return mBad;
  }
}
