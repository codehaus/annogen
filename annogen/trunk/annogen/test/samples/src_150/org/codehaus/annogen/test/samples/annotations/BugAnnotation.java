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
package org.codehaus.annogen.test.samples.annotations;

import org.codehaus.annogen.generate.AnnogenInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * FIXME move this to org.codehaus.annogen.test.samples
 *
 * @author Patrick Calahan &lt;email: codehaus-at-bea-dot-com&gt;
 */
@AnnogenInfo(annoBeanClass="org.codehaus.annogen.test.samples.annotations.impl.BugAnnotationImpl")
@Retention(value=RetentionPolicy.RUNTIME)
public @interface BugAnnotation {
    int    id();
    String synopsis();
    //EmployeeAnnotation engineer();
    String enteredBy();
    String date();
}