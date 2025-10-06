/**
 * OOPDemo.java
 * This program demonstrates the four core principles of Object-Oriented Programming (OOP) in Java:
 * 1. Abstraction (via Abstract Class and Interface)
 * 2. Inheritance (Dog and Cat extend Animal)
 * 3. Encapsulation (Private fields with public getters/setters in the Dog class)
 * 4. Polymorphism (Method overriding and interface implementation)
 */

import java.util.ArrayList;
import java.util.List;

// 1. ABSTRACTION (via Interface)
// Defines a contract for any class that can move.
interface Movable {
    /**
     * Describes the action of movement.
     */
    void move();
}

// 1. ABSTRACTION (via Abstract Class)
// Represents the general concept of an Animal, which cannot be instantiated directly.
abstract class Animal {
    // Fields common to all animals (protected for access by subclasses)
    protected String name;
    protected int age;

    /**
     * Constructor for the Animal class.
     * @param name The name of the animal.
     * @param age The age of the animal.
     */
    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("-> Animal created: " + this.name);
    }

    // 1. ABSTRACTION (Abstract Method)
    // Subclasses MUST provide an implementation for this method.
    public abstract void makeSound();

    // Concrete method inherited by subclasses
    public void sleep() {
        System.out.println(name + " is sleeping peacefully.");
    }
    
    // Getter for name
    public String getName() {
        return name;
    }
}

// 2. INHERITANCE: Dog inherits from Animal
// 3. POLYMORPHISM: Dog implements the Movable interface and overrides makeSound()
class Dog extends Animal implements Movable {
    // 4. ENCAPSULATION: private field, accessible only via public methods
    private String breed;

    public Dog(String name, int age, String breed) {
        // Calls the constructor of the parent class (Animal)
        super(name, age);
        this.breed = breed;
    }

    // 4. ENCAPSULATION: Public getter and setter for the private field 'breed'
    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
        System.out.println(name + "'s breed updated to " + breed);
    }

    // POLYMORPHISM (Method Overriding): Provides the concrete implementation
    // for the abstract method inherited from Animal.
    @Override
    public void makeSound() {
        System.out.println(name + " the Dog says: Woof! Woof!");
    }

    // POLYMORPHISM (Interface Implementation): Provides the concrete implementation
    // for the method defined in the Movable interface.
    @Override
    public void move() {
        System.out.println(name + " is running on four paws.");
    }
}

// 2. INHERITANCE: Cat inherits from Animal
// 3. POLYMORPHISM: Cat implements the Movable interface and overrides makeSound()
class Cat extends Animal implements Movable {
    private String color;
    
    public Cat(String name, int age, String color) {
        super(name, age);
        this.color = color;
    }

    // POLYMORPHISM (Method Overriding)
    @Override
    public void makeSound() {
        System.out.println(name + " the Cat says: Meow.");
    }

    // POLYMORPHISM (Interface Implementation)
    @Override
    public void move() {
        System.out.println(name + " is gracefully stalking prey.");
    }
}


// Main class to execute the demonstration
public class OOPdemo {

    public static void main(String[] args) {
        System.out.println("--- 2. Inheritance & 3. Polymorphism Demo ---");

        // Create objects of the concrete classes
        Dog myDog = new Dog("Buddy", 3, "Golden Retriever");
        Cat myCat = new Cat("Whiskers", 5, "Tabby");

        // Demonstrate 3. POLYMORPHISM (Runtime): Calling the abstract method via concrete types
        myDog.makeSound(); // Dog's implementation
        myCat.makeSound(); // Cat's implementation
        myDog.sleep();     // Inherited concrete method
        
        System.out.println("\n--- 4. Encapsulation Demo ---");
        // Demonstrate 4. ENCAPSULATION: Accessing private data via public methods
        System.out.println(myDog.getName() + " is a " + myDog.getBreed());
        myDog.setBreed("Labrador Mix");
        System.out.println(myDog.getName() + "'s new breed is " + myDog.getBreed());


        System.out.println("\n--- 3. Polymorphism (Collection and Abstract Type) Demo ---");

        // POLYMORPHISM: Using a list of the ABSTRACTION type (Animal)
        // This allows us to treat Dogs and Cats uniformly.
        List<Animal> pets = new ArrayList<>();
        pets.add(myDog);
        pets.add(myCat);
        
        for (Animal pet : pets) {
            // Runtime Polymorphism: The JVM decides which makeSound() method to call
            // based on the actual object type (Dog or Cat), not the reference type (Animal).
            System.out.print(pet.getName() + ": ");
            pet.makeSound();
        }

        System.out.println("\n--- 3. Polymorphism (Interface Type) Demo ---");
        
        // POLYMORPHISM: Using a list of the INTERFACE type (Movable)
        List<Movable> movers = new ArrayList<>();
        movers.add(myDog);
        movers.add(myCat);
        
        for (Movable mover : movers) {
            // Polymorphism in action again, treating all movers the same way.
            mover.move();
        }
    }
}
