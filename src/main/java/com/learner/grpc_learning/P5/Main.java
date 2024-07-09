package com.learner.grpc_learning.P5;

import com.learner.grpc_learning.proto.p5.BodyStyle;
import com.learner.grpc_learning.proto.p5.Book;
import com.learner.grpc_learning.proto.p5.Car;
import com.learner.grpc_learning.proto.p5.Library;
import com.learner.grpc_learning.proto.p5.ShowRoom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

  public static void main(String[] args) {
    Car car=Car.newBuilder().setCarName("Honda").setManufactureYear(2015).setBodystyle(BodyStyle.SEDAN).build();
    Car car1=car.toBuilder().setCarName("Mahindra").setBodystyle(BodyStyle.SUV).build();
    ShowRoom showRoom= ShowRoom.newBuilder().addAllCars(List.of(car,car1)).setName("Singh Automobiles").build();

    System.out.println("showRoom:"+showRoom);

    Book book=Book.newBuilder().setName("bookName1").build();
    Book book1=Book.newBuilder().setName("bookName2").build();
    Book book2=Book.newBuilder().setName("bookName3").build();
    Book book3=Book.newBuilder().setName("bookName4").build();
    Book book4=Book.newBuilder().setName("bookName5").build();

    Map<Integer,Book> bookMap=new HashMap<>();
    bookMap.put(1,book);
    bookMap.put(2,book1);
    bookMap.put(3,book2);
    bookMap.put(4,book3);
    bookMap.put(5,book4);
    Library library=Library.newBuilder().setName("libraryName").putAllRackBookMap(bookMap).build();

    System.out.println("library:"+library);
  }
}
