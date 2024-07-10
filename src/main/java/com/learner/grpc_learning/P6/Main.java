package com.learner.grpc_learning.P6;

import com.learner.grpc_learning.proto.p6.Credentials;
import com.learner.grpc_learning.proto.p6.Email;
import com.learner.grpc_learning.proto.p6.Phone;

public class Main {

  public static void main(String[] args) {
    Email email= Email.newBuilder().setAddress("yogi1223@gmail.com").setPassword("password").build();
    Phone phone= Phone.newBuilder().setCode(121212).setNumber(1223).build();

    login(Credentials.newBuilder().setEmail(email).build());
    login(Credentials.newBuilder().setPhone(phone).build());

    //last one will be taken
    login(Credentials.newBuilder().setEmail(email).setPhone(phone).build());
  }

  public static void login(Credentials credentials) {
    switch (credentials.getLoginTypeCase())
    {
      case EMAIL -> System.out.println("email : "+credentials.getEmail());
      case PHONE -> System.out.println("phone : "+credentials.getPhone());
    }
  }

}
