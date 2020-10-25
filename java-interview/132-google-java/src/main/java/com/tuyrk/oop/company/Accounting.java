package com.tuyrk.oop.company;

public class Accounting {
    private BankEndPoint bank;

    public void payAll() {
        Employee.loadAllEmployees();
        for (Employee employee : Employee.allEmployees) {
            employee.getPaid(bank);
        }
    }
}
