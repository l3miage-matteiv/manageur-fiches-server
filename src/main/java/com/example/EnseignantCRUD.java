package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.Statement; 
import java.util.ArrayList; 
import javax.servlet.http.HttpServletResponse; 
import javax.sql.DataSource;

import com.example.model.Enseignant;
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
@RequestMapping("/enseignant")
public class EnseignantCRUD {
    
    //@Autowired permet au Framework Spring de résoudre et injecter le Bean qui gère la connexion à la base de donnée
    @Autowired
    private DataSource dataSource;
    
    //READ ALL -- GET
    @GetMapping("/")
    public ArrayList<Enseignant> getAllEnseignants(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM enseignant LEFT OUTER JOIN utilisateur ON (enseignant.id_utilisateur = utilisateur.id)");
            
            ArrayList<Enseignant> L = new ArrayList<Enseignant>();
            while (rs.next()) { 
                Enseignant u = new Enseignant();
                u.setID(rs.getLong("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setTel(rs.getString("tel"));
                u.setMail(rs.getString("mail"));
                u.setTypeUtilisateur(rs.getString("type_utilisateur"));
                u.setAdresse(rs.getString("adresse"));
                u.setCodePostal(rs.getString("code_postal"));
                u.setVille(rs.getString("ville"));
                u.setPays(rs.getString("pays"));
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
    public Enseignant getEnseignantById(@PathVariable(value="id") int id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM enseignant LEFT OUTER JOIN utilisateur ON (enseignant.id_utilisateur = utilisateur.id) WHERE id = '" + id + "'");
            
            Enseignant u = new Enseignant();
            while (rs.next()) { 
                u.setID(rs.getLong("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setTel(rs.getString("tel"));
                u.setMail(rs.getString("mail"));
                u.setTypeUtilisateur(rs.getString("type_utilisateur"));
                u.setAdresse(rs.getString("adresse"));
                u.setCodePostal(rs.getString("code_postal"));
                u.setVille(rs.getString("ville"));
                u.setPays(rs.getString("pays"));
            } 

            // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
            if(u.getNom() == null) {
                System.out.println("Enseignant does not exist : " + id );
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

    //CREATE -- POST : /service_rh/add/{id}
    @PostMapping("/add/{id}")
    public Enseignant addEnseignant(@PathVariable(value="id") int id, @RequestBody Enseignant u, HttpServletResponse response){
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            
            //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
            if( !(id == (u.getID())) ) {
                System.out.println("Request Body not equivalent to variable path : " + id + "!=" + u.getID());
                response.setStatus(412);
                return null;
            }
             //une erreur 403 si un Enseignant existe déjà avec le même identifiant
            if(getEnseignantById(id,response) == null) {
                PreparedStatement p = connection.prepareStatement("INSERT INTO enseignant values (?,?,?,?,?,?,?,?,?,?)");
                p.setLong(1, u.getID());
                p.setString(2, u.getNom() );
                p.setString(3, u.getPrenom() );
                p.setString(4, u.getTel() );
                p.setString(5, u.getMail() );
                p.setString(6, u.getTypeUtilisateur() );
                p.setString(7, u.getAdresse());
                p.setString(8, u.getCodePostal());
                p.setString(9, u.getVille());
                p.setString(10, u.getPays());
                p.executeUpdate();
                Enseignant inseree = this.getEnseignantById(id, response);
                return inseree;
            }else {
                System.out.println("Enseignant already exist: " + id );
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

    
    //UPDATE -- PUT : /utilisateur/{id}
    @PutMapping("/update/{id}")
    public Enseignant updateEnseignant(@PathVariable(value="id") int id, @RequestBody Enseignant u, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
           
            // Une erreur 404 si l'identifiant de l'Enseignant ne correspond pas à un Enseignant dans la base.
            if(u.getNom() == null) {
                System.out.println("Service RH does not exist : " + id );
                response.setStatus(404);
                return null;

            //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
            }else if( !(id == (u.getID())) ) {
                System.out.println("Request Body not equivanlent to variable path : " + id + "!=" + u.getID());
                response.setStatus(412);
                return null;

            } else {
                PreparedStatement p = connection.prepareStatement("UPDATE enseignant id= ?, nom = ?, prenom = ?, tel = ?, mail = ?, type_utilisateur = ?, adresse = ?, code_postal = ?, ville = ?, pays = ? WHERE id = '"+id+"'");
                p.setLong(1, u.getID());
                p.setString(2, u.getNom() );
                p.setString(3, u.getPrenom() );
                p.setString(4, u.getTel() );
                p.setString(5, u.getMail() );
                p.setString(6, u.getTypeUtilisateur() );
                p.setString(7, u.getAdresse());
                p.setString(8, u.getCodePostal());
                p.setString(9, u.getVille());
                p.setString(10, u.getPays());
                p.executeUpdate();
                Enseignant inseree = this.getEnseignantById(id, response);
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
    public void deleteUtilisateur(@PathVariable(value="id") int id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            int rs = stmt.executeUpdate("DELETE FROM enseignant WHERE id = '"+id+"'");

            // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
            if(rs == 0){
                System.out.println("Enseignant does not exist : " + id );
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
    
