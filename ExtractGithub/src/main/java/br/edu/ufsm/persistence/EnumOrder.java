package br.edu.ufsm.persistence;

/**
 * <h1>EnumOrder Enum</h1>
 *
 * @author Alex Malmann Becker
 * @version 1.0
 * @since Feb 1, 2017
 */
public enum EnumOrder {

    ASC("asc", "Crescente"),
    DESC("desc", "Decrescente");

    private final String codigo;
    private final String nome;

    private EnumOrder(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

}
