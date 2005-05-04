package org.codehaus.jam.test.samples.jsr175;

@LotsaDefaultsAnnotation()

@RFEAnnotation_150(
    id       = 4561414,
    synopsis = "Balance the federal budget"
)

public abstract class AnnotatedClass {

  @InnerAnnotation.GoofyAnno(foo = 5, bar = 6)
  public int someMember;

  public abstract void setFoo(int value);

  public abstract int getFoo();

}
