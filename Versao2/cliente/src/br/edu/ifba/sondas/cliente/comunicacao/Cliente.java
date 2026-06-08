package br.edu.ifba.sondas.cliente.comunicacao;

import br.edu.ifba.sondas.cliente.sensoriamento.Sensoriamento;

public interface Cliente<Monitorado, Leitura> {

    public void configurar(Monitorado monitorado, Sensoriamento<Leitura> sensoriamento) throws Exception;

    public boolean ocorreuDiferencaSignificativa(Leitura leituraAtual, Leitura ultimaLeitura,
            double limiarTemperatura, double limiarOxigenio);

    public Resultado enviar(Leitura leitura) throws Exception;

    public Resultado enviar(double probabilidade) throws Exception;

}
