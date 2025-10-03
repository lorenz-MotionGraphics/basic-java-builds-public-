/*
demonstrate how constructors are called in inheritance hierarchy using a person superclass and a student subclass
*/
//sample code
class person {
	person() {
		System.out.print("person constructor called");
	}
}

class Student extends person {
	Student() {
		System.out.print("student constructor called");
	}
}

class task4_main {
	public static void main(String args[]) {
		Student s = new Student();
	}
}