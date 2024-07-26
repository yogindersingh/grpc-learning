package com.learner.grpc_learning.P11.Validator;

import com.learner.grpc_learning.P11.Repository.AccountRepository;
import io.grpc.Status;

import java.util.Optional;

public class RequestValidator {

  public static Optional<Status> validateAccountId(Integer accountId)
  {
    if(accountId>0&& accountId<4)
    {
      return Optional.empty();
    }
    return Optional.of(Status.INVALID_ARGUMENT.withDescription("account should be between 0 to 4"));
  }

  public static Optional<Status> validateAmountIsMultipleOfTen(Integer amount)
  {
    if(amount%10==0)
    {
      return Optional.empty();
    }
    return Optional.of(Status.INVALID_ARGUMENT.withDescription("amount should be multiple of 10"));
  }

  public static Optional<Status> validateAmountIsLessThanBalance(Integer amount,Integer accountId)
  {
    if(AccountRepository.getAccountDetails(accountId)>=amount && amount>0)
    {
      return Optional.empty();
    }

    return Optional.of(Status.FAILED_PRECONDITION.withDescription("withdrawal amount should be less than balance"));
  }

}
