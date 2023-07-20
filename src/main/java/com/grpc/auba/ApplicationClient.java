package com.grpc.auba;

import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;

public class ApplicationClient {
    public static void main(String[] args) {

        //Create a communication channel with the gRPC Server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        //Create the client gRPC to service OrganizaPeladaService
        OrganizaPeladaServiceGrpc.OrganizaPeladaServiceBlockingStub client = OrganizaPeladaServiceGrpc.newBlockingStub(channel);


        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(Instant.now().getEpochSecond())
                .setNanos(Instant.now().getNano())
                .build();


        CreateMatchDayRequest request = CreateMatchDayRequest.newBuilder()
                .setTime(timestamp)
                .addPlayers(Player.newBuilder().setName("Thiago").setSkill(Skill.FOUR_STARS).build())
                .addPlayers(Player.newBuilder().setName("Messi").setSkill(Skill.FIVE_STARS).build())
                .addPlayers(Player.newBuilder().setName("Ronaldo").setSkill(Skill.FIVE_STARS).build())
                .addPlayers(Player.newBuilder().setName("Marco Reus").setSkill(Skill.FOUR_STARS).build())
                .addPlayers(Player.newBuilder().setName("Torres").setSkill(Skill.THREE_STARS).build())
                .addPlayers(Player.newBuilder().setName("Aguero").setSkill(Skill.FOUR_STARS).build())
                .addPlayers(Player.newBuilder().setName("Lamine Yamal").setSkill(Skill.THREE_STARS).build())
                .addPlayers(Player.newBuilder().setName("Neymar").setSkill(Skill.FIVE_STARS).build())
                .addPlayers(Player.newBuilder().setName("Modric").setSkill(Skill.FOUR_STARS).build())
                .addPlayers(Player.newBuilder().setName("Sergi Roberto").setSkill(Skill.THREE_STARS).build())
                .addPlayers(Player.newBuilder().setName("Semedo").setSkill(Skill.THREE_STARS).build())
                .addPlayers(Player.newBuilder().setName("Salah").setSkill(Skill.FOUR_STARS).build())
                .addPlayers(Player.newBuilder().setName("Roberto Carlos").setSkill(Skill.FOUR_STARS).build())
                .addPlayers(Player.newBuilder().setName("Haaland").setSkill(Skill.FOUR_STARS).build())
                .addPlayers(Player.newBuilder().setName("Van Persie").setSkill(Skill.FOUR_STARS).build())
                .build();


        //System.out.println(request);

        //If the request is saved in a file
        try {
            request.writeTo(new FileOutputStream("new-match-request.bin"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CreateMatchDayResponse response = client.register(request);
        System.out.println(response);
        channel.shutdown();

    }
}
