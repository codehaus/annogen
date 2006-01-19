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
import org.codehaus.annogen.generate.AnnoBeanMapping;
import org.codehaus.annogen.generate.Annogen;
import org.codehaus.annogen.generate.AnnogenTask;
import org.codehaus.annogen.generate.internal.PropfileUtils;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JamService;
import org.codehaus.jam.JamServiceFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.net.URL;
import java.util.Properties;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class AnnogenTestCase extends TestCase {


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
    return new AnnogenContext[0];
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

  public void testAnnoBeanMappingLogic() {
    {
      String type = "com.foo.bar.SimpleAnno";
      String bean = "com.foo.bar.annobeans.SimpleAnnoBean";
      AnnoBeanMapping abm = new AnnoBeanMapping(type,bean);
      assertTrue(abm.getAnnoBeanFor(type).equals(bean));
      assertTrue(abm.getAnnoBeanFor(type+"random") == null);
      assertTrue(abm.getAnnoBeanFor("foo."+type) == null);
    }
    {
      String type = "com.foo.bar.*Anno";
      String bean = "com.foo.bar.annobeans.*AnnoBean";
      AnnoBeanMapping abm = new AnnoBeanMapping(type,bean);
      assertTrue(abm.getAnnoBeanFor(type).equals(bean));
      assertTrue(abm.getAnnoBeanFor("com.bing.MyAnno") == null);
      assertTrue(abm.getAnnoBeanFor("com.foo.bar.MyAno") == null);
    }
  }


  public void testPropfileUtils() throws IOException, ClassNotFoundException {
    File outputDir = createTempDir();
    System.out.println("temp output in "+outputDir);
    Class jsr175class = String.class;
    JClass jsr175type = JamServiceFactory.getInstance().createSystemJamClassLoader().
        loadClass(jsr175class.getName());
    String annobeanType = Annogen.getDefaultAnnobeanClassnameFor(jsr175type);
    assertTrue(annobeanType.equals("java.lang.annobeans.StringAnnobean"));
    PropfileUtils.getInstance().writeAnnobeanTypeFor(jsr175type,
                                                     annobeanType,
                                                     outputDir);
    ClassLoader cl = new URLClassLoader(new URL[] {outputDir.toURL()});
    Properties props =
        PropfileUtils.getInstance().getPropfileForAnnotype(jsr175class,cl);
    assertTrue(props != null);
    assertTrue(props.getProperty("annobean").equals(annobeanType));
  }

  private static File createTempDir() throws IOException {
    File outputFile = File.createTempFile("annogen_test","");
    File tempDir = outputFile.getParentFile();
    outputFile.delete();
    File out = new File(tempDir,outputFile.getName());
    if (!out.mkdir()) throw new IOException("failed to mkdir "+out);
    return out;
  }

}
