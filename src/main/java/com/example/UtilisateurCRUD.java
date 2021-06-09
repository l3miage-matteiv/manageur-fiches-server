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
@RequestMapping("/utilisateur")
public class UtilisateurCRUD {
    
    //@Autowired permet au Framework Spring de résoudre et injecter le Bean qui gère la connexion à la base de donnée
    @Autowired
    private DataSource dataSource;
    
    //READ ALL -- GET
    @GetMapping("/")
    public ArrayList<Utilisateur> allUtilisateurs(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM utilisateur");
            
            ArrayList<Utilisateur> L = new ArrayList<Utilisateur>();
            while (rs.next()) { 
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setTel(rs.getString("tel"));
                u.setMail(rs.getString("mail"));
                u.setIdAdresse(rs.getInt("id_adresse"));
                u.setTypeUtilisateur(rs.getString("type_utilisateur"));
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
    public Utilisateur read(@PathVariable(value="id") String id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM utilisateur where id = '" + id + "'");
            
            Utilisateur u = new Utilisateur();
            while (rs.next()) { 
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setTel(rs.getString("tel"));
                u.setMail(rs.getString("mail"));
                u.setIdAdresse(rs.getInt("id_adresse"));
                u.setTypeUtilisateur(rs.getString("type_utilisateur"));
            } 

            // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
            if(u.getNom() == null) {
                System.out.println("Utilisateur does not exist : " + id );
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


    // //CREATE -- POST : /api/utilisateur/{id}
    // @PostMapping("/{id}")
    // public Utilisateur create(@PathVariable(value="id") String id, @RequestBody Utilisateur u, HttpServletResponse response){
    //     try (Connection connection = dataSource.getConnection()) {
    //         Statement stmt = connection.createStatement(); 
            
    //         //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
    //         if( !(id.equals(u.getPseudo())) ) {
    //             System.out.println("Request Body not equivanlent to variable path : " + id + "!=" + u.getPseudo());
    //             response.setStatus(412);
    //             return null;
    //         }
    //          //une erreur 403 si un Utilisateur existe déjà avec le même identifiant
    //         if(read(id,response) == null) {
    //             PreparedStatement p = connection.prepareStatement("INSERT INTO Uti values (?,?,?,?,?)");
    //             p.setString(1, u.getPseudo());
    //             p.setString(2, u.getEmail() );
    //             p.setInt(3, u.getAge() );
    //             p.setString(4, u.getVille() );
    //             p.setString(5, u.getDescription() );
    //             p.executeUpdate();
    //             Chamis inseree = this.read(id, response);
    //             return inseree;
    //         }else {
    //             System.out.println("Chamis already exist: " + id );
    //             response.setStatus(403);
    //             return null;
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

    
    // //UPDATE -- PUT : /api/chamis/{chamisID}
    // @PutMapping("/{chamisId}")
    // public Chamis update(@PathVariable(value="chamisId") String id, @RequestBody Chamis u, HttpServletResponse response) {
    //     try (Connection connection = dataSource.getConnection()) {
    //         Statement stmt = connection.createStatement(); 
           
    //         // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
    //         if(u.getPseudo() == null) {
    //             System.out.println("Chamis does not exist : " + id );
    //             response.setStatus(404);
    //             return null;

    //         //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
    //         }else if( !(id.equals(u.getPseudo())) ) {
    //             System.out.println("Request Body not equivanlent to variable path : " + id + "!=" + u.getPseudo());
    //             response.setStatus(412);
    //             return null;

    //         }else{
    //             PreparedStatement p = connection.prepareStatement("UPDATE chamis SET pseudo = ?,email= ?, age= ?, ville= ?, description=? WHERE pseudo = '"+id+"'");
    //             p.setString(1, u.getPseudo());
    //             p.setString(2, u.getEmail() );
    //             p.setInt(3, u.getAge() );
    //             p.setString(4, u.getVille() );
    //             p.setString(5, u.getDescription() );
    //             p.executeUpdate();
    //             Chamis inseree = this.read(id, response);
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
    // @DeleteMapping("/{chamisId}")
    // public void delete(@PathVariable(value="chamisId") String id, HttpServletResponse response) {
    //     try (Connection connection = dataSource.getConnection()) {
    //         Statement stmt = connection.createStatement(); 
    //         int rs = stmt.executeUpdate("DELETE FROM chamis WHERE pseudo = '"+id+"'");

    //         // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
    //         if(rs == 0){
    //             System.out.println("Chamis does not exist : " + id );
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
    

