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
package org.codehaus.jam.internal.parser;

import org.codehaus.jam.JamClassLoader;
import org.codehaus.jam.internal.JamPrinter;
import org.codehaus.jam.mutable.MClass;
import org.codehaus.jam.provider.JamClassBuilder;
import org.codehaus.jam.provider.JamClassPopulator;
import org.codehaus.jam.provider.JamServiceContext;
import org.codehaus.jam.provider.ResourcePath;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

//FIXME uncomment when we get around to working on the parser again
//import org.codehaus.jam.internal.parser.generated.JavaLexer;
//import org.codehaus.jam.internal.parser.generated.JavaParser;

/**
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class ParserClassBuilder extends JamClassBuilder implements JamClassPopulator {

  // ========================================================================
  // Constants

  private static final boolean VERBOSE = false;

  // ========================================================================
  // Variables

  private ResourcePath mSourcePath;
  private boolean mVerbose = VERBOSE;
  private PrintWriter mOut = new PrintWriter(System.out);

  // ========================================================================
  // Constructors

  private ParserClassBuilder() {}

  public ParserClassBuilder(JamServiceContext jsp) {
    mSourcePath = jsp.getInputSourcepath();
    //mOut = jsp.getOut();
  }

  // ========================================================================
  // BaseJClassLoader implementation

  public MClass build(String pkg, String name) {
    if (pkg == null) throw new IllegalArgumentException("null pkg");
    if (name == null) throw new IllegalArgumentException("null name");
    String filespec = pkg.replace('.',File.separatorChar)+
            File.separatorChar+name+".java";
    if (name.indexOf(".") != -1) {
      throw new IllegalArgumentException("inner classes are NYI at the moment");
    }
    InputStream in = mSourcePath.findInPath(filespec);
    if (in == null) {
      if (mVerbose) {
        mOut.println("[ParserClassBuilder] could not find "+filespec);
      }
      return null;
    } else {
      if (mVerbose) {
        mOut.println("[ParserClassBuilder] loading class "+pkg+"  "+name);
        mOut.println("[ParserClassBuilder] from file "+filespec);
      }
    }
    Reader rin = new InputStreamReader(in);
    try {
      /*
      MClass[] clazz = null;//FIXME parse(rin,loader);
      if (clazz.length > 1) {
        System.out.println("WARNING: multiple classes per package are not "+
                           "handled correctly at the moment.  FIXME");
      }
      return clazz[0]; //FIXME deal properly with multiple classes
      */
    } catch(Throwable t) {
      t.printStackTrace();
    } finally {
      try {
      rin.close();
      } catch(IOException ohwell) {
        ohwell.printStackTrace();
      }
    }
    return null;
  }

  // ========================================================================
  // JamClassPopulator implementation

  public void populate(MClass m) {
    throw new IllegalStateException("NYI");
  }

  // ========================================================================
  // Private methods

  private static MClass[] parse(Reader in, JamClassLoader loader) throws Exception {
    if (in == null) throw new IllegalArgumentException("null in");
    if (loader == null) throw new IllegalArgumentException("null loader");

    //FIXME uncomment when we get around to working on the parser again
    throw new IllegalStateException("temporarily NI");
/*
    JavaLexer lexer = new JavaLexer(in);
    JavaParser parser = new JavaParser(lexer);
    parser.setClassLoader(loader);
    parser.start();
    return parser.getResults();
    */
  }

  // ========================================================================
  // main method

  public static void main(String[] files) {
    new ParserClassBuilder.MainTool().process(files);
  }

  static class MainTool {
    private List mFailures = new ArrayList();
    private int mCount = 0;
    private PrintWriter mOut = new PrintWriter(System.out);
    private long mStartTime = System.currentTimeMillis();

    public void process(String[] files) {
      try {
        for(int i=0; i<files.length; i++) {
          File input = new File(files[i]);
          parse(new ParserClassBuilder(),input);
        }
      } catch(Exception e) {
        e.printStackTrace();
      }
      mOut.println("\n\n\n");
      int fails = mFailures.size();
      if (fails != 0) {
        mOut.println("The following files failed to parse:");
        for(int i=0; i<fails; i++) {
          mOut.println(((File)mFailures.get(i)).getAbsolutePath());
        }
      }
      mOut.println((((mCount-fails)*100)/mCount)+
                   "% ("+(mCount-fails)+"/"+mCount+") "+
                   "of input java files successfully parsed.");
      mOut.println("Total time: "+
                   ((System.currentTimeMillis()-mStartTime)/1000)+
                   " seconds.");
      mOut.flush();
      System.out.flush();
      System.err.flush();
    }

    private void parse(ParserClassBuilder parser, File input)
            throws Exception
    {
      System.gc();
      if (input.isDirectory()) {
        if (VERBOSE) mOut.println("scanning in directory "+input);
        File[] files = input.listFiles();
        for(int i=0; i<files.length; i++) {
          parse(parser,files[i]);
        }
      } else {
        if (!input.getName().endsWith(".java")) return;
        if (VERBOSE) {
          mOut.println("-----------------------------------------");
          mOut.println("processing "+input);
          mOut.println("-----------------------------------------");
        }
        mCount++;
        MClass[] results = null;
        try {
          results = parser.parse(new FileReader(input),null);
          if (results == null) {
            mOut.println("[error, parser result is null]");
            addFailure(input);
          } else {
            if (VERBOSE) {
              JamPrinter jp = JamPrinter.newInstance();
              for(int i=0; i<results.length; i++) {
                jp.print(results[i],mOut);
              }
            }
          }
        } catch(Throwable e) {
          e.printStackTrace(mOut);
          addFailure(input);
        }
      }
    }

    private void addFailure(File file) {
      mFailures.add(file);
    }
  }
}