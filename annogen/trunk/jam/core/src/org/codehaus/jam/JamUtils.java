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
package org.codehaus.jam;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Singleton collection of utility methods which can be very useful in
 * some samples, but not often enough to warrant inclusion in the main
 * APIs.
 * </p>
 * 
 * <p>
 * Most of these are here to help you jump from the JClass hierarchy to
 * the java.lang.reflect hierarchy. This is primarily useful when you
 * find yourself, for example, wanting to actually invoke a method
 * represented by a JMethod.
 * </p>
 * 
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class JamUtils {

  // ========================================================================
  // Singleton

  public static final JamUtils getInstance() {
    return INSTANCE;
  }

  private JamUtils() {}

  private static final JamUtils INSTANCE = new JamUtils();

  // ========================================================================
  // Public methods

  /**
   * <p>
   * Returns the java.lang.Method on a given java.lang.Class which is
   * represented by a given JMethod.
   * </p>
   * 
   * @param containedin
   *          Class to be searched for the method.
   * @return
   * @throws ClassNotFoundException
   *           if one of the paramType classes specified for this
   *           MethodName cannot be loaded from the given class'
   *           classloader.
   * @throws NoSuchMethodException
   *           If the named method does not exist on this class.
   * @throws IllegalArgumentException
   *           if any argument is null
   */
  public Method getMethodOn(JMethod method, Class containedin)
      throws NoSuchMethodException, ClassNotFoundException
  {
    if (containedin == null) {
      throw new IllegalArgumentException("null class");
    }
    if (method == null) {
      throw new IllegalArgumentException("null method");
    }
    Class[] types = null;
    JParameter[] params = method.getParameters();
    if (params != null && params.length > 0) {
      types = new Class[params.length];
      for (int i = 0; i < types.length; i++) {
        types[i] = loadClass(params[i].getType(),
                             containedin.getClassLoader());
      }
    }
    return containedin.getMethod(method.getSimpleName(), types);
  }

  /**
   * <p>
   * Returns the java.lang.Method on a given java.lang.Class which is
   * represented by a given JMethod.
   * </p>
   * 
   * @param containedin
   *          Class to be searched for the ctor.
   * @return
   * @throws ClassNotFoundException
   *           if one of the paramType classes specified for this
   *           MethodName cannot be loaded from the given class'
   *           classloader.
   * @throws NoSuchMethodException
   *           If the named ctor does not exist on this class.
   * @throws IllegalArgumentException
   *           if any argument is null
   */
  public Constructor getConstructorOn(JConstructor ctor,
                                      Class containedin)
      throws NoSuchMethodException, ClassNotFoundException
  {
    if (containedin == null) {
      throw new IllegalArgumentException("null class");
    }
    if (ctor == null) throw new IllegalArgumentException("null ctor");
    Class[] types = null;
    JParameter[] params = ctor.getParameters();
    if (params != null && params.length > 0) {
      types = new Class[params.length];
      for (int i = 0; i < types.length; i++) {
        types[i] = loadClass(params[i].getType(),
                             containedin.getClassLoader());
      }
    }
    return containedin.getConstructor(types);
  }

  /**
   * <p>
   * Returns the java.lang.Method on a given java.lang.Class which is
   * represented by a given JMethod.
   * </p>
   * 
   * @param containedin
   *          Class to be searched for the method.
   * @return
   * @throws NoSuchFieldException
   *           if the field does not exist on the class.
   * @throws IllegalArgumentException
   *           if any argument is null
   */
  public Field getFieldOn(JField field, Class containedin)
      throws NoSuchFieldException
  {
    if (containedin == null) {
      throw new IllegalArgumentException("null class");
    }
    if (field == null) throw new IllegalArgumentException("null field");
    return containedin.getField(field.getSimpleName());
  }

  /**
   * <p>
   * Loads the java.lang.Class represented by a given JClass out of a
   * given classloader.
   * </p>
   * 
   * @return
   * @throws ClassNotFoundException
   *           If the class is not found in the classloader
   */
  public Class loadClass(JClass clazz, ClassLoader inThisClassloader)
      throws ClassNotFoundException
  {
    return inThisClassloader.loadClass(clazz.getQualifiedName());
  }

  /**
   * Sorts the given array in place so that the elements are ordered by
   * their sourcePosition's line numbers.
   */
  public void placeInSourceOrder(JElement[] elements) {
    if (elements == null) {
      throw new IllegalArgumentException("null elements");
    }
    Arrays.sort(elements, SOURCE_POSITION_COMPARATOR);
  }

  
    /**
     * Sorts the given array in place so that the elements are ordered
     * alphabetically by their simple names.
     */
  public void placeInAlphabeticalOrder(JElement[] elements) {
    if (elements == null) {
      throw new IllegalArgumentException("null elements");
    }
    Arrays.sort(elements, ALPHABETICAL_COMPARATOR);
  }

  
  /**
   * <p>
   * Utility method for mapping Files to JClasses. This method returns
   * an array of JClasses such that the nth JClass is the JClass in the
   * input 'classes' array that is described by the nth source File in
   * the input 'files' array. The returned array is guaranteed to be of
   * the same length as 'files.'
   * </p>
   * 
   * <p>
   * Note that the File-JClass association can only be made for JClasses
   * which are in the input set and which were originally loaded from a
   * source file.
   * </p>
   * 
   * <p>
   * If a matching JClass cannot be found for the nth element in
   * 'files', or if the nth element in 'files' is null, the nth element
   * in the returned array will be null.
   * </p>
   */
  public JClass[] getClassesCorrespondingTo(File[] files,
                                            JClass[] classes)
  {
    if (files == null) {
      throw new IllegalArgumentException("null files");
    }
    if (classes == null) {
      throw new IllegalArgumentException("null classes");
    }
    Map uri2class = new HashMap();
    for (int i = 0; i < classes.length; i++) {
      if (classes[i] == null) continue;
      JSourcePosition sp = classes[i].getSourcePosition();
      if (sp == null) continue;
      URI uri = sp.getSourceURI();
      if (uri == null) continue;
      uri2class.put(uri, classes[i]);
    }
    JClass[] out = new JClass[files.length];
    for (int i = 0; i < out.length; i++) {
      if (files[i] == null) continue;
      URI uri = files[i].toURI();
      out[i] = (JClass) uri2class.get(uri);
    }
    return out;
  }

  // ========================================================================
  // Private

  private static Comparator SOURCE_POSITION_COMPARATOR = new Comparator()
  {

    public int compare(Object o, Object o1) {
      JSourcePosition p1 = ((JElement) o).getSourcePosition();
      JSourcePosition p2 = ((JElement) o1).getSourcePosition();
      if (p1 == null) return (p2 == null) ? 0 : -1;
      if (p2 == null) return 1;
      return (p1.getLine() < p2.getLine()) ? -1
          : (p1.getLine() > p2.getLine()) ? 1 : 0;
    }
  };

  private static Comparator ALPHABETICAL_COMPARATOR = new Comparator()
  {

    public int compare(Object o1, Object o2) {
      String n1 = ((JElement) o1).getSimpleName();
      String n2 = ((JElement) o2).getSimpleName();
      return n1.compareTo(n2);
    }
  };

}