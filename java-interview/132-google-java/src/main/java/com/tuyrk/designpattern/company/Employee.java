package com.tuyrk.designpattern.company;

import com.tuyrk.oop.company.BankEndPoint;

import java.util.Objects;

public class Employee {
    private final String name;
    private final int salary;
    private Role role;

    public Employee(String name, int salary, Role role) {
        this.name = name;
        this.salary = salary;
        this.role = role;
    }

    public void doWork() {
        role.doWork();
    }

    public void getPaid(BankEndPoint bank) {
        bank.payment(name, salary);
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
                Objects.equals(name, employee.name) &&
                Objects.equals(role, employee.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, salary, role);
    }

    @Override
    public String toString() {
        return "Employee{name='" + name + '\'' + ", salary=" + salary + ", role=" + role + '}';
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
