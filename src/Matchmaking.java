import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.InputMismatchException;

public class Matchmaking {
    public List<Equipe> criarPartidaBalanceada(List<Jogador> jogadoresDisponiveis, ModoDeJogo modoDeJogo) {
        if (!validarJogadores(jogadoresDisponiveis, modoDeJogo.getJogadoresPorEquipe())) {
            return null;
        }

        // 1. Calcular a média de vitórias e partidas jogadas para a divisão
        double mediaVitorias = 0;
        double mediaPartidas = 0;

        if (!jogadoresDisponiveis.isEmpty()) {
            for (Jogador jogador : jogadoresDisponiveis) {
                mediaVitorias += jogador.getPercentualDeVitorias();
                mediaPartidas += jogador.getPartidasJogadas();
            }
            mediaVitorias /= jogadoresDisponiveis.size();
            mediaPartidas /= jogadoresDisponiveis.size();
        }

        // 2. Separar jogadores experientes de novatos
        List<Jogador> jogadoresExperientes = new ArrayList<>();
        List<Jogador> jogadoresNovatos = new ArrayList<>();

        for (Jogador jogador : jogadoresDisponiveis) {
            if (jogador.getPercentualDeVitorias() > mediaVitorias && jogador.getPartidasJogadas() > mediaPartidas) {
                jogadoresExperientes.add(jogador);
            } else {
                jogadoresNovatos.add(jogador);
            }
        }

        // 3. Ordenar jogadores experientes (do melhor para o pior) e embaralhar os novatos
        Collections.sort(jogadoresExperientes, Comparator.comparingDouble(Jogador::getPercentualDeVitorias).reversed());
        Collections.shuffle(jogadoresNovatos);

        // 4. Criar as equipes
        int numeroDeEquipes = jogadoresDisponiveis.size() / modoDeJogo.getJogadoresPorEquipe();
        List<Equipe> equipes = new ArrayList<>();
        for (int i = 0; i < numeroDeEquipes; i++) {
            equipes.add(new Equipe());
        }

        // 5. Distribuir jogadores experientes de forma cíclica
        distribuirJogadoresExperientes(equipes, jogadoresExperientes);

        // 6. Distribuir jogadores novatos para a equipe com o menor percentual médio
        distribuirJogadoresNovatos(equipes, jogadoresNovatos);

        System.out.println("\nPartida criada com sucesso!");
        for (Equipe equipe : equipes) {
            equipe.exibirMembros();
        }
        return equipes;
    }

    private void distribuirJogadoresExperientes(List<Equipe> equipes, List<Jogador> jogadoresExperientes) {
        int indiceEquipe = 0;
        for (Jogador jogador : jogadoresExperientes) {
            equipes.get(indiceEquipe).adicionarMembro(jogador);
            indiceEquipe = (indiceEquipe + 1) % equipes.size();
        }
    }

    private void distribuirJogadoresNovatos(List<Equipe> equipes, List<Jogador> jogadoresNovatos) {
        for (Jogador jogador : jogadoresNovatos) {
            Equipe equipeMaisFraca = encontrarEquipeMaisFraca(equipes);
            equipeMaisFraca.adicionarMembro(jogador);
        }
    }

    private Equipe encontrarEquipeMaisFraca(List<Equipe> equipes) {
        Equipe equipeMaisFraca = null;
        double menorPercentual = Double.MAX_VALUE;

        for (Equipe equipe : equipes) {
            double percentualAtual = equipe.getPercentualMedioDeVitorias();
            if (percentualAtual < menorPercentual) {
                menorPercentual = percentualAtual;
                equipeMaisFraca = equipe;
            }
        }
        return equipeMaisFraca;
    }

    public boolean validarJogadores(List<Jogador> jogadoresDisponiveis, int jogadoresPorEquipe) {
        if (jogadoresDisponiveis.size() < (jogadoresPorEquipe * 2)) {
            System.out.println("Número insuficiente de jogadores para formar pelo menos duas equipes.");
            return false;
        }

        if (jogadoresDisponiveis.size() % jogadoresPorEquipe != 0) {
            System.out.println("Número de jogadores incompatível com o tamanho da equipe.");
            return false;
        }

        return true;
    }
}