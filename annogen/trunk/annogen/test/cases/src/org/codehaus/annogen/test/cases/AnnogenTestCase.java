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
package org.codehaus.annogen.test.cases;

import junit.framework.TestCase;
import org.codehaus.jam.JamServiceFactory;
import org.codehaus.jam.JamServiceParams;

import java.util.ArrayList;
import java.util.List;
import java.io.File;

/**
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public abstract class AnnogenTestCase extends TestCase {

  // ========================================================================
  // Constants

  //FIXME put a knob on this please so we can get it from the ant script
  private File[] CLASSPATH = { new File("build/annogen_test_samples") };

  //FIXME put a knob on this please so we can get it from the ant script
  private File[] SOURCEPATH = { /*new File(".","annogen/test/samples/src"),*/
                                  new File(".","annogen/test/samples/src_150")};

  // ========================================================================
  // Variables

  private static AnnogenContext[] mStuffers = null;

  // ========================================================================
  // Constructors

  public AnnogenTestCase() {}
  
  public AnnogenTestCase(String name) { super(name); }

  // ========================================================================
  // Protected methods

  protected AnnogenContext[] initStuffers() throws Exception {
    List list = new ArrayList();
    {
      // reflect stuffer
      list.add(new ReflectContext(ClassLoader.getSystemClassLoader()));
    }
    {
      // source-based jam stuffer
      JamServiceFactory jsf = JamServiceFactory.getInstance();
      JamServiceParams params = jsf.createServiceParams();
      params.includeSourcePattern(SOURCEPATH,
                                  "org/codehaus/annogen/test/samples/**/*.java");
      list.add(new JamContext(jsf.createService(params).getClassLoader()));
    }
    {
      // class-based jam stuffer
      JamServiceFactory jsf = JamServiceFactory.getInstance();
      JamServiceParams params = jsf.createServiceParams();
      params.includeClassPattern(CLASSPATH,
                                 "org/codehaus/annogen/test/samples/**/*.class");
      list.add(new JamContext(jsf.createService(params).getClassLoader()));
    }
    {
      // package up all the stuffers and return them
      AnnogenContext[] out = new AnnogenContext[list.size()];
      list.toArray(out);
      return out;
    }
  }

  protected AnnogenContext[] getStuffers() {
    if (mStuffers == null) throw new IllegalStateException();
    return mStuffers;
  }

  // ========================================================================
  // JUnit methods

  public final void setUp() throws Exception {
    if (mStuffers == null) mStuffers = initStuffers();
  }

  // ========================================================================
  // Test methods

  public void testPleaseAddSome14SafeTests() {}
}
