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

import org.codehaus.annogen.view.AnnoViewerParams;
import org.codehaus.annogen.view.internal.AnnoViewerBase;
import org.codehaus.annogen.test.samples.annotations.BugAnnotation;
import org.codehaus.annogen.test.samples.elements.annotated.Igloo;
import org.codehaus.annogen.override.AnnoOverrider;
import org.codehaus.annogen.override.AnnoContext;
import org.codehaus.annogen.override.ElementId;
import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.jam.JamServiceFactory;
import org.codehaus.jam.JamServiceParams;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public final class AnnogenTestCase_150 extends AnnogenTestCase {

  // ========================================================================
  // Constants

  //FIXME put a knob on this please so we can get it from the ant script
  private File[] CLASSPATH = { new File("build/annogen_test_samples") };

  //FIXME put a knob on this please so we can get it from the ant script
  private File[] SOURCEPATH = { /*new File(".","annogen/test/samples/src"),*/
                                  new File(".","annogen/test/samples/src_150")};

  // ========================================================================
  // Constructors

  public AnnogenTestCase_150() {}

  public AnnogenTestCase_150(String name) { super(name); }


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

  // ========================================================================
  // Test methods

  public void testSimpleOverride() throws Exception
  {
    AnnogenContext[] stuffers = getStuffers();
    for(int i=0; i<stuffers.length; i++) _testSimpleOverride(stuffers[i]);
  }

  private void _testSimpleOverride(AnnogenContext stuffer) throws Exception
  {
    final String ANNOTATED_METHOD = "getDoorCount";
    final String ANNOTATED_CLASS = Igloo.class.getName();
    final int FAKEID = 343432;
    TestElementId id = new TestElementId();
    {
      // set up the ElementId of the method
      id.setType(TestElementId.METHOD_TYPE);
      id.setContainingClass(Igloo.class.getName());
      id.setName("getDoorCount");
      stuffer.stuffExtractor(id);
    }
    AnnoViewerParams asp = AnnoViewerParams.Factory.create();
    // create a specialized ProxyPopulator just for this test
    asp.addOverrider(new AnnoOverrider() {
      public void init(AnnoContext pc) {}

      public void modifyAnnos(ElementId id, AnnoBeanSet currentAnnos) {
        if (id.getType() == ElementId.METHOD_TYPE &&
            id.getContainingClass().equals(Igloo.class.getName()) &&
            id.getName().equals(ANNOTATED_METHOD)) {
            AnnoBean p = currentAnnos.findOrCreateBeanFor(BugAnnotation.class);
            assertTrue("error encountered setting 'id'",
                       p.setValue("id",new Integer(FAKEID)));
        }
      }
    });
    AnnoViewerBase viewer = stuffer.createViewer(asp);

/*
    Method annotatedMethod = Igloo.class.getMethod(ANNOTATED_METHOD,null);
    Method notAnnotatedMethod = QuansaHut.class.getMethod(ANNOTATED_METHOD,null);
    // ok now do the tests
    {
      // a quick sanity test to be sure the annotation is actually there
      BugAnnotation ban = annotatedMethod.getAnnotation(BugAnnotation.class);
      assertTrue("original annotation isn't even there!",ban != null);
    }
    */
    {
      // now make sure the service still sees it.  this is testing a degenerate
      // case where they want use 175 but specified 14compatibilitymode.
      BugAnnotation bug = (BugAnnotation)
        viewer.getAnnotation(BugAnnotation.class,id);
      assertTrue("service not reporting @BugAnnotation on "+
                 ANNOTATED_CLASS+"."+ANNOTATED_METHOD, bug != null);
      assertTrue("expected @BugAnnotation.id() to be "+FAKEID+" , is "+bug.id(),
                 bug.id() == FAKEID);
    }
/*
{
      // make sure an annotation was not synthesized where it should not have been
      BugAnnotationImpl bug = (BugAnnotationImpl)
          ras.getAnnotation(BugAnnotationImpl.class,notAnnotatedMethod);
      assertTrue("unexpected BugAnnotation on "+annotatedMethod,bug == null);
    }
    {
      // negative test to make sure we get an IAE when annogen was run 1.4-safe
      try {
        ras.getAnnotation(BugAnnotation.class,annotatedMethod);
        assertTrue("did not get expectd IllegalArgumentException",false);
      } catch(IllegalArgumentException expected) {}
    }
*/
  }

}
