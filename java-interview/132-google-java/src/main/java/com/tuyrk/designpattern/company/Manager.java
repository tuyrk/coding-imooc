package com.tuyrk.designpattern.company;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manager implements Role {
    private final List<Employee> reporters;

    public Manager(List<Employee> reporters) {
        this.reporters = Collections.unmodifiableList(new ArrayList<>(reporters));
    }

    @Override
    public void doWork() {
        System.out.println("Dispatching work");
        Employee worker = selectReporters();
        worker.doWork();
    }

    @Override
    public String toString() {
        return "Manager{}";
    }

    private Employee selectReporters() {
        Assert.notEmpty(reporters, "Manager without reporters");
        return reporters.get(0);
    }
}
