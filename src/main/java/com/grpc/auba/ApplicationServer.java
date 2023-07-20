package com.grpc.auba;

import com.google.protobuf.Timestamp;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.logging.Logger;

public class ApplicationServer {
    public static final Logger logger = Logger.getLogger(ApplicationServer.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
         Server server = ServerBuilder
                .forPort(50051)
                .addService(new CreateMatchDayEndpoint())
                .build();

         server.start();
         logger.info("Server started on " + server.getPort());

         server.awaitTermination();

    }
}

class CreateMatchDayEndpoint extends OrganizaPeladaServiceGrpc.OrganizaPeladaServiceImplBase {
    @Override
    public void register(CreateMatchDayRequest request, StreamObserver<CreateMatchDayResponse> responseObserver) {

        for (Player player : request.getPlayersList()) {
            System.out.println("Player: " + player.getName() + " (Skill: " + player.getSkill() + ")");
        }

        //Create a Response
        CreateMatchDayResponse response = CreateMatchDayResponse.newBuilder()
                .setStatus("Match day successfully created")
                .setMatchId("12345") // UUID XXX
                .addTeams(Team.newBuilder()
                        .addPlayers(Player.newBuilder().setName("Jogador 1").setSkill(Skill.FOUR_STARS).build())
                        .addPlayers(Player.newBuilder().setName("Jogador 2").setSkill(Skill.THREE_STARS).build())
                        .build()
                )
                .addTeams(Team.newBuilder()
                        .addPlayers(Player.newBuilder().setName("Jogador 3").setSkill(Skill.FOUR_STARS).build())
                        .addPlayers(Player.newBuilder().setName("Jogador 4").setSkill(Skill.THREE_STARS).build())
                        .build()
                )
                .build();

        //Send a response to cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }
}
