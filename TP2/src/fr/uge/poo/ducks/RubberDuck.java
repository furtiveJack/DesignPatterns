package fr.uge.poo.ducks;

public class RubberDuck implements Duck {

    private final String name;

    public RubberDuck(){
        this("Anonymous");
    }

    public RubberDuck(String name){
        this.name=name;
    }

    @Override
    public String quack() {
        return "RubberDuck["+name+"] refuses to quack.";
    }


}
