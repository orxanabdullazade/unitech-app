package az.unibank.unitechapp.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "accounts")
@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double balance;

    private int status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
