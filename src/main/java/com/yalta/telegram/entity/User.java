package com.yalta.telegram.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class User {

    @Id
    private Long id;
    private Long chatId;
    private String username;
    private String firstName;
    private String lastName;
                                //todo add state
}
