// package com.example;

// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import java.util.List;

// import com.example.model.Utilisateur;
// import com.example.service.UtilisateurService;

// @RestController
// @RequestMapping("/utilisateur")
// public class UtilisateurResource {
//     private final UtilisateurService utilisateurService;

//     public UtilisateurResource(UtilisateurService utilisateurService) {
//         this.utilisateurService = utilisateurService;
//     }

//     @GetMapping
//     public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
//         List<Utilisateur> utilisateurs = utilisateurService.findAllUtilisateurs();
//         return new ResponseEntity<>(utilisateurs, HttpStatus.OK);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable("id") Long id) {
//         Utilisateur utilisateur = utilisateurService.findUtilisateurById(id);
//         return new ResponseEntity<>(utilisateur, HttpStatus.OK);
//     }

//     @PostMapping("/add")
//     public ResponseEntity<Utilisateur> addUtilisateur(@RequestBody Utilisateur utilisateur) {
//         Utilisateur newUtilisateur = utilisateurService.addUtilisateur(utilisateur);
//         return new ResponseEntity<>(newUtilisateur, HttpStatus.CREATED);
//     }

//     @PutMapping("/update")
//     public ResponseEntity<Utilisateur> updateUtilisateur(@RequestBody Utilisateur utilisateur) {
//         Utilisateur updatedUtilisateur = utilisateurService.updateUtilisateur(utilisateur);
//         return new ResponseEntity<>(updatedUtilisateur, HttpStatus.OK);
//     }

//     @DeleteMapping("/delete/{id}")
//     public ResponseEntity<Utilisateur> deleteUtilisateur(@PathVariable("id") Long id) {
//         utilisateurService.deleteUtilisateur(id);
//         return new ResponseEntity<>(HttpStatus.OK);
//     }
// }
