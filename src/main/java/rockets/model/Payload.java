package rockets.model;

import java.util.Set;

import static org.apache.commons.lang3.Validate.notNull;

public class Payload {

    private String type;

    private Set<String> identity;

    private int weight;

    public Payload(String type, Set<String> identity ,int weight){
        notNull(type);
        PayloadValidation.NotZero(weight);

        this.type = type;
        this.identity = identity;
        this.weight = weight;
    }

    public String getType(){
        return type;
    }

    public int getWeight(){

        return weight;
    }

    public void setType(String type){this.type = type;}



    public void setIdentity(Set<String> identity){

        notNull(identity);
       PayloadValidation.isEmpty(identity);

        this.identity = identity;
    }

    public Set<String> getIdentity(){
        notNull(identity);
       PayloadValidation.isEmpty(identity);
        return identity;
    }
}
