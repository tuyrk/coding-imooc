package com.tuyrk.oop.company;

import java.util.List;
import java.util.Objects;

public class Employee {
    public static List<Employee> allEmployees;

    private final String name;
    private final int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }

    public Employee(String name) {
        this(name, 0);
    }

    public void doWork() {
        loadAllEmployees();
    }

    public void getPaid(BankEndPoint bank) {
        bank.payment(name, salary);
    }

    public static void loadAllEmployees() {
        // loads all employees from database.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return salary == employee.salary &&
                Objects.equals(name, employee.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, salary);
    }

    @Override
    public String toString() {
        return "Employee{name='" + name + '\'' + ", salary=" + salary + '}';
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }
}
