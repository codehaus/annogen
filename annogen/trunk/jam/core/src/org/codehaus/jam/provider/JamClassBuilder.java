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
package org.codehaus.jam.provider;

import org.codehaus.jam.internal.elements.ClassImpl;
import org.codehaus.jam.internal.elements.ElementContext;
import org.codehaus.jam.mutable.MClass;

/**
 * <p>Implemented by providers to build and initialize classes on demand.
 * The main responsibility a JAM provider has is writing an extension of this
 * class.
 * </p>
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public abstract class JamClassBuilder {

  // ========================================================================
  // Variables

  private ElementContext mContext = null;

  // ========================================================================
  // Public methods

  /**
   * This method is called by JAM to initialize this class builder.  Extending
   * classes can override this to perform additional initialization work
   * (just remember to call super.init()!).
   *
   * @param ctx
   */
  public void init(ElementContext ctx) {
    if (mContext != null) {
      throw new IllegalStateException("init called more than once");
    }
    if (ctx == null) throw new IllegalArgumentException("null ctx");
    mContext = ctx;
  }


  // ========================================================================
  // Abstract methods

  /**
   * <p>This is called by JAM when it attempts to load a class.  If the
   * builder has access to an artifact (typically a java source or classfile)
   * that represents the given type, it should call createClassToBuild() to get
   * a new instance of MClass and then return it.  No caching should be
   * performed - if an MClass is going to be returned, it should be a new
   * instance returned by createClassToBuild()</p>
   *
   * <p>If no artififact is available, the builder should just return null,
   * signalling that other JamClassBuilders should attempt to build the
   * class.</p>
   *
   * @param packageName
   * @param className
   * @return
   */
  public abstract MClass build(String packageName, String className);

  // ========================================================================
  // Protected methods

  /**
   * <p>When a JamClassBuilder decides that it is going to be able
   * to respond to a build() request, it must call this method to get an
   * initial instance of MClass to return.</p>
   *
   * @param packageName qualified name of the package that contains the
   * class to create
   * @param className simple name of the class to create.
   * @param importSpecs array of import specs to be used in the class,
   * or null if not known or relevant.  Import specs are only strictly
   * required if the builder is planning on setting any unqualified type
   * references on the class.  The importspec values will be used in
   * determining what is retuned by JClass.getImportedPackages() and
   * getImportedClasses().  Note that an importSpec does not include the
   * word 'import' - e.g. it should only be 'java.util.List' or 'java.util.*'
   */
  protected MClass createClassToBuild(String packageName,
                                      String className,
                                      String[] importSpecs,
                                      JamClassPopulator pop)
  {
    if (mContext == null) throw new IllegalStateException("init not called");
    if (packageName == null) throw new IllegalArgumentException("null pkg");
    if (className == null) throw new IllegalArgumentException("null class");
    if (pop == null) throw new IllegalArgumentException("null pop");
    assertInitialized();
    className = className.replace('.','$');
    ClassImpl out = new ClassImpl(packageName,className,mContext,importSpecs,pop);
    return out;
  }

  /**
   * <p>When a JamClassBuilder decides that it is going to be able
   * to respond to a build() request, it must call this method to get an
   * initial instance of MClass to return.</p>
   *
   * @param packageName qualified name of the package that contains the
   * class to create
   * @param className simple name of the class to create.
   * @param importSpecs array of import specs to be used in the class,
   * or null if not known or relevant.  Import specs are only needed if
   * the builder is planning on setting any unqualified type references
   * on the class.
   */
  protected MClass createClassToBuild(String packageName,
                                      String className,
                                      String[] importSpecs)
  {
    if (mContext == null) throw new IllegalStateException("init not called");
    if (packageName == null) throw new IllegalArgumentException("null pkg");
    if (className == null) throw new IllegalArgumentException("null class");
    assertInitialized();
    className = className.replace('.','$');
    ClassImpl out = new ClassImpl(packageName,className,mContext,importSpecs);
    return out;
  }

  protected JamLogger getLogger() { return mContext; }


  /**
   * Asserts that init() has been called on this class builder.
   */
  protected final void assertInitialized() {
    if (mContext == null) {
      throw new IllegalStateException(this+" not yet initialized.");
    }
  }

}