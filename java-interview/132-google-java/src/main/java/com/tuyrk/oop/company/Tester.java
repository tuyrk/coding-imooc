package com.tuyrk.oop.company;

import org.junit.Test;

import java.util.LinkedList;

public class Tester {
    @Test
    public void compare() {
        Employee employee1 = new Employee("John", 10000);
        Employee employee2 = new Employee("Mary", 20000);
        Employee employee3 = new Employee("John");
        // employee3.setSalary(10000);
        System.out.println("(employee1==employee3) = " + (employee1 == employee3));
        System.out.println("employee1.equals(employee3) = " + employee1.equals(employee3));
        System.out.println("employee2.equals(employee3) = " + employee2.equals(employee3));
        Employee employee4 = null;
        System.out.println("employee4.equals(employee3) = " + employee4.equals(employee3));

        System.out.println("employee2 = " + employee2);
    }

    @Test
    public void foreach() {
        Employee employee1 = new Employee("John", 10000);
        Employee employee2 = new Employee("Mary", 20000);
        Employee employee3 = new Employee("John");

        LinkedList<Employee> employees = new LinkedList<>();
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    @Test
    public void manager() {
        LinkedList<Employee> employees = new LinkedList<>();
        employees.add(new Employee("John", 10000));
        employees.add(new Employee("Mary", 20000));
        employees.add(new Employee("John"));

        Employee manager = new Manager("Tony", 100000, employees);
        employees.add(manager);

        for (Employee employee : employees) {
            System.out.println(employee);
        }

        manager.doWork();
    }
}
