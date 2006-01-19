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
package org.codehaus.annogen.view.internal.jam;

import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.ProgramElementDoc;
import org.codehaus.annogen.override.AnnoBean;
import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.annogen.view.internal.IndigenousAnnoExtractor;
import org.codehaus.annogen.view.internal.NullIAE;
import org.codehaus.annogen.view.internal.javadoc.JavadocAnnogenTigerDelegate;
import org.codehaus.annogen.view.internal.javadoc.ParameterJavadocIAE;
import org.codehaus.annogen.view.internal.javadoc.ProgramElementJavadocIAE;
import org.codehaus.annogen.view.internal.reflect.ReflectAnnogenTigerDelegate;
import org.codehaus.annogen.view.internal.reflect.ReflectIAE;
import org.codehaus.jam.JAnnotatedElement;
import org.codehaus.jam.JAnnotation;
import org.codehaus.jam.JAnnotationValue;
import org.codehaus.jam.JInvokable;
import org.codehaus.jam.JParameter;
import org.codehaus.jam.JProperty;
import org.codehaus.jam.provider.JamLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public final class JamIAE implements IndigenousAnnoExtractor {

  // ========================================================================
  // Factory methods

  /**
   * This factory method tries to dig into the JAM element's artifact
   * (i.e. the real object that it represents, e.g. a java.lang.Class)
   * and delegate to an appropriate IAE for that artifact.  If there
   * is no artifact, or the artifact is unknown, then an instance of
   * JamIAE will be returned, which operates on JAM's untyped API
   * for accessing annotations.  This is less efficient but is the only
   * option in the absence of something we know about (javadoc or reflection).
   */
  public static IndigenousAnnoExtractor create(JAnnotatedElement element,
                                               JamLogger logger,
                                               ReflectAnnogenTigerDelegate rtiger,
                                               JavadocAnnogenTigerDelegate jtiger) {
    {
      if (element == null) throw new IllegalArgumentException("null element");
      Object artifact = element.getArtifact();
      if (artifact == null) return createForUnknownArtifact(element,logger);
      if (element instanceof JProperty) {
        throw new IllegalStateException("NYI");
      } else if (element instanceof JParameter) {
        JInvokable parent = (JInvokable)element.getParent();
        Object parentArt = parent.getArtifact();
        int num = getParameterNumber((JParameter)element);
        if (parentArt instanceof ExecutableMemberDoc) {
          return ParameterJavadocIAE.create((ExecutableMemberDoc)parentArt,
                                            num,jtiger);
        } else if (parentArt instanceof Method) {
          return ReflectIAE.create((Method)parentArt,num,rtiger);
        } else if (parentArt instanceof Constructor) {
          return ReflectIAE.create((Constructor)parentArt,num,rtiger);
        } else {
          return createForUnknownArtifact(element,logger);
        }
      } else if (artifact instanceof ProgramElementDoc) {
        return ProgramElementJavadocIAE.create((ProgramElementDoc)artifact,jtiger);
      } else if (artifact instanceof Class) {
        return ReflectIAE.create((Class)artifact,rtiger);
      } else if (artifact instanceof Package) {
        return ReflectIAE.create((Package)artifact,rtiger);
      } else if (artifact instanceof Method) {
        return ReflectIAE.create((Method)artifact,rtiger);
      } else if (artifact instanceof Constructor) {
        return ReflectIAE.create((Constructor)artifact,rtiger);
      } else if (artifact instanceof Field) {
        return ReflectIAE.create((Field)artifact,rtiger);
      } else {
        return createForUnknownArtifact(element,logger);
      }
    }
  }

  private static IndigenousAnnoExtractor
    createForUnknownArtifact(JAnnotatedElement element, JamLogger logger) {
    JAnnotation[] anns = element.getAnnotations();
    if (anns == null || anns.length == 0) {
      // if there are no annotations on it anyway, let's not bother with it
      return NullIAE.getInstance();
    }
    return new JamIAE(element,logger);
  }

  // ========================================================================
  // Variables

  private JAnnotatedElement mElement;
  private JamLogger mLogger;

  // ========================================================================
  // Constructors

  private JamIAE(JAnnotatedElement element, JamLogger logger) {
    mElement = element;
    mLogger = logger;
  }

  // ========================================================================
  // IAE implementation

  public boolean extractIndigenousAnnotations(AnnoBeanSet out) {
    JAnnotation[] anns = mElement.getAnnotations();
    if (anns == null || anns.length == 0) return false;
    for(int i=0; i<anns.length; i++) {
      Class annoClass = getAnnotationTypeClass(anns[i]);
      if (annoClass == null) continue;
      AnnoBean bean = out.findOrCreateBeanFor(annoClass);
      if (bean == null) continue;
      populate(anns[i],bean);
    }
    return true;
  }

  // ========================================================================
  // Private methods

  private void populate(JAnnotation src, AnnoBean dest) {
    JAnnotationValue[] values = src.getValues();
    if (values == null || values.length == 0) return;
    for(int i=0; i<values.length; i++) {
      //FIXME this is way busted for nested complex types
      dest.setValue(values[i].getName(),values[i].getValue());
    }
  }

  //REVIEW this is pretty fragile, but do we have a choice?
  //We really need to know from JAM whether the JAnnotation is a 175 or
  //a javadoc tag.  Maybe we need separate abstractions.
  private Class getAnnotationTypeClass(JAnnotation ann) {
    try {
      return Class.forName(ann.getQualifiedName());
    } catch(ClassNotFoundException cnfe) {
      // mLogger.warning(cnfe);
    }
    return null;
  }

  /**
   * REVIEW is it worth doing a more efficient implementation here?  We
   * could add getNumber() to JParameter but it just feels wrong.
   */
  public static int getParameterNumber(JParameter param) {
    JParameter[] params = ((JInvokable)param.getParent()).getParameters();
    for(int i=0; i<params.length; i++) if (param == params[i]) return i;
    throw new IllegalStateException();
  }
}