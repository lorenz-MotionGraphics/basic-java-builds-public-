class main {


	static String a = "a";
	static int b = 105;
	static int c = 205;

	static void heyho() {
		System.out.print(" 04/10/2025");
	}

	public static void hellofun() {
		System.out.print(" lorenz introductory to java");
	}

	public static void main(String args[]) {
		for(int i = 0; i < 150; i++) {
			System.out.print("-");
		}
		
		System.out.print("\nhello world" + " " + a);
		hellofun();
		heyho();
		System.out.print("\n");

		for(int i = 0; i < 150; i++) {
			System.out.print("-");
		}

		System.out.print("\n");
		System.out.print("\ncalculatory data");
		System.out.print("\n");
		System.out.print("\nAddition	division			multiplication 			subtraction");
		System.out.print("\n" + (b + c) + "		" + (b % c) + "				" + (b * c) + " 				" + (b / c));
		System.out.println("\n");
		for(int i = 0; i < 150; i++) {
			System.out.print("-");
		}
	}
}