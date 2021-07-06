package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.Statement; 
import java.util.ArrayList; 
import javax.servlet.http.HttpServletResponse; 
import javax.sql.DataSource;

import com.example.model.Etudiant;
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM etudiant LEFT OUTER JOIN utilisateur ON (etudiant.id_utilisateur = utilisateur.id) WHERE type_utilisateur = 'Étudiant'");
            
            ArrayList<Etudiant> L = new ArrayList<Etudiant>();
            while (rs.next()) { 
                Etudiant e = new Etudiant();
                e.setID(rs.getLong("id_utilisateur"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setTel(rs.getString("tel"));
                e.setMail(rs.getString("mail"));
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
            ResultSet rs = stmt.executeQuery("SELECT * FROM etudiant LEFT OUTER JOIN utilisateur ON (etudiant.id_utilisateur = utilisateur.id) where id = '" + id + "'");
            
            Etudiant e = new Etudiant();
            while (rs.next()) { 
                e.setID(rs.getLong("id_utilisateur"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setTel(rs.getString("tel"));
                e.setMail(rs.getString("mail"));
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


    //CREATE -- POST : /etudiant/add/{id}
    @PostMapping("/add/{id}")
    public Etudiant create(@PathVariable(value="id") int id, @RequestBody Etudiant etu, HttpServletResponse response){
        try (Connection connection = dataSource.getConnection()) {
            
            //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
            if( !(id == (etu.getID())) ) {
                System.out.println("Request Body not equivanlent to variable path : " + id + "!=" + etu.getID());
                response.setStatus(412);
                return null;
            }
             //une erreur 403 si un Etudiant existe déjà avec le même identifiant
            if(read(id,response) == null) {
                PreparedStatement p = connection.prepareStatement("INSERT INTO etudiant values (?,?,?,?,?,?)");
                p.setLong(1, etu.getID());
                p.setString(2, etu.getNumeroEtudiant());
                p.setString(3, etu.getTypeAffiliation());
                p.setString(4, etu.getCaisseAssuranceMaladie());
                p.setString(5, etu.getInscription());
                p.setString(6, etu.getEnseignantReferent());
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
    @PutMapping("/update/{id}")
    public Etudiant update(@PathVariable(value="id") int id, @RequestBody Etudiant etu, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
           
            // Une erreur 404 si l'identifiant de l'Etudiant ne correspond pas à un Etudiant dans la base.
            if(etu.getNom() == null) {
                System.out.println("Etudiant does not exist : " + id );
                response.setStatus(404);
                return null;

            //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
            } else if( !(id == (etu.getID())) ) {
                System.out.println("Request Body not equivanlent to variable path : " + id + "!=" + etu.getID());
                response.setStatus(412);
                return null;

            } else {
                PreparedStatement p = connection.prepareStatement("UPDATE etudiant SET id_utilisateur = ?, nom = ?, prenom = ?, tel = ?, mail = ?, type_utilisateur = ?, adresse = ?, code_postal = ?, ville = ?, pays = ?, numero_etudiant = ?, type_affiliation = ?, caisse_assurance = ?, inscription = ?, enseignant_referent = ? WHERE id = '"+id+"'");
                p.setLong(1, etu.getID());
                p.setString(2, etu.getNom() );
                p.setString(3, etu.getPrenom() );
                p.setString(4, etu.getTel() );
                p.setString(5, etu.getMail() );
                p.setString(6, etu.getTypeUtilisateur() );
                p.setString(7, etu.getAdresse());
                p.setString(8, etu.getCodePostal());
                p.setString(9, etu.getVille());
                p.setString(10, etu.getPays());
                p.setString(11, etu.getNumeroEtudiant());
                p.setString(12, etu.getTypeAffiliation());
                p.setString(13, etu.getCaisseAssuranceMaladie());
                p.setString(14, etu.getInscription());
                p.setString(15, etu.getEnseignantReferent());
                p.executeUpdate();
                Etudiant inseree = this.read(id, response);
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
    @DeleteMapping("/delete/{id}")
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
    
