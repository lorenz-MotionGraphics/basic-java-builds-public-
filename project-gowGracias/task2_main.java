/*
1. create a class Parent with method display();
2. in sub class, override display() but also call parent's version using super
*/
//sample code
class Parent {
	void display() {
		System.out.println("Parent display method");
	}
}

class Child extends Parent {
	@Override
	void display() {
		super.display();
		System.out.println("Child display methodd");
	}
}

class task2_main {
	public static void main(String args[]) {
		Child c = new Child();
		c.display();
	}
}