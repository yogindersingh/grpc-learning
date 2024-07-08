package com.learner.grpc_learning.P1;

import com.learner.grpc_learning.proto.p1.PersonOuterClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

  public static void main(String[] args) {

    PersonOuterClass.Person person= createObj();
    log.info(person.toString());

    PersonOuterClass.Person person2=createObj();
    log.info(person2.toString());

    PersonOuterClass.Person person1 = person.toBuilder().setAbc("new abc").build();
    log.info(person1.toString());

    log.info("{}",person2.equals(person));
    log.info("{}",person==person2);

    log.info("{}",person1.equals(person));
    log.info("{}",person1==person);


  }

  private static PersonOuterClass.Person createObj() {
    return PersonOuterClass.Person.newBuilder().setAbc("abc").build();
  }

}
