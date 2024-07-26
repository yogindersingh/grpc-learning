package com.learner.grpc_learning.P11.Repository;

import java.util.HashMap;
import java.util.Map;

public class AccountRepository {


  private static final Map<Integer, Integer> map = new HashMap<>(Map.of(1, 10000, 2, 1000, 3, 20000));


  public static Integer getAccountDetails(int accountId) {
    return map.get(accountId);
  }

  public static void updateAccountDetails(int accountId, int amount) {
    map.put(accountId, amount);
  }

}
