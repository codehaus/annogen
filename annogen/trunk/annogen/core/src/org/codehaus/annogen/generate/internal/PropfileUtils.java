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
package org.codehaus.annogen.generate.internal;

import org.codehaus.jam.JClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton utility class which encapsulates details of reading and writing
 * the annobean classname associated with each JSR175 type compiled by
 * annogen.  We stuff the name of the annobean type away in one magic
 * properties file per-175 type.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class PropfileUtils {

  // ========================================================================
  // Constants

  private static final String PROPFILE_SUFFIX = ".annogen.properties";

  private static final String ANNOBEAN_PROP = "annobean";

  // ========================================================================
  // Singleton


  private static final PropfileUtils INSTANCE = new PropfileUtils();

  public static PropfileUtils getInstance() { return INSTANCE; }

  private PropfileUtils() {}

  // ========================================================================
  // Public utility methods

  /**
   * Writes out a properties file to disk to map the given 175 type to
   * the named annobean type.  The rootDir should be the root of the
   * classpath element where the annobeanType will be written.
   */
  public void writeAnnobeanTypeFor(JClass jsr175type,
                                   String annobeanType,
                                   File rootDir) throws IOException {
    if (jsr175type == null) throw new IllegalArgumentException("null 175 type");
    if (annobeanType == null) throw new IllegalArgumentException("null annobeanType");
    if (rootDir == null) throw new IllegalArgumentException("null rootDir");
    Properties props = new Properties();
    props.setProperty(ANNOBEAN_PROP,annobeanType);
    writeProperties(jsr175type,props,rootDir);
  }


  /**
   * Returns the name of the annobean type to which the given jsr175type maps.
   * The properties file containing the mapping must be loadable in the given
   * classloader (should be the same classloader that the annobean class will
   * be loaded from).
   */
  public Class getAnnobeanTypeFor(Class jsr175type, ClassLoader cl)
      throws IOException, ClassNotFoundException
  {
    Properties typeProps = getPropfileForAnnotype(jsr175type, cl);
    String name = typeProps.getProperty(ANNOBEAN_PROP);
    if (name == null) throw new IllegalStateException("no "+ANNOBEAN_PROP+
                                                     " in "+typeProps);
    return cl.loadClass(name);
  }


  // ========================================================================
  // Private methods

  private void writeProperties(JClass jsr175type,
                               Properties props,
                               File rootDir) throws IOException {
    String propFile =
        jsr175type.getQualifiedName().replace('.',File.separatorChar);
    File file = new File(rootDir, propFile+PROPFILE_SUFFIX);
    if (!file.getParentFile().exists() &&
        !file.getParentFile().mkdirs()) {
      throw new IOException("failed to create "+file.getParentFile());
    }
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);
      props.store(fos,null);
    } catch(RuntimeException e) {
      throw e;
    } catch(IOException e) {
      throw e;
    } finally {
      if (fos != null) fos.close();
    }
  }


  public Properties getPropfileForAnnotype(Class jsr175type,
                                            ClassLoader cl)
      throws IOException
  {
    String propFile = jsr175type.getName().replace('.',File.separatorChar)+
        PROPFILE_SUFFIX;
    InputStream in = cl.getResourceAsStream(propFile);
    if (in == null) throw new IOException("Could not load "+propFile);
    Properties out = new Properties();
    try {
      out.load(in);
    } catch(IOException e) {
      throw e;
    } catch(RuntimeException re) {
      throw re;
    } finally {
      in.close();
    }
    return out;
  }
}
