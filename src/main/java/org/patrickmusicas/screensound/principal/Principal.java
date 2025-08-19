package org.patrickmusicas.screensound.principal;

import org.patrickmusicas.screensound.model.Artista;
import org.patrickmusicas.screensound.model.Musica;
import org.patrickmusicas.screensound.repository.ArtistaRepository;
import org.patrickmusicas.screensound.service.ConsultaGemini;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
    private Artista artista;
    private Musica musica;
    private ArtistaRepository artistaRepository;
    private Scanner leitura = new Scanner(System.in);
    private Optional<Artista> artistaBuscado;
    private List<Artista> listaArtistas;

    public Principal(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    public void exibirMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Cadastrar artistas
                    2 - Cadastrar músicas
                    3 - Listar músicas
                    4 - Listar artistas
                    5 - Buscar músicas por artista 
                    6 - Pesquisar dados sobre um artista
                    7 - Listar artistas pela categoria
                    8 - Pesquisar música pelo trecho
                    0 - Sair                                 
                    """;
            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    cadastrarArtista();
                    break;
                case 2:
                    cadastrarMusica();
                    break;
                case 3:
                    listarMusicas();
                    break;
                case 4:
                    listarArtistas();
                    break;
                case 5:
                    listarMusicaPorArtista();
                    break;
                case 6:
                    pesquisarDadosSobreArtista();
                    break;
                case 7:
                    ordenarArtistasPelaCategoria();
                    break;
                case 8:
                    pesquisarMusicaPeloTrecho();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void cadastrarArtista() {
        String outroArtista = "";
        while (!outroArtista.equalsIgnoreCase("N")) {
            System.out.println("Informe o nome do artista:");
            var nome = leitura.nextLine();
            existeArtista(nome);
            if (artistaBuscado.isEmpty()) {
                System.out.println("Informe o tipo desse artista:(solo, dupla, banda)");
                var tipoArtista = leitura.nextLine();
                Artista novoArtista = new Artista(nome, tipoArtista);
                artistaRepository.save(novoArtista);
                System.out.println("Cadastrar outro artista? (S/N)");
                outroArtista = leitura.nextLine();
            } else {
                break;
            }
        }
    }

    private void cadastrarMusica() {
        String outraMusica = "";
        while (!outraMusica.equalsIgnoreCase("N")) {
            System.out.println("Informe o nome do artista:");
            var artista = leitura.nextLine();
            existeArtista(artista);
            if (artistaBuscado.isPresent()) {
                System.out.println(artistaBuscado.get().getNome());
                System.out.println("Informe o nome da música:");
                var nome = leitura.nextLine();
                System.out.println("Informe a duração:");
                var duracao = leitura.nextInt();
                leitura.nextLine();
                System.out.println("Informe o gênero:");
                var genero = leitura.nextLine();
                System.out.println("Informe o álbum:");
                var album = leitura.nextLine();
                Musica musica = new Musica(nome, duracao, genero, album);
                artistaBuscado.get().setMusicas(musica);
                artistaRepository.save(artistaBuscado.get());
                System.out.println("Cadastrar outra música? (S/N)");
                outraMusica = leitura.nextLine();
            } else {
                break;
            }
        }
    }

    private void listarMusicas() {
        List<Musica> listaRetornada = artistaRepository.buscarMusicas();
        if (!listaRetornada.isEmpty()) {
            listaRetornada.forEach(m -> System.out.println("Música: " + m.getNome() + " - Álbum: " + m.getAlbum()
                    + " - Artista: " + m.getArtista().getNome()));
        } else {
            System.out.println("Nenhuma música encontrada!");
        }
    }
    private void listarArtistas(){
        existeArtistas();
        if (!listaArtistas.isEmpty()) {
            listaArtistas.forEach(a -> System.out.println("Artista: " + a.getNome()));
        }
    }

    private void listarMusicaPorArtista() {
        System.out.println("Informe o artista que deseja pesquisar:");
        var nome = leitura.nextLine();
        existeArtista(nome);
        if (artistaBuscado.isPresent()){
            List<Musica> listaRetornada = artistaRepository.buscarMusicasPeloArtista(artistaBuscado.get());
            if (!listaRetornada.isEmpty()) {
                listaRetornada.forEach(m -> System.out.println("Música: " + m.getNome() + " - Álbum: " + m.getAlbum()
                        + " - Artista: " + m.getArtista().getNome()));

            } else {
                System.out.println("Nenhuma música encontrada!");
            }
        }
    }
    private void pesquisarDadosSobreArtista(){
        System.out.println("Informe o artista que deseja pesquisar:");
        var nome = leitura.nextLine();
        var retornoApiGemini = ConsultaGemini.obterTraducao(nome);
        System.out.println(retornoApiGemini);
    }
    private void ordenarArtistasPelaCategoria(){
        existeArtistas();
        if (!listaArtistas.isEmpty()){
            List<Artista> listaRetornada = artistaRepository.listarArtistasPelaCategoria();
            listaRetornada.forEach(a -> System.out.println("Artista: " + a.getNome() + " Gênero: " + a.getTipo()));
        }
    }
    private void pesquisarMusicaPeloTrecho(){
        System.out.println("Digite o trecho que deseja pesquisar:");
        var trecho = leitura.nextLine();
        List<Musica> musicas = artistaRepository.buscarMusicaPorTrecho(trecho);
        if (!musicas.isEmpty()){
            musicas.forEach(m -> System.out.println("Música: " + m.getNome() + " Álbum: " + m.getAlbum() + " Artista: " +
                    m.getArtista().getNome()));
        } else{
            System.out.println("Música não encontrada");
        }
    }

    //Método auxiliar para checar se já existe um artista com esse nome
    private void existeArtista(String nome){
        artistaBuscado = artistaRepository.buscarArtistaPeloNome(nome);
        if (artistaBuscado.isEmpty()){
            System.out.println("Nenhum artista com o nome " + nome + " encontrado!");
        }
    }
    //Método auxiliar para checar se já existe algum artista
    private void existeArtistas(){
        listaArtistas = artistaRepository.buscarArtistas();
        if (listaArtistas.isEmpty()){
            System.out.println("Nenhum artista com o nome encontrado!");
        }
    }
}
