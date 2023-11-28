package com.example.project;

public class Employee {

    private int id;
    private  String nom;
    private  int age;
    private String dep;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", age=" + age +
                ", dep='" + dep + '\'' +
                '}';
    }

    public Employee() {
    }

    public Employee(String nom, int age, String dep) {
        this.nom = nom;
        this.age = age;
        this.dep = dep;
    }

    public Employee(int id, String nom, int age, String dep) {
        this.id = id;
        this.nom = nom;
        this.age = age;
        this.dep = dep;
    }
}
