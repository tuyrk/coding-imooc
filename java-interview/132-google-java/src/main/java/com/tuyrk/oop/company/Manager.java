package com.tuyrk.oop.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Manager extends Employee {
    private final List<Employee> reporters; // = new ArrayList<>();

    public Manager(String name, int salary, List<Employee> reporters) {
        super(name, salary);
        this.reporters = Collections.unmodifiableList(new ArrayList<>(reporters));
    }

    @Override
    public void getPaid(BankEndPoint bank) {
        getStocks();
        super.getPaid(bank);
    }

    @Override
    public void doWork() {
        Employee worker = selectReporters();
        worker.doWork();
    }

    @Override
    public String toString() {
        return "Manager{name='" + super.getName() + '\'' + ", salary=" + super.getSalary() + '}';
    }

    private void getStocks() {

    }

    private Employee selectReporters() {
        loadReporters();
        return null;
    }

    private void loadReporters() {
        reporters.clear();
        reporters.add(new Employee("John", 10000));
        reporters.add(new Employee("Mary", 20000));
    }
}
