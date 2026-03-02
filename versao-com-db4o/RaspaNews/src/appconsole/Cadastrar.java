package appconsole;

import requisito.Fachada;

public class Cadastrar {

    public Cadastrar() {
        System.out.println("Cadastrando notícias e assuntos...");

        try {
            Fachada.criarAssunto("Política");
            Fachada.criarAssunto("Tecnologia");
            Fachada.criarAssunto("Esportes");
            Fachada.criarAssunto("Saúde");
            Fachada.criarAssunto("Economia");

            Fachada.criarNoticia(
                "Encontro entre Trump e Xi Jinping, da China, tem redução de tarifas e acordos sobre fentanil e exportação de terras raras",
                "Os dois se reuniram em uma base aérea na cidade de Busan, na Coreia do Sul, para discutir uma possível trégua na guerra comercial. A reunião, primeira entre os dois líderes desde o retorno de Trump à presidência em janeiro, durou quase duas horas.",
                "29/10/2025",
                "https://g1.globo.com/economia/noticia/2025/10/29/trump-encontra-xi-jinping.ghtml"
            );

            Fachada.criarNoticia(
                "WhatsApp não abre? Usuários reclamam de erro no app nesta quarta (29)",
                "O WhatsApp apresenta instabilidades nesta quarta-feira (29). O serviço não abre ou está fechando sozinho para usuários do iPhone.",
                "29/10/2025",
                "https://www.techtudo.com.br/noticias/2025/10/whatsapp-nao-abre-usuarios-reclamam-de-erro-no-aplicativo-edapps.ghtml"
            );

            Fachada.criarNoticia(
                "Ministério do Esporte lança Cartilha de Ações Orçamentárias 2026",
                "O Ministério do Esporte lançou a Cartilha de Ações Orçamentárias 2026, documento que reúne informações sobre programas, projetos e ações para emendas parlamentares.",
                "23/10/2025",
                "https://www.uol.com.br/esporte/.../cartilha-2026"
            );

            Fachada.criarNoticia(
                "Intoxicação por metanol: Brasil tem 58 casos confirmados, diz ministério",
                "Entre os casos em investigação, a maioria também é de São Paulo, que concentra 14 registros.",
                "24/10/2025",
                "https://www.saudenews.com/vacina-cancer"
            );

            Fachada.criarNoticia(
                "Safra de uva está no final na região de Jales",
                "Com o quilo vendido por até R$ 7, produtores enfrentam perdas causadas pelas variações de temperatura.",
                "26/10/2025",
                "https://g1.globo.com/.../safra-de-uva-final"
            );


            Fachada.adicionarAssuntoNaNoticia(
                "Encontro entre Trump e Xi Jinping, da China, tem redução de tarifas e acordos sobre fentanil e exportação de terras raras",
                "Economia"
            );

            Fachada.adicionarAssuntoNaNoticia(
                "WhatsApp não abre? Usuários reclamam de erro no app nesta quarta (29)",
                "Tecnologia"
            );

            Fachada.adicionarAssuntoNaNoticia(
                "Ministério do Esporte lança Cartilha de Ações Orçamentárias 2026",
                "Esportes"
            );

            Fachada.adicionarAssuntoNaNoticia(
                "Ministério do Esporte lança Cartilha de Ações Orçamentárias 2026",
                "Política"
            );

            Fachada.adicionarAssuntoNaNoticia(
                "Intoxicação por metanol: Brasil tem 58 casos confirmados, diz ministério",
                "Saúde"
            );

            Fachada.adicionarAssuntoNaNoticia(
                "Safra de uva está no final na região de Jales",
                "Economia"
            );

            Fachada.adicionarAssuntoNaNoticia(
                "Safra de uva está no final na região de Jales",
                "Política"
            );

            System.out.println("Cadastro concluído com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Cadastrar();
    }
}
