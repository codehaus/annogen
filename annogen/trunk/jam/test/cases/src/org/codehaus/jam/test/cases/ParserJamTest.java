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

import org.codehaus.jam.JamService;
import org.codehaus.jam.JamServiceFactory;
import org.codehaus.jam.JamServiceParams;
import org.codehaus.jam.test.cases.JamTestBase;
import org.codehaus.jam.provider.JamServiceFactoryImpl;

import java.io.File;
import java.io.IOException;

/**
 * THIS CLASS IS CURRENTLY DEAD CODE
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public class ParserJamTest extends JamTestBase {

  // ========================================================================
  // Constructors

  public ParserJamTest(String name) {
    super(name);
  }

  // ========================================================================
  // JamTestBase implementation

  protected JamService getResultToTest() throws IOException {
    JamServiceFactory jsf = JamServiceFactory.getInstance();
    JamServiceParams params = jsf.createServiceParams();
    params.setProperty(JamServiceFactoryImpl.USE_NEW_PARSER,"true");
    params.includeSourcePattern(getSamplesSourcepath(),"**/*.java");
    return jsf.createService(params);
  }

  protected boolean is175AnnotationsAvailable() { return false;//FIXME!!
  }

  protected boolean isImportsAvailable() { return false; }

  //kind of a quick hack for now, should remove this and make sure that
  //even the classes case make the annotations available using a special
  //JStore
  protected boolean is175AnnotationInstanceAvailable() {
    return false;
  }

  protected boolean isJavadocTagsAvailable() { return false; }


  protected boolean isParameterNamesKnown() {
    return true;
  }

  protected boolean isCommentsAvailable() {
    return true;
  }

  protected File getMasterDir() {
    return new File("masters/parser");
  }
}