package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.Statement; 
import java.util.ArrayList; 
import javax.servlet.http.HttpServletResponse; 
import javax.sql.DataSource;

import com.example.model.Tuteur;
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
@RequestMapping("/tuteur")
public class TuteurCRUD {
    
    //@Autowired permet au Framework Spring de résoudre et injecter le Bean qui gère la connexion à la base de donnée
    @Autowired
    private DataSource dataSource;
    
    //READ ALL -- GET
    @GetMapping("/")
    public ArrayList<Tuteur> getAllTuteurs(HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM tuteur LEFT OUTER JOIN utilisateur ON (tuteur.id_utilisateur = utilisateur.id)");
            
            ArrayList<Tuteur> L = new ArrayList<Tuteur>();
            while (rs.next()) { 
                Tuteur t = new Tuteur();
                t.setID(rs.getLong("id_utilisateur"));
                t.setNom(rs.getString("nom"));
                t.setPrenom(rs.getString("prenom"));
                t.setTel(rs.getString("tel"));
                t.setMail(rs.getString("mail"));
                t.setAdresse(rs.getString("adresse"));
                t.setCodePostal(rs.getString("code_postal"));
                t.setVille(rs.getString("ville"));
                t.setPays(rs.getString("pays"));
                t.setTypeUtilisateur(rs.getString("type_utilisateur"));
                t.setFonction(rs.getString("fonction"));
                t.setService(rs.getString("service"));
                L.add(t);
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
    public Tuteur read(@PathVariable(value="id") int id, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
            ResultSet rs = stmt.executeQuery("SELECT * FROM tuteur LEFT OUTER JOIN utilisateur ON (tuteur.id_utilisateur = utilisateur.id) where id_utilisateur = '" + id + "'");
            
            Tuteur t = new Tuteur();
            while (rs.next()) { 
                t.setID(rs.getLong("id_utilisateur"));
                t.setNom(rs.getString("nom"));
                t.setPrenom(rs.getString("prenom"));
                t.setTel(rs.getString("tel"));
                t.setMail(rs.getString("mail"));
                t.setAdresse(rs.getString("adresse"));
                t.setCodePostal(rs.getString("code_postal"));
                t.setVille(rs.getString("ville"));
                t.setPays(rs.getString("pays"));
                t.setTypeUtilisateur(rs.getString("type_utilisateur"));
                t.setFonction(rs.getString("fonction"));
                t.setService(rs.getString("service"));
            } 

            // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
            if(t.getNom() == null) {
                System.out.println("Tuteur does not exist : " + id );
                response.setStatus(404);
                return null;
            } else {
                return t; 
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


    //CREATE -- POST : /tuteur/add/{id}
    @PostMapping("/add/{id}")
    public Tuteur create(@PathVariable(value="id") int id, @RequestBody Tuteur tuteur, HttpServletResponse response){
        try (Connection connection = dataSource.getConnection()) {
            
            //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
            if( !(id == (tuteur.getID())) ) {
                System.out.println("Request Body not equivanlent to variable path : " + id + "!=" + tuteur.getID());
                response.setStatus(412);
                return null;
            }
             //une erreur 403 si un Tuteur existe déjà avec le même identifiant
            if(read(id,response) == null) {
                PreparedStatement p = connection.prepareStatement("INSERT INTO tuteur values (?,?,?,?,?,?,?,?,?,?,?,?)");
                p.setLong(1, tuteur.getID());
                p.setString(2, tuteur.getNom() );
                p.setString(3, tuteur.getPrenom() );
                p.setString(4, tuteur.getTel() );
                p.setString(5, tuteur.getMail() );
                p.setString(6, tuteur.getTypeUtilisateur() );
                p.setString(7, tuteur.getAdresse());
                p.setString(8, tuteur.getCodePostal());
                p.setString(9, tuteur.getVille());
                p.setString(10, tuteur.getPays());
                p.setString(11, tuteur.getFonction());
                p.setString(12, tuteur.getService());
                p.executeUpdate();
                Tuteur inseree = this.read(id, response);
                return inseree;
            }else {
                System.out.println("Tuteur already exist: " + id );
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

    
    //UPDATE -- PUT : /tuteur/update/{id}
    @PutMapping("/update/{id}")
    public Tuteur update(@PathVariable(value="id") int id, @RequestBody Tuteur tuteur, HttpServletResponse response) {
        try (Connection connection = dataSource.getConnection()) {
            Statement stmt = connection.createStatement(); 
           
            // Une erreur 404 si l'identifiant de l'Tuteur ne correspond pas à un Tuteur dans la base.
            if(tuteur.getNom() == null) {
                System.out.println("Tuteur does not exist : " + id );
                response.setStatus(404);
                return null;

            //une erreur 412 si l'identifiant du User dans l'URL n'est pas le même que celui du User dans le corp de la requête.
            } else if( !(id == (tuteur.getID())) ) {
                System.out.println("Request Body not equivanlent to variable path : " + id + "!=" + tuteur.getID());
                response.setStatus(412);
                return null;

            } else {
                PreparedStatement p = connection.prepareStatement("UPDATE tuteur SET id = ?, nom = ?, prenom = ?, tel = ?, mail = ?, type_utilisateur = ?, adresse = ?, code_postal = ?, ville = ?, pays = ?, type_affiliation = ?, fonction = ?, service = ? WHERE id = '"+id+"'");
                p.setLong(1, tuteur.getID());
                p.setString(2, tuteur.getNom() );
                p.setString(3, tuteur.getPrenom() );
                p.setString(4, tuteur.getTel() );
                p.setString(5, tuteur.getMail() );
                p.setString(6, tuteur.getTypeUtilisateur() );
                p.setString(7, tuteur.getAdresse());
                p.setString(8, tuteur.getCodePostal());
                p.setString(9, tuteur.getVille());
                p.setString(10, tuteur.getPays());
                p.setString(11, tuteur.getFonction());
                p.setString(12, tuteur.getService());
                p.executeUpdate();
                Tuteur inseree = this.read(id, response);
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
            int rs = stmt.executeUpdate("DELETE FROM tuteur WHERE id = '"+id+"'");

            // Une erreur 404 si l'identifiant de l'utilisateur ne correspond pas à un utilisateur dans la base.
            if(rs == 0){
                System.out.println("Tuteur does not exist : " + id );
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
    
