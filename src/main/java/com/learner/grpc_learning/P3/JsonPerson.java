package com.learner.grpc_learning.P3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JsonPerson {
  String abc;
  int age;
  int balance;
  int transaction_id;
  float transaction_amount;
  Boolean primary_holder;
}
