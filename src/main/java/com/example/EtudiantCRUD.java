package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.Statement; 
import java.util.ArrayList; 
import javax.servlet.http.HttpServletResponse; 
import javax.sql.DataSource;

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
@RequestMapping("/etudiant")
public class EtudiantCRUD {
    
    //@Autowired permet au Framework Spring de résoudre et injecter le Bean qui gère la connexion à la base de donnée
    @Autowired
    private DataSource dataSource;
    
    //READ ALL -- GET
    @GetMapping("/")
    public ArrayList<Etudiant> getAllEtudiants(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM etudiant LEFT OUTER JOIN utilisateur ON (etudiant.id_utilisateur = utilisateur.id) LEFT OUTER JOIN adresse ON (utilisateur.id_adresse = adresse.id)");
            
            ArrayList<Etudiant> L = new ArrayList<Etudiant>();
            while (rs.next()) { 
                Etudiant e = new Etudiant();
                e.setID(rs.getInt("id_utilisateur"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setTel(rs.getString("tel"));
                e.setMail(rs.getString("mail"));
                e.setIdAdresse(rs.getInt("id_adresse"));
                e.setAdresse(rs.getString("adresse"));
                e.setCodePostal(rs.getString("code_postal"));
                e.setVille(rs.getString("ville"));
                e.setPays(rs.getString("pays"));
                e.setTypeUtilisateur(rs.getString("type_utilisateur"));
                e.setNumeroEtudiant(rs.getString("numero_etudiant"));
                e.setTypeAffiliation(rs.getString("type_affiliation"));
                e.setCaisseAssuranceMaladie(rs.getString("caisse_assurance"));
                e.setInscription(rs.getString("inscription"));
                e.setEnseignantReferent(rs.getString("enseignant_referent"));
                L.add(e);
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
    public Etudiant read(@PathVariable(value="id") int id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM etudiant LEFT OUTER JOIN utilisateur ON (etudiant.id_utilisateur = utilisateur.id) LEFT OUTER JOIN adresse ON (utilisateur.id_adresse = adresse.id) where id_utilisateur = '" + id + "'");
            
            Etudiant e = new Etudiant();
            while (rs.next()) { 
                e.setID(rs.getInt("id_utilisateur"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setTel(rs.getString("tel"));
                e.setMail(rs.getString("mail"));
                e.setIdAdresse(rs.getInt("id_adresse"));
                e.setAdresse(rs.getString("adresse"));
                e.setCodePostal(rs.getString("code_postal"));
                e.setVille(rs.getString("ville"));
                e.setPays(rs.getString("pays"));
                e.setTypeUtilisateur(rs.getString("type_utilisateur"));
                e.setNumeroEtudiant(rs.getString("numero_etudiant"));
                e.setTypeAffiliation(rs.getString("type_affiliation"));
                e.setCaisseAssuranceMaladie(rs.getString("caisse_assurance"));
                e.setInscription(rs.getString("inscription"));
                e.setEnseignantReferent(rs.getString("enseignant_referent"));
            } 

            // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
            if(e.getNom() == null) {
                System.out.println("Etudiant does not exist : " + id );
                response.setStatus(404);
                return null;
            } else {
                return e; 
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


    //CREATE -- POST : /utilisateur/{id}
    @PostMapping("/{id}")
    public Etudiant create(@PathVariable(value="id") int id, @RequestBody Etudiant u, HttpServletResponse response){
        try (Connection connection = dataSource.getConnection()) {
            
            //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
            if( !(id == (u.getID())) ) {
                System.out.println("Request Body not equivanlent to variable path : " + id + "!=" + u.getID());
                response.setStatus(412);
                return null;
            }
             //une erreur 403 si un Etudiant existe déjà avec le même identifiant
            if(read(id,response) == null) {
                PreparedStatement p = connection.prepareStatement("INSERT INTO etudiant values (?,?,?,?,?,?,?)");
                p.setInt(1, u.getID());
                p.setString(2, u.getNom() );
                p.setString(3, u.getPrenom() );
                p.setString(4, u.getTel() );
                p.setString(5, u.getMail() );
                p.setInt(6, u.getIdAdresse() );
                p.setString(7, u.getTypeUtilisateur() );
                p.executeUpdate();
                Etudiant inseree = this.read(id, response);
                return inseree;
            }else {
                System.out.println("Utilisateur already exist: " + id );
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

    
    //UPDATE -- PUT : /utilisateur/{chamisID}
    @PutMapping("/{id}")
    public Utilisateur update(@PathVariable(value="id") int id, @RequestBody Utilisateur u, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
           
            // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
            if(u.getNom() == null) {
                System.out.println("Utilisateur does not exist : " + id );
                response.setStatus(404);
                return null;

            //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
            }else if( !(id == (u.getID())) ) {
                System.out.println("Request Body not equivanlent to variable path : " + id + "!=" + u.getID());
                response.setStatus(412);
                return null;

            }else{
                PreparedStatement p = connection.prepareStatement("UPDATE utilisateur SET id = ?,nom = ?, prenom = ?, tel = ?, mail = ?, id_adresse = ?, type_utilisateur = ? WHERE id = '"+id+"'");
                p.setInt(1, u.getID());
                p.setString(2, u.getNom() );
                p.setString(3, u.getPrenom() );
                p.setString(4, u.getTel() );
                p.setString(5, u.getMail() );
                p.setInt(6, u.getIdAdresse() );
                p.setString(7, u.getTypeUtilisateur() );
                p.executeUpdate();
                Utilisateur inseree = this.read(id, response);
                return inseree;
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

        
    //DELETE -- DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(value="id") int id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            int rs = stmt.executeUpdate("DELETE FROM utilisateur WHERE id = '"+id+"'");

            // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
            if(rs == 0){
                System.out.println("Utilisateur does not exist : " + id );
                response.setStatus(404);
            }
        } catch (Exception e) {
            response.setStatus(500);

            try {
                response.getOutputStream().print( e.getMessage() );
            } catch (Exception e2) {
                System.err.println(e2.getMessage());
            }
            System.err.println(e.getMessage());
        }
    }

}
    
