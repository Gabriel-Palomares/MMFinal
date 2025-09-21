public class Jogador {
    private String nome;
    private int numeroDeVitorias;
    private int partidasJogadas;

    public Jogador(String nome) {
        this.nome = nome;
        this.numeroDeVitorias = 0;
        this.partidasJogadas = 0;
    }

    public String getNome() {
        return nome;
    }

    public int getNumeroDeVitorias() {
        return numeroDeVitorias;
    }

    public int getPartidasJogadas() {
        return partidasJogadas;
    }

    public void adicionarVitoria() {
        this.numeroDeVitorias++;
        this.partidasJogadas++;
    }

    public void adicionarDerrota() {
        this.partidasJogadas++;
    }

    public double getPercentualDeVitorias() {
        if (partidasJogadas == 0) {
            return 0.0;
        }
        return (double) numeroDeVitorias / partidasJogadas;
    }
}
