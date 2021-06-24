package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.Statement; 
import java.util.ArrayList; 
import javax.servlet.http.HttpServletResponse; 
import javax.sql.DataSource;

import com.example.model.FicheRenseignement;
import com.fasterxml.jackson.annotation.JacksonInject.Value;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.CrossOrigin; 
import org.springframework.web.bind.annotation.DeleteMapping; 
import org.springframework.web.bind.annotation.GetMapping; 
import org.springframework.web.bind.annotation.PathVariable; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PutMapping; 
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


//controleur REST ( répondre à HTTP avec des données quelconques (pas nécessaires HTML) )
@RestController
//indique que le contrôleur accepte les requêtes provenant d'une source quelconque (et donc pas nécessairement le même serveur). 
@CrossOrigin
// Indique que les ressources HTTP qui seront déclarées dans la classe seront toutes préfixées par /api/users.
@RequestMapping("/fiche_renseignement")
public class FicheRenseignementCRUD {
    //@Autowired permet au Framework Spring de résoudre et injecter le Bean qui gère la connexion à la base de donnée
    @Autowired
    private DataSource dataSource;
    
    //READ ALL -- GET
    @GetMapping("/")
    public ArrayList<FicheRenseignement> getAllFiches(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM fiche");
            
            ArrayList<FicheRenseignement> L = new ArrayList<FicheRenseignement>();
            while (rs.next()) { 
                FicheRenseignement u = new FicheRenseignement();
                u.setID(rs.getLong("id"));
                u.setIDEtudiant(rs.getLong("id_etudiant"));
                u.setIDServiceRH(rs.getLong("id_service_rh"));
                u.setIDTuteur(rs.getLong("id_tuteur"));
                u.setIDEnseignent(rs.getLong("id_enseignent"));
                u.setMailServiceRH(rs.getString("mail_service_rh"));
                u.setMailTuteur(rs.getString("mail_tuteur"));
                u.setMailEnseignent(rs.getString("mail_enseignent"));
                u.setIDFicheAccueilStagiaire(rs.getLong("id_fiche_accueil_stagiaire"));
                u.setIDFicheTuteur(rs.getLong("id_fiche_tuteur"));
                L.add(u);
            } 
            return L;
        } catch (Exception e) {
            response.setStatus(500);

            try {
                response.getOutputStream().print( e.getMessage() );
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
            }
            System.err.println(e.getMessage());
            return null;
        }
    }


    //READ -- GET 
    @GetMapping("/{id}")
    public FicheRenseignement getFicheById(@PathVariable(value="id") int id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM fiche WHERE id = '" + id + "'");
            
            FicheRenseignement u = new FicheRenseignement();
            while (rs.next()) { 
                u.setID(rs.getLong("id"));
                u.setIDEtudiant(rs.getLong("id_etudiant"));
                u.setIDServiceRH(rs.getLong("id_service_rh"));
                u.setIDTuteur(rs.getLong("id_tuteur"));
                u.setIDEnseignent(rs.getLong("id_enseignent"));
                u.setMailServiceRH(rs.getString("mail_service_rh"));
                u.setMailTuteur(rs.getString("mail_tuteur"));
                u.setMailEnseignent(rs.getString("mail_enseignent"));
                u.setIDFicheAccueilStagiaire(rs.getLong("id_fiche_accueil_stagiaire"));
                u.setIDFicheTuteur(rs.getLong("id_fiche_tuteur"));
            } 

            // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
            if(u.getID() == null) {
                System.out.println("Fiche does not exist : " + id );
                response.setStatus(404);
                return null;
            } else {
                return u; 
            }
            

        } catch (Exception e) {
            response.setStatus(500);

            try {
                response.getOutputStream().print( e.getMessage() );
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
            }
            System.err.println(e.getMessage());
            return null;
        }
    }

    //READ -- GET /utilisateur/last_utilisateur
    @GetMapping("/last_fiche_renseignement")
    public FicheRenseignement getLastUtilisateur(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM fiche ORDER BY ID DESC LIMIT 1");
            
            FicheRenseignement u = new FicheRenseignement();
            while (rs.next()) { 
                u.setID(rs.getLong("id"));
                u.setIDEtudiant(rs.getLong("id_etudiant"));
                u.setIDServiceRH(rs.getLong("id_service_rh"));
                u.setIDTuteur(rs.getLong("id_tuteur"));
                u.setIDEnseignent(rs.getLong("id_enseignent"));
                u.setMailServiceRH(rs.getString("mail_service_rh"));
                u.setMailTuteur(rs.getString("mail_tuteur"));
                u.setMailEnseignent(rs.getString("mail_enseignent"));
                u.setIDFicheAccueilStagiaire(rs.getLong("id_fiche_accueil_stagiaire"));
                u.setIDFicheTuteur(rs.getLong("id_fiche_tuteur"));
            } 
            return u;
        } catch (Exception e) {
            response.setStatus(500);

            try {
                response.getOutputStream().print( e.getMessage() );
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
            }
            System.err.println(e.getMessage());
            return null;
        }
    }

    // CREATE -- POST : /fiche/add/{id}
    @PostMapping("/add/{id}")
    public FicheRenseignement addFiche(@PathVariable(value="id") int id, @RequestBody FicheRenseignement u, HttpServletResponse response){
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            
            //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
            if( !(id == (u.getID())) ) {
                System.out.println("Request Body not equivalent to variable path : " + id + "!=" + u.getID());
                response.setStatus(412);
                return null;
            }
             //une erreur 403 si un Utilisateur existe déjà avec le même identifiant
            if(getFicheById(id,response) == null) {
                PreparedStatement p = connection.prepareStatement("INSERT INTO fiche values (?,?,?,?,?,?,?,?,?,?)");
                p.setLong(1, u.getID());
                p.setLong(2, u.getIDEtudiant());
                p.setLong(3, u.getIDServiceRH());
                p.setLong(4, u.getIDTuteur());
                p.setLong(5, u.getIDEnseignent());
                p.setString(6, u.getMailServiceRH());
                p.setString(7, u.getMailTuteur());
                p.setString(8, u.getMailEnseignent());
                p.setLong(9, u.getIDFicheAccueilStagiaire());
                p.setLong(10, u.getIDFicheTuteur());
                p.executeUpdate();
                FicheRenseignement inseree = this.getFicheById(id, response);
                return inseree;
            } else {
                System.out.println("Fiche already exist: " + id );
                response.setStatus(403);
                return null;
            }
        } catch (Exception e) {
            response.setStatus(500);
            try {
                response.getOutputStream().print( e.getMessage() );
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
            }
            System.err.println(e.getMessage());
            return null;
        }
    }

    
    // //UPDATE -- PUT : /utilisateur/{chamisID}
    // @PutMapping("/update/{id}")
    // public Utilisateur updateUtilisateur(@PathVariable(value="id") int id, @RequestBody Utilisateur u, HttpServletResponse response) {
    //     try (Connection connection = dataSource.getConnection()) {
    //         Statement stmt = connection.createStatement(); 
           
    //         // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
    //         if(u.getNom() == null) {
    //             System.out.println("Utilisateur does not exist : " + id );
    //             response.setStatus(404);
    //             return null;

    //         //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
    //         }else if( !(id == (u.getID())) ) {
    //             System.out.println("Request Body not equivanlent to variable path : " + id + "!=" + u.getID());
    //             response.setStatus(412);
    //             return null;

    //         }else{
    //             PreparedStatement p = connection.prepareStatement("UPDATE utilisateur id= ?, nom = ?, prenom = ?, tel = ?, mail = ?, type_utilisateur = ?, adresse = ?, code_postal = ?, ville = ?, pays = ? WHERE id = '"+id+"'");
    //             p.setLong(1, u.getID());
    //             p.setString(2, u.getNom() );
    //             p.setString(3, u.getPrenom() );
    //             p.setString(4, u.getTel() );
    //             p.setString(5, u.getMail() );
    //             p.setString(6, u.getTypeUtilisateur() );
    //             p.setString(7, u.getAdresse());
    //             p.setString(8, u.getCodePostal());
    //             p.setString(9, u.getVille());
    //             p.setString(10, u.getPays());
    //             p.executeUpdate();
    //             Utilisateur inseree = this.getUtilisateurById(id, response);
    //             return inseree;
    //         }   

    //     } catch (Exception e) {
    //         response.setStatus(500);

    //         try {
    //             response.getOutputStream().print( e.getMessage() );
    //         } catch (Exception e2) {
    //             System.err.println(e2.getMessage());
    //         }
    //         System.err.println(e.getMessage());
    //         return null;
    //     } 
    // }

        
    // //DELETE -- DELETE
    // @DeleteMapping("/delete/{id}")
    // public void deleteUtilisateur(@PathVariable(value="id") int id, HttpServletResponse response) {
    //     try (Connection connection = dataSource.getConnection()) {
    //         Statement stmt = connection.createStatement(); 
    //         int rs = stmt.executeUpdate("DELETE FROM utilisateur WHERE id = '"+id+"'");

    //         // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
    //         if(rs == 0){
    //             System.out.println("Utilisateur does not exist : " + id );
    //             response.setStatus(404);
    //         }
    //     } catch (Exception e) {
    //         response.setStatus(500);

    //         try {
    //             response.getOutputStream().print( e.getMessage() );
    //         } catch (Exception e2) {
    //             System.err.println(e2.getMessage());
    //         }
    //         System.err.println(e.getMessage());
    //     }
    // }
}
