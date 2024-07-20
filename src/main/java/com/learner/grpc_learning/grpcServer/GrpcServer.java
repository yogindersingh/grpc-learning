package com.learner.grpc_learning.grpcServer;

import com.learner.grpc_learning.P9.BankServiceImpl;
import com.learner.grpc_learning.P9.TransferServiceImple;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import io.grpc.ServiceDescriptor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class GrpcServer {

  public static void main(String[] args) throws InterruptedException {
    new GrpcServer(new BankServiceImpl(),new TransferServiceImple()).start().awaitTermination();
  }

  public static Server server;

  GrpcServer(BindableService... bindableServices) {
    createServer(6565,bindableServices);
  }

  GrpcServer(Integer port, BindableService... bindableServices) {
      createServer(port,bindableServices);
  }

  public static void createServer(Integer port, BindableService... bindableServices) {
    ServerBuilder<?> serverBuilder = ServerBuilder.forPort(port);
    Arrays.stream(bindableServices).forEach(serverBuilder::addService);
    server = serverBuilder.build();
  }

  public GrpcServer start() {
    log.info("Server started");
    Set<String> services = server.getServices().stream().map(ServerServiceDefinition::getServiceDescriptor)
        .map(ServiceDescriptor::getName).collect(Collectors.toSet());
    log.info("Services: " + services);
    try {
      server.start();
      return this;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }

  public GrpcServer awaitTermination() throws InterruptedException {
    server.awaitTermination();
    return this;
  }

}
