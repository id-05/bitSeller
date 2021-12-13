package DAO;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BitSellerFilterWord {
    private Long id;
    private String user;
    private String word;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    BitSellerFilterWord(){

    }

    BitSellerFilterWord(String user, String word){
        this.user = user;
        this.word = word;
    }
}
