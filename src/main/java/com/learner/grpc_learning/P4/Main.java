package com.learner.grpc_learning.P4;

import com.learner.grpc_learning.proto.p4.Address;
import com.learner.grpc_learning.proto.p4.School;
import com.learner.grpc_learning.proto.p4.Student;

public class Main {
  public static void main(String[] args) {
    Address builderForValue = Address.newBuilder().setState("karnataka").setStreet(
        "Murugeshpalya").build();
    School school=
        School.newBuilder().setSchoolName("DAV").setAddress(builderForValue).build();
    Student student=
        Student.newBuilder().setStudentName("Kamal").setAddress(builderForValue.toBuilder().setStreet("BheemaDevi").build()).build();

    System.out.println("address:"+builderForValue);
    System.out.println("student:"+student);
    System.out.println("school:"+school);
  }
}
