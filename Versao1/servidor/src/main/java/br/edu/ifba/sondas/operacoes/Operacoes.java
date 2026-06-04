package br.edu.ifba.sondas.operacoes;

import java.util.List;
import java.util.Map;
import br.edu.ifba.sondas.ordenacao.TipoOrdenacao;

public interface Operacoes<Monitorado, Leitura> {

    // d.1
    public void imprimir(List<Monitorado> monitorados);

    // d.2
    public void imprimir(Map<Monitorado, List<Leitura>> leituras);

    // d.3
    public Map<Monitorado, List<Leitura>> ordenar(Map<Monitorado, List<Leitura>> leituras, TipoOrdenacao tipoOrdenacao);

    // d.4 
    public void calcularProbabilidadeDeVida(Map<Monitorado, List<Leitura>> dados);
    
}