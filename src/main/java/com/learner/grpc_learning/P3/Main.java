package com.learner.grpc_learning.P3;

import com.learner.grpc_learning.proto.p3.Person;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class Main {

  public static void main(String[] args) throws IOException {

    Person person =
        Person.newBuilder().setAbc("abc").setTransactionAmount(12.4f).setAge(24).setTransactionId(122112121)
            .setBalance(-1).setPrimaryHolder(true).build();
    log.info("{}", person);


    Person.parseFrom(person.toByteArray());
    serialize(person);
    log.info("{}", person.equals(deserialize(person)));

  }

  static void serialize(Person person) throws IOException {
    person.writeTo(Files.newOutputStream(Path.of("src/main/resources/serialize_person.proto")));
  }

  static Person deserialize(Person person) throws IOException {
    return Person.parseFrom(Files.readAllBytes(Path.of("src/main/resources/serialize_person.proto")));
  }

}
