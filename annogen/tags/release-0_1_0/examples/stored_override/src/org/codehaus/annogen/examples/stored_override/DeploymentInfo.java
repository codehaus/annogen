package org.codehaus.annogen.examples.stored_override;

import org.codehaus.annogen.generate.AnnogenInfo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * This is an JSR 175 annotation type that developers use on the Beans
 * they write.  They use it to set the cacheSize that should be used when
 * the Bean is deployed.  Unfortunately, sometimes the value they specify
 * isn't sufficient for all deployments...
 *
 * Also, note the AnnogenInfo - that is required to tell the AnnogenTask
 * what AnnoBean it should code generate for you.
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
@AnnogenInfo(
  annoBeanClass = "org.codehaus.annogen.examples.stored_override.DeploymentInfoAnnoBean"
)

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DeploymentInfo {

  public int cacheSize();

}
