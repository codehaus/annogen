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
package org.codehaus.jam.test.samples.jsr175;


@EmployeeGroupAnnotation(
  employees={
  @EmployeeAnnotation(
    firstName       = "Boog",
    lastName        = "Powell",
    address = @AddressAnnotation(street="123 shady lane", city="Cooperstown",zip=123456),
    specialDigits={8,6,7,5,3,0,9},
    active=Constants.Bool.TRUE,
    aka={"larry","joe","booooooooooog"}),


  @EmployeeAnnotation(
    firstName       = "Takako",
    lastName        = "Minekawa",
    address = @AddressAnnotation(street="456 Shinjuku", city="Tokyo",zip=5478),
    specialDigits={8,6,7,5,3,0,9},
    active=Constants.Bool.FALSE,
    aka={"destron","fantastic"})

  }
)
@EmployeeAnnotation(
  firstName       = "Joe",
  lastName        = "Torre",
  address = @AddressAnnotation(street="567 street", city="Cooperstown",zip=123456),
  specialDigits={8,6,7,5,3,0,9},
  active=Constants.Bool.TRUE,
  aka={"joe t","joe","bleh"}
)
public class NestedAnnotatedClass {




  public void someMethod(
    @EmployeeAnnotation( firstName="Joe",
                         lastName="Louis",
                         address=@AddressAnnotation(street="parameter annotation!!", city="Chicago",zip=4343),
                         specialDigits={5,3},
                         active=Constants.Bool.TRUE,
                         aka={"joe t","joe","bleh"}
    )
    int joeLouisParameter)
  {

  }

}
