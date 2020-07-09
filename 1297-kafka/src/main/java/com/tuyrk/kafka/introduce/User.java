package com.tuyrk.kafka.introduce;

import lombok.Data;

import java.io.Serializable;

/**
 * @author tuyrk
 */
@Data
public class User implements Serializable {
    private String id;
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
