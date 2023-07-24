package com.grpc.auba;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.Collections;

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

        List<Player> players5stars = new ArrayList<>();
        List<Player> players4stars = new ArrayList<>();
        List<Player> players3stars = new ArrayList<>();

        for (Player player : request.getPlayersList()) {
            if (player.getSkill().equals(Skill.FOUR_STARS)) {
                players4stars.add(player);
            }
            else if (player.getSkill().equals(Skill.FIVE_STARS)) {
                players5stars.add(player);
            }
            else if (player.getSkill().equals(Skill.THREE_STARS)) {
                players3stars.add(player);
            }
        }

        Collections.shuffle(players5stars);
        Collections.shuffle(players4stars);
        Collections.shuffle(players3stars);


        List<Team> teams = new ArrayList<>();

        Team team1 = Team.newBuilder().build();
        Team team2 = Team.newBuilder().build();
        Team team3 = Team.newBuilder().build();

        teams.add(team1);
        teams.add(team2);
        teams.add(team3);


        // Lista temporária de times a serem formados
        List<Team> tempTeams = new ArrayList<>(teams);

        // Equilibra os jogadores com 5 estrelas entre os times
        distributePlayersEqually(tempTeams, players5stars);

        // Equilibra os jogadores com 4 estrelas entre os times
        distributePlayersEqually(tempTeams, players4stars);

        // Equilibra os jogadores com 3 estrelas entre os times
        distributePlayersEqually(tempTeams, players3stars);

        // Atribui os times da lista temporária à lista final de times
        teams.clear();
        teams.addAll(tempTeams);

        //logic here

        //Create a Response
        CreateMatchDayResponse response = CreateMatchDayResponse.newBuilder()
                .setStatus("Match day successfully created")
                .setMatchId(UUID.randomUUID().toString()) // UUID XXX
                .addAllTeams(teams)
                .build();

        System.out.println(response);

        //Send a response to cliente
        responseObserver.onNext(response);
        responseObserver.onCompleted();





    }
    private void distributePlayersEqually(List<Team> teams, List<Player> players) {
        Collections.shuffle(teams); // Embaralha a lista de times
        for (Player player : players) {
            Team team = teams.get(0); // Seleciona o primeiro time da lista
            teams.remove(0); // Remove o time da lista temporária para que o próximo jogador seja adicionado a outro time
            team = team.toBuilder().addPlayers(player).build(); // Adiciona o jogador ao time
            teams.add(team); // Adiciona o time de volta à lista temporária
        }
    }

}
