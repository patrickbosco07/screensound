package org.patrickmusicas.screensound.enums;

public enum Tipo {
    SOLO("Solo"),
    DUPLA("Dupla"),
    BANDA("Banda");

    private String tipoArtista;

    Tipo(String tipoArtista) {
        this.tipoArtista = tipoArtista;
    }

    public static Tipo defineTipo(String tipo){
        for (Tipo tipoAtual: Tipo.values()) {
            if (tipoAtual.tipoArtista.equalsIgnoreCase(tipo)){
                return tipoAtual;
            }
        }
        throw new RuntimeException("Nenhum tipo de artista encontrado para o tipo: " + tipo);
    }
}
