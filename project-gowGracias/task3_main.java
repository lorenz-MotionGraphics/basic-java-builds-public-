/*
write a java program that demonstrates method overriding by creating a vehicle superclass and a car subclass that overrides aa method
*/
//sample code
class Vehicle {
	void move() {
		System.out.println("The vehicle moves.");
	}
}

class Car extends Vehicle {
	@Override
	void move() {
		System.out.println("the car drives on the road");
	}
}

class task3_main {
	public static void main(String args[]) {
		Vehicle v = new Car();
		v.move();
	}
}