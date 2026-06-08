package br.edu.ifba.sondas.servidor.operacoes;

public interface Operacoes<Monitorado, Leitura> {

    public void gravar(Monitorado monitorado, Leitura leitura);

    public void gravar(Monitorado monitorado, double probabilidade);

    public double obterTotalProbabilidades();

}
