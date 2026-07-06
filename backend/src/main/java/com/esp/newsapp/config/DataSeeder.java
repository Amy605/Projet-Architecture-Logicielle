package com.esp.newsapp.config;

import com.esp.newsapp.model.*;
import com.esp.newsapp.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final AuthTokenRepository authTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, CategoryRepository categoryRepository,
                       ArticleRepository articleRepository, AuthTokenRepository authTokenRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
        this.authTokenRepository = authTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder().username("admin").password(passwordEncoder.encode("admin123")).role(Role.ADMIN).build());
            userRepository.save(User.builder().username("editeur").password(passwordEncoder.encode("editeur123")).role(Role.EDITEUR).build());
        }

        if (categoryRepository.count() == 0) {
            Category politique = categoryRepository.save(Category.builder().nom("Politique").build());
            Category sport = categoryRepository.save(Category.builder().nom("Sport").build());
            Category technologie = categoryRepository.save(Category.builder().nom("Technologie").build());
            Category economie = categoryRepository.save(Category.builder().nom("Economie").build());

            articleRepository.save(Article.builder().titre("Ouverture du sommet regional a Dakar")
                    .resume("Les chefs d'Etat se reunissent pour discuter d'integration economique.")
                    .contenu("Le sommet regional s'est ouvert ce matin a Dakar en presence de plusieurs chefs d'Etat de la sous-region. Les discussions porteront sur l'integration economique, la libre circulation des personnes et des biens, ainsi que la securite regionale.")
                    .categorie(politique).build());

            articleRepository.save(Article.builder().titre("Les Lions du Senegal se qualifient")
                    .resume("L'equipe nationale valide son billet pour la prochaine competition.")
                    .contenu("Apres une victoire convaincante, les Lions du Senegal ont valide leur qualification pour la prochaine grande competition continentale. Le selectionneur s'est felicite de l'engagement du groupe.")
                    .categorie(sport).build());

            articleRepository.save(Article.builder().titre("L'ESP lance un nouveau hub d'innovation")
                    .resume("Un espace dedie aux startups etudiantes voit le jour a l'Ecole Superieure Polytechnique.")
                    .contenu("L'Ecole Superieure Polytechnique de l'UCAD a inaugure un hub d'innovation destine a accompagner les projets etudiants en technologie, avec un accent particulier sur l'intelligence artificielle et le genie logiciel.")
                    .categorie(technologie).build());

            articleRepository.save(Article.builder().titre("Croissance economique : les previsions revues a la hausse")
                    .resume("Les institutions financieres tablent sur une croissance soutenue pour l'annee a venir.")
                    .contenu("Selon les dernieres previsions, la croissance economique nationale devrait depasser les attentes initiales, portee par les secteurs de l'agriculture, du numerique et des services.")
                    .categorie(economie).build());

            articleRepository.save(Article.builder().titre("Nouvelle reglementation sur les donnees personnelles")
                    .resume("Une loi renforce la protection des donnees des citoyens.")
                    .contenu("Le gouvernement a adopte une nouvelle reglementation visant a renforcer la protection des donnees personnelles des citoyens, en conformite avec les standards internationaux en la matiere.")
                    .categorie(politique).build());

            articleRepository.save(Article.builder().titre("Un derby local attire des milliers de supporters")
                    .resume("Le stade affichait complet pour ce match tres attendu.")
                    .contenu("Le derby local a tenu toutes ses promesses avec une ambiance electrique dans les tribunes. Les deux equipes ont livre un match spectaculaire devant un public conquis.")
                    .categorie(sport).build());
        }

        if (authTokenRepository.count() == 0) {
            authTokenRepository.save(AuthToken.builder()
                    .token("demo-token-0000-0000-000000000000")
                    .description("Jeton de demonstration (a supprimer en production)")
                    .build());
        }
    }
}
