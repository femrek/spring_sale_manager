package dev.faruk.commoncodebase.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "pw")
    private String password;

    @Column(name = "deleted")
    private boolean deleted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_user_bridge",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<AppUserRole> roles;

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void add(AppUserRole userRole) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(userRole);
    }
}
