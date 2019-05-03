package rockets.model;

import java.util.Set;

public class PayloadValidation {

    public static void isEmpty(Set<String> identity){

        if(identity.isEmpty())
            throw new IllegalArgumentException("Set is empty");
    }

    public static void isEmpty(String str){

        if(str.isEmpty())
            throw new IllegalArgumentException("String is empty") ;

    }

    public static void NotZero(int weight){

        if(weight == 0)
            throw new IllegalArgumentException("Weight cannot be zero.");
    }
}
