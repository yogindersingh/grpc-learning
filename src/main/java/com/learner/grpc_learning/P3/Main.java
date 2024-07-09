package com.learner.grpc_learning.P3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.learner.grpc_learning.proto.p3.Person;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class Main {

  static ObjectMapper objectMapper=new ObjectMapper();

  public static void main(String[] args) throws IOException {

    Person person =
        Person.newBuilder().setAbc("abc").setTransactionAmount(12.4f).setAge(24).setTransactionId(122112121)
            .setBalance(-1).setPrimaryHolder(true).build();
    log.info("{}", person);

    Person.parseFrom(person.toByteArray());
    serialize(person);
    log.info("{}", person.equals(deserialize(person)));

    JsonPerson jsonPerson=new JsonPerson("abc",24,-1,122112121,12.4f,true);

    for (int i=0;i<5;i++)
    {
      test(i,()-> {
        try {
          protoSerializeDeserialize(person);
        } catch (InvalidProtocolBufferException e) {
          throw new RuntimeException(e);
        }
      },"proto");
      test(i,()-> {
        try {
          jsonSerializeDeserialize(jsonPerson);
        } catch (InvalidProtocolBufferException e) {
          throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      },"json");
    }
  }

  static void serialize(Person person) throws IOException {
    person.writeTo(Files.newOutputStream(Path.of("src/main/resources/serialize_person.proto")));
  }

  static Person deserialize(Person person) throws IOException {
    return Person.parseFrom(Files.readAllBytes(Path.of("src/main/resources/serialize_person.proto")));
  }


  static void protoSerializeDeserialize(Person person) throws InvalidProtocolBufferException {
    byte[] byteArray = person.toByteArray();
    Person.parseFrom(byteArray);
  }

  static void jsonSerializeDeserialize(JsonPerson person) throws IOException {
    byte[] byteArray = objectMapper.writeValueAsBytes(person);
    objectMapper.readValue(byteArray,JsonPerson.class);
  }

  static void test(int noOfTests, Runnable runnable,String type)
  {
    System.out.println("noOfTests: "+noOfTests + " type: "+type);
    long startTime = System.currentTimeMillis();
    for (int i=0;i<1000000;i++)
    {
      runnable.run();
    }
    System.out.println("total Time:"+(System.currentTimeMillis()-startTime));
  }

}
