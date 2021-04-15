package com.example;

// import java.util.Date;
import javax.persistence.*;

@Entity
public class Chami {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public String id;

    // @Column(nullable = false)
    // public Date birthday;

    @Column(name = "description", nullable = false)
    public String description;

}
