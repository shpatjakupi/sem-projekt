/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author Younes
 */
public class CarriersDTO {
    private int id;
    private String code;
    private String name;
    private String imageUrl;

    public CarriersDTO(int id, String code, String name, String imageUrl) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.imageUrl = imageUrl;
    }
    
    
}
