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
package org.codehaus.annogen.view.internal.reflect;

import org.codehaus.annogen.override.AnnoBeanSet;
import org.codehaus.annogen.view.internal.IndigenousAnnoExtractor;
import org.codehaus.annogen.view.internal.NullIAE;
import org.codehaus.annogen.view.internal.IndigenousAnnoExtractor;
import org.codehaus.annogen.view.internal.NullIAE;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Just a holder for static factory methods for getting impls of
 * IndigenousAnnoExtractor that work with reflect abstractions.
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
public abstract class ReflectIAE implements IndigenousAnnoExtractor {

  // ========================================================================
  // Factory methods


  /**
   * java.lang.Package
   */
  public static IndigenousAnnoExtractor create(final Package x,
                                               final ReflectAnnogenTigerDelegate tiger) {
    if (tiger == null) return NullIAE.getInstance();
    return new IndigenousAnnoExtractor() {
      public boolean extractIndigenousAnnotations(AnnoBeanSet out) {
        return tiger.extractAnnotations(out,x);
      }
    };
  }

  /**
   * java.lang.reflect.Class
   */
  public static IndigenousAnnoExtractor create(final Class x,
                                               final ReflectAnnogenTigerDelegate tiger) {
    if (tiger == null) return NullIAE.getInstance();
    return new IndigenousAnnoExtractor() {
      public boolean extractIndigenousAnnotations(AnnoBeanSet out) {
        return tiger.extractAnnotations(out,x);
      }
    };
  }

  /**
   * java.lang.reflect.Method
   */
  public static IndigenousAnnoExtractor create(final Method x,
                                               final ReflectAnnogenTigerDelegate tiger) {
    if (tiger == null) return NullIAE.getInstance();
    return new IndigenousAnnoExtractor() {
      public boolean extractIndigenousAnnotations(AnnoBeanSet out) {
        return tiger.extractAnnotations(out,x);
      }
    };
  }

  /**
   * java.lang.reflect.Constructor
   */
  public static IndigenousAnnoExtractor create(final Constructor x,
                                               final ReflectAnnogenTigerDelegate tiger) {
    if (tiger == null) return NullIAE.getInstance();
    return new IndigenousAnnoExtractor() {
      public boolean extractIndigenousAnnotations(AnnoBeanSet out) {
        return tiger.extractAnnotations(out,x);
      }
    };
  }

  /**
   * java.lang.reflect.Field
   */
  public static IndigenousAnnoExtractor create(final Field x,
                                               final ReflectAnnogenTigerDelegate tiger) {
    if (tiger == null) return NullIAE.getInstance();
    return new IndigenousAnnoExtractor() {
      public boolean extractIndigenousAnnotations(AnnoBeanSet out) {
        return tiger.extractAnnotations(out,x);
      }
    };
  }

  /**
   * Parameter on java.lang.reflect.Constructor
   */
  public static IndigenousAnnoExtractor create(final Constructor x,
                                               final int n,
                                               final ReflectAnnogenTigerDelegate tiger) {
    if (tiger == null) return NullIAE.getInstance();
    return new IndigenousAnnoExtractor() {
      public boolean extractIndigenousAnnotations(AnnoBeanSet out) {
        return tiger.extractAnnotations(out,x,n);
      }
    };
  }

  /**
   * Parameter on java.lang.reflect.Method
   */
  public static IndigenousAnnoExtractor create(final Method x,
                                               final int n,
                                               final ReflectAnnogenTigerDelegate tiger) {
    if (tiger == null) return NullIAE.getInstance();
    return new IndigenousAnnoExtractor() {
      public boolean extractIndigenousAnnotations(AnnoBeanSet out) {
        return tiger.extractAnnotations(out,x,n);
      }
    };
  }
}
