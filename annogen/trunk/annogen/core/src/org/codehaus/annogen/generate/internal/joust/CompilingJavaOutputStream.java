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

package org.codehaus.annogen.generate.internal.joust;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This a SourceJavaOutputStream which can compile it's results when
 * close() is called.  It always writes source files to disk.
 *
 * @author Patrick Calaham <codehaus@bea.com>
 */
public class CompilingJavaOutputStream extends SourceJavaOutputStream
        implements WriterFactory
 {
  // ========================================================================
  // Constants

  private static final String PREFIX = "[CompilingJavaOutputStream] ";

  // ========================================================================
  // Variables

  private FileWriterFactory mWriterFactoryDelegate;
  private List mSourceFiles;
  private File mCompileDir = null;
  private File[] mJavacClasspath = null;
  private boolean mKeepGenerated;
  private String mJavacPath = null;
  private boolean mDoCompile = true;
  private String mEncoding = null;
  private File mSourceDir = null;

  // ========================================================================
  // Constructors

  /**
   * Construct a new CompilingJavaOutputStream which generates java sources
   * in the given directory.
   *
   * @param srcDir Directory in which sources get generated.
   */
  public CompilingJavaOutputStream(File srcDir) {
    this();
    setSourceDir(srcDir);
  }

  /**
   * Constructs a new CompilingJavaOutputStream.  Note that if you use
   * this default constructor, you *must* call setSourceDir at some point
   * before the stream is used; failure to do so will produce an
   * IllegalStateException.
   */
  public CompilingJavaOutputStream() {
    super();
    setWriterFactory(this);
    mSourceFiles = new ArrayList();
  }

  // ========================================================================
  // Public methods

  //REVIEW the naming this directory 'source' seems a little confusing
  /**
   * Sets the source directory to which files will be written.  This can
   * safely be changed mistream if desired.
   */
  public void setSourceDir(File srcDir) {
    if (srcDir == null) throw new IllegalArgumentException("null srcDir");
    mWriterFactoryDelegate = null;
    mSourceDir = srcDir;
  }

  /**
   * Sets the encoding to use for writing java sources.
   */
  public void setEncoding(String enc) {
    mWriterFactoryDelegate = null;
    mEncoding = enc;
  }
  
  /**
   * Enables compilation of the generated source files into the given
   * directory.  If this method is never called, no compilation will occur.
   */
  public void setCompilationDir(File destDir) {
    mCompileDir = destDir;
  }

  /**
   * Sets the location of javac to be invoked.  Default compiler is used
   * if this is not set.  Ignored if compilationDir is never set.
   */
  public void setJavac(String javacPath) {
    mJavacPath = javacPath;
  }

  /**
   * Sets the classpath to use for compilation.  System classpath is used
   * by default.  This is ignored if compilationDir is never set.
   */
  public void setJavacClasspath(File[] classpath) {
    mJavacClasspath = classpath;
  }

  /**
   * Sets whether generated sources should be kept after compilation.
   * Default is true.  This is ignored if compilationDir is never set.
   */
  public void setKeepGenerated(boolean b) {
    mKeepGenerated = b;
  }

  /**
   * Sets whether javac should be run on the generated sources.  Default
   * is true.
   */
  public void setDoCompile(boolean b) {
    mDoCompile = b;
  }


  // ========================================================================
  // WriterFactory implementation

  /**
   * Delegate to FileWriterFactory, but ask it for Files instead of Writers
   * so that we can keep track of what we need to compile later.
   */
  public Writer createWriter(String packageName, String className)
          throws IOException {
    if (mWriterFactoryDelegate == null) {
      if (mSourceDir == null) {
        throw new IllegalStateException("setSourceDir must be called");
      }
      mWriterFactoryDelegate = (mEncoding != null) ?
        new FileWriterFactory(mSourceDir, mEncoding) :
        new FileWriterFactory(mSourceDir);
    }
    File out = mWriterFactoryDelegate.createFile(packageName,className);
    mSourceFiles.add(out);
    return new FileWriter(out);
  }

  // ========================================================================
  // JavaOutputStream implementation

  public void close() throws IOException {
    super.close();
    verbose("closing");
    if (mDoCompile && mCompileDir != null) {
      verbose("compileDir = "+mCompileDir);
      Iterator i = mSourceFiles.iterator();
      while(i.hasNext()) {
        verbose(i.next().toString());
      }
      boolean verbose = mLogger.isVerbose(this);
      boolean result = mSourceFiles.size() > 0 ? CodeGenUtil.
              externalCompile(mSourceFiles,mCompileDir,mJavacClasspath,
                  verbose,mJavacPath,null,null,!verbose,verbose) : true;
      verbose("compilation result: "+result);
      if (!result) {
        throw new IOException("Compilation of sources failed, " +
                              "check log for details.");
      }
      if (!mKeepGenerated) {
        for(Iterator j = mSourceFiles.iterator(); j.hasNext(); ) {
          File f = (File)j.next();
          verbose("deleting "+f);
          f.delete();
        }
      }
    }
  }

  private void verbose(String msg) {
    if (mLogger.isVerbose(this)) {
      mLogger.verbose(PREFIX+" "+msg);
    }
  }
}