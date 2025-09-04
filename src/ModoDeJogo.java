public class ModoDeJogo {
    private String nome;
    private int jogadoresPorEquipe;

    public ModoDeJogo(String nome, int jogadoresPorEquipe) {
        this.nome = nome;
        this.jogadoresPorEquipe = jogadoresPorEquipe;
    }

    public String getNome() {
        return nome;
    }

    public int getJogadoresPorEquipe() {
        return jogadoresPorEquipe;
    }
}
