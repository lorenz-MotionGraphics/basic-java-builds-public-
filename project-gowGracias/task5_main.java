/*
1. create a base class book with title and author
2. inherit Ebook with extra attribute filesize
3. inherit printedbook with extra attribute pages
4. display details of both
*/
//sample code
class Book {
	String title, author;
	Book(String title, String author) {
		this.title = title;
		this.author = author;
	}
}

class EBook extends Book {
	double fileSize;
	EBook(String title, String author, double fileSize) {
		super(title, author);
		this.fileSize = fileSize;
	}
	void display() {
		System.out.println("EBook: " + title + " by " + author + " (" + fileSize + "MB)");
	}
}

class PrintedBook extends Book {
	int pages;
	PrintedBook(String title, String author, int pages) {
		super(title, author);
		this.pages = pages;
	}
	void display() {
		System.out.println("PrintedBook: " + title + " by " + author + " (" + pages + " pages)");
	}
}

class task5_main {
	public static void main(String args[]) {
		EBook ebook = new EBook("java basics", "john doe", 2.5);
		PrintedBook pbook = new PrintedBook("advanced java", "jane smith", 450);
		ebook.display();
		pbook.display();
	}
}