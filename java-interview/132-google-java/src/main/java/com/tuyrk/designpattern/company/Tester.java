package com.tuyrk.designpattern.company;

import org.junit.Test;

import java.util.*;

public class Tester {
    @Test
    public void test() {
        Employee employee1 = new Employee("John", 10000, new Engineer());
        Employee employee2 = new Employee("Mary", 20000, new Engineer());
        List<Employee> employees = new LinkedList<>();
        employees.add(employee1);
        employees.add(employee2);

        System.out.println("Print using for each:");
        for (Employee employee : employees) {
            System.out.println(employee);
        }

        System.out.println("Testing managers:");
        employee2.setRole(new Manager(Collections.singletonList(employee1)));
        for (Employee employee : employees) {
            System.out.println(employee);
        }

        System.out.println("Testing doWork:");
        System.out.print("Employee1:");
        employee1.doWork();
        System.out.print("Employee2:");
        employee2.doWork();
    }
}
