package com.onlinebank.authService.entity;

import java.util.Set;

import com.onlinebank.common.util.Audit;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
/**
 * User entity representing system users.
 * Conforms to technical test specifications.
 *
 * @author OnlineBank Team
 * @version 1.0
 */
@Document(collection = "users")
public class User extends Audit {

    @Id
    private String id;

    @Field("name")
    private String name;


    @Indexed(unique = true)
    @Field("email")
    private String email;

    @Field("password")
    private String password;

    public User() {
        super();
    }

    public User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }
}