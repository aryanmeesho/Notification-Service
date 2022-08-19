package com.meesho.notificationservice.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Entity
@Table(name = "blacklist_numbers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistNumber {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    public BlacklistNumber (String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

}
