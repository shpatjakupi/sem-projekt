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
public class AgentsDTO {
    
    private int id;
    private String name;
    private String imageUrlAgents;

    public AgentsDTO(int id, String name, String imageUrlAgents) {
        this.id = id;
        this.name = name;
        this.imageUrlAgents = imageUrlAgents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrlAgents() {
        return imageUrlAgents;
    }

    public void setImageUrl(String imageUrlAgents) {
        this.imageUrlAgents = imageUrlAgents;
    }
    
    
    
}
