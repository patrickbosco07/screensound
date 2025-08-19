package org.patrickmusicas.screensound.repository;

import org.patrickmusicas.screensound.model.Artista;
import org.patrickmusicas.screensound.model.Musica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    @Query(value = "select artista from Artista artista WHERE artista.nome ILIKE %:nome")
    Optional<Artista> buscarArtistaPeloNome(String nome);
    @Query(value = "select musica from Artista artista JOIN artista.musicas musica")
    List<Musica> buscarMusicas();
    @Query(value = "select musica from Artista artista JOIN artista.musicas musica WHERE artista = :artista")
    List<Musica> buscarMusicasPeloArtista(Artista artista);
    @Query(value = "select artista from Artista artista")
    List<Artista> buscarArtistas();
    @Query(value = "select artista from Artista artista ORDER BY artista.tipo")
    List<Artista> listarArtistasPelaCategoria();
    @Query(value = "select musica from Artista artista JOIN artista.musicas musica WHERE musica.nome ILIKE %:trecho%")
    List<Musica> buscarMusicaPorTrecho(String trecho);
}
