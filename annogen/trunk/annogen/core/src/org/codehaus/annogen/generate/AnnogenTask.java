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
package org.codehaus.annogen.generate;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;
import org.codehaus.jam.JClass;
import org.codehaus.jam.JamService;
import org.codehaus.jam.JamServiceFactory;
import org.codehaus.jam.JamServiceParams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Ant task which generates AnnoBeans.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class AnnogenTask extends Task {

  // ========================================================================
  // Variables

  private Annogen mAnnogen = new Annogen();
  private Path mSrcDir = null;
  private Path mToolpath = null;
  private Path mClasspath = null;
  private String mIncludes = "**/*.java";
  private List mMappings = null;

  // ========================================================================
  // Constructors

  public AnnogenTask() {}

  // ========================================================================
  // Public methods

  /**
   * Sets the directory into which source files should be generated.
   * @param f
   */
  public void setOutputDir(File f) {
    mAnnogen.setOutputDir(f);
  }

  /**
   * Set the source directories to find the source Java files.
   */
  public void setSrcdir(Path srcDir) {
    if (mSrcDir == null) {
      mSrcDir = srcDir;
    } else {
      mSrcDir.append(srcDir);
    }
  }

  /**
   * Includes source files matching the given patten.  Pattern is relative
   * to srcDir.
   */
  public void setIncludes(String includes) {
    if (includes == null) throw new IllegalArgumentException("null includes");
    mIncludes = includes;
  }

  public void setImplementAnnotationTypes(boolean b) {
    mAnnogen.setImplementAnnotationTypes(b);
  }

  public void setToolpath(Path path) {
    if (mToolpath == null) {
      mToolpath = path;
    } else {
      mToolpath.append(path);
    }
  }

  public void setToolpathRef(Reference r) {
    createToolpath().setRefid(r);
  }


  public Path createToolpath() {
    if (mToolpath == null) {
      mToolpath = new Path(getProject());
    }
    return mToolpath.createPath();
  }

  public void setClasspath(Path path) {
    if (mClasspath == null) {
      mClasspath = path;
    } else {
      mClasspath.append(path);
    }
  }

  public void setClasspathRef(Reference r) {
    createClasspath().setRefid(r);
  }


  public Path createClasspath() {
    if (mClasspath == null) {
      mClasspath = new Path(getProject());
    }
    return mClasspath.createPath();
  }


  public void addMapping(AnnoBeanMapping m) {
    if (mMappings == null) mMappings = new ArrayList();
    mMappings.add(m);
  }


  // ========================================================================
  // Task implementation

  public void execute() throws BuildException {
    if (mSrcDir == null) {
      throw new BuildException("'srcDir' must be specified");
    }
    JamServiceFactory jsf = JamServiceFactory.getInstance();
    JamServiceParams p = jsf.createServiceParams();
    if (mToolpath != null) {
     File[] tcp = path2files(mToolpath);
      for(int i=0; i<tcp.length; i++) p.addToolClasspath(tcp[i]);
    }
    if (mClasspath != null) {
     File[] cp = path2files(mClasspath);
      for(int i=0; i<cp.length; i++) p.addClasspath(cp[i]);
    }
    p.includeSourcePattern(path2files(mSrcDir),mIncludes);
    try {
      JamService js = jsf.createService(p);
      JClass[] classes = js.getAllClasses();
      mAnnogen.addAnnotationClasses(classes);
      log("Generating annotation impls for the following classes:");
      for(int i=0; i<classes.length; i++) {
        log("  "+classes[i].getQualifiedName());
      }
      mAnnogen.doCodegen();
      log("...done.");
    } catch(IOException ioe) {
      throw new BuildException(ioe);
    }
  }

  // ========================================================================
  // Private methods

  private File[] path2files(Path path) {
    String[] list = path.list();
    File[] out = new File[list.length];
    for(int i=0; i<out.length; i++) {
      out[i] = new File(list[i]).getAbsoluteFile();
    }
    return out;
  }

}
