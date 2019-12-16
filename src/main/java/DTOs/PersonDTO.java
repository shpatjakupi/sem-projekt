/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTOs;

public class PersonDTO {
    private String name;
    private String height;
    private String gender;

    public PersonDTO() {
    }

    public PersonDTO(String name, String height, String gender) {
        this.name = name;
        this.height = height;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "PersonDTO{" + "name=" + name + ", height=" + height + ", gender=" + gender + '}';
    }
    
    
}