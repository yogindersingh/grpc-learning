# gRPC Learning

A progressive, hands-on project for learning gRPC in Java. Covers everything from proto3 basics through all four RPC communication patterns, interceptors, TLS, flow control, and call credentials.

## Tech Stack

- Java 17
- gRPC 1.60.0 (Netty transport)
- Protobuf 3.24.0
- Spring Boot 3.3.1
- Maven

## Project Structure

```
src/main/
├── proto/
│   ├── P1–P8/          # Proto3 fundamentals (messages, types, imports, wrappers)
│   ├── P9/             # Banking service — all four RPC types
│   ├── P10/            # Flow control service
│   └── P11/            # Validation service (interceptors, credentials)
├── java/com/learner/grpc_learning/
│   ├── P1–P6/          # Proto concepts demos (serialization, nested msgs, oneof, etc.)
│   ├── P9/             # BankServiceImpl, TransferServiceImpl
│   ├── P10/            # FlowControlImpl
│   ├── P11/            # Validation, interceptors, credentials, enhanced client
│   ├── grpcServer/     # GrpcServer — server bootstrap, service registration
│   ├── grpcclient/     # GrpcClient — all RPC type demos
│   └── responseObservers/ # Shared StreamObserver implementations
└── resources/
    └── KeyStores/      # JKS keystore & truststore for TLS
```

## Modules at a Glance

| Module | Proto File | Concepts Demonstrated |
|--------|-----------|----------------------|
| P1 | `person.proto` | Basic message definition, builder pattern |
| P2 | `person.proto` | `java_multiple_files`, comments |
| P3 | `person.proto` | Proto vs JSON serialization benchmark |
| P4 | `CombiningMultipleMessage.proto` | Nested/composite messages |
| P5 | `carAndLibrary.proto` | Repeated fields, maps, enums |
| P6 | `Oneof.proto` | `oneof` fields (mutually exclusive) |
| P7 | `common.proto` | Proto imports, cross-package message reuse |
| P8 | `wrapperType.proto` | Google wrapper types (nullable scalars) |
| P9 | `bankService.proto` | All four RPC types (unary, server streaming, client streaming, bidirectional) |
| P10 | `FlowControl.proto` | Flow control and backpressure in bidirectional streaming |
| P11 | `Input-Validation.proto` | Input validation, error metadata, interceptors, compression, deadlines, call credentials |

## RPC Patterns (P9)

The `BankService` and `TransferService` demonstrate all four gRPC communication patterns:

| RPC | Method | Pattern |
|-----|--------|---------|
| Unary | `getAccountDetails` | Single request → single response |
| Unary (Empty) | `getAccounts` | `google.protobuf.Empty` request → all accounts |
| Server Streaming | `getWithdrawalAmount` | Single request → stream of responses |
| Client Streaming | `saveAmount` | Stream of deposit requests → single balance response |
| Bidirectional Streaming | `transferMoney` | Simultaneous request/response streams |

## Advanced Topics (P11)

- **Input Validation** — `RequestValidator` returns `Optional<StatusRuntimeException>` with `INVALID_ARGUMENT` / `FAILED_PRECONDITION` status codes and custom error proto attached as metadata
- **Server Interceptor** — `ServerResponseObserver` inspects incoming metadata (e.g. API key checks)
- **Client Interceptor** — `ClientInterceptorImpl` injects a 10-second deadline if none is set
- **Compression** — `ServerCallStreamObserver.setCompression("gzip")`
- **Deadlines** — `Deadline.after(10, TimeUnit.SECONDS)` with client-side cancellation awareness via `Context.current().isCancelled()`
- **Call Credentials** — `UserTokenCallCredentials` extends `CallCredentials`, applies `Authorization` metadata asynchronously

## Flow Control (P10)

`FlowControlImpl` demonstrates backpressure: the client sends batch size requests and the server emits exactly that many response values per round, stopping at a total of 100 values.

## TLS

JKS keystores are provided in `src/main/resources/KeyStores/`. TLS setup using `NettyServerBuilder` / `NettyChannelBuilder` with `SslContext` is implemented but commented out in `GrpcServer.java` and `GrpcClient.java`.

## Running

```bash
mvn compile                    # generates protobuf/gRPC stubs from .proto files
mvn spring-boot:run            # starts the Spring Boot app (REST on 8080)
```

The gRPC server starts on port **6565** (see `GrpcServer.java`). Run it alongside the client demos to exercise each RPC pattern.

## Reference

- [gRPC Status Codes](https://github.com/grpc/grpc/blob/master/doc/statuscodes.md)
- [google.rpc.Code proto](https://github.com/googleapis/googleapis/blob/master/google/rpc/code.proto)

## Proto Scalar Types

| .proto Type | Java Type |
|:-----------:|:---------:|
| double | double |
| float | float |
| int32 | int |
| int64 | long |
| uint32 | int |
| uint64 | long |
| sint32 | int |
| sint64 | long |
| fixed32 | int |
| fixed64 | long |
| sfixed32 | int |
| sfixed64 | long |
| bool | boolean |
| string | String |
| bytes | ByteString |
