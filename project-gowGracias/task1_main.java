/*
1. create vehicle with a constructor printing "vehicle created"
2. create car subclass calling super constructor
3. demonstrate constructor chaining
*/
//sample code
class Vehicle {
	Vehicle() {
		System.out.println("vehicle created");
	}
}

class Car extends Vehicle {
	Car() {
		super();
		System.out.println("car created");
	}
}

	
class task1_main {
	public static void main(String args[]) {
		Car c = new Car();
	}
}