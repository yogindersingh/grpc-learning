package com.learner.grpc_learning.P11.Validator;

import com.learner.grpc_learning.P11.Repository.AccountRepository;
import com.learner.grpc_learning.proto.p11.ErrorMessage;
import com.learner.grpc_learning.proto.p11.ValidationCode;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;

import java.util.Optional;

public class RequestValidator {

  public static Optional<StatusRuntimeException> validateAccountId(Integer accountId) {
    if (accountId > 0 && accountId < 4) {
      return Optional.empty();
    }
    Metadata metadata = metadata(
        ErrorMessage.newBuilder().setErrorCode(ValidationCode.INVALID_ACCOUNT).build(), "account should be between 0 " +
            "to 4");
    return Optional.of(Status.INVALID_ARGUMENT.asRuntimeException(metadata));
  }

  public static Optional<StatusRuntimeException> validateAmountIsMultipleOfTen(Integer amount) {
    if (amount % 10 == 0) {
      return Optional.empty();
    }
    Metadata metadata = metadata(
        ErrorMessage.newBuilder().setErrorCode(ValidationCode.INVALID_AMOUNT).build(),
        "amount should be multiple of 10");
    return Optional.of(Status.INVALID_ARGUMENT.asRuntimeException(metadata));
  }

  public static Optional<StatusRuntimeException> validateAmountIsLessThanBalance(Integer amount, Integer accountId) {
    if (AccountRepository.getAccountDetails(accountId) >= amount && amount > 0) {
      return Optional.empty();
    }
    Metadata metadata = metadata(
        ErrorMessage.newBuilder().setErrorCode(ValidationCode.INVALID_AMOUNT).build(),
        "withdrawal amount should be less than balance");
    return Optional.of(Status.FAILED_PRECONDITION.asRuntimeException(metadata));
  }

  private static Metadata metadata(ErrorMessage errorMessage, String message) {
    Metadata metadata = new Metadata();
    Metadata.Key<ErrorMessage> key = ProtoUtils.keyForProto(ErrorMessage.getDefaultInstance());
    metadata.put(key, errorMessage);
    Metadata.Key<String> stringKey = Metadata.Key.of("description", Metadata.ASCII_STRING_MARSHALLER);
    metadata.put(stringKey, message);
    return metadata;
  }

}
