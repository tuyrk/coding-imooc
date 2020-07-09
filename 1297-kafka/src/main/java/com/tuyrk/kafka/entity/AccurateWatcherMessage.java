package com.tuyrk.kafka.entity;

import lombok.Data;

@Data
public class AccurateWatcherMessage {
    private String title;
    private String application;
    private String level;
    private String body;
    private String executionTime;
}
