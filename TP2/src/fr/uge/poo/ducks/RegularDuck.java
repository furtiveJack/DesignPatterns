package fr.uge.poo.ducks;

public class RegularDuck implements Duck {

    private final String name;

    public RegularDuck(){
        this("Anonymous");
    }

    public RegularDuck(String name){
        this.name=name;
    }

    @Override
    public String quack() {
        return "RegularDuck["+name+"] says quack.";
    }

}
