package dev.faruk.commoncodebase.entity;

import dev.faruk.commoncodebase.logging.SensitiveDataType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user", schema = "public")
public class AppUser implements SensitiveDataType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "full_name")
    private String name;

    @Column(name = "pw")
    private String password;

    @Column(name = "deleted", columnDefinition = "boolean default false", nullable = false, insertable = false)
    private Boolean deleted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_user_bridge",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<AppUserRole> roles;

    public void add(AppUserRole userRole) {
        if (roles == null) roles = new ArrayList<>();
        roles.add(userRole);
    }

    @Override
    public String toVisualString() {
        return new StringBuilder()
                .append("AppUser{")
                .append("id=").append(id)
                .append(", username='").append(username).append('\'')
                .append(", name='").append(name).append('\'')
                .append(", password='").append("********").append('\'')
                .append(", deleted=").append(deleted)
                .append(", roles=").append(roles).append('}').toString();
    }

    @Override
    public String toString() {
        return toVisualString();
    }
}
