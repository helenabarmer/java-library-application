package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibrarySystem implements Serializable {

    private ArrayList<Book> availableBooks = new ArrayList<>();
    private ArrayList<Book> addedBooks = new ArrayList<>();
    private ArrayList<Book> borrowedBooks = new ArrayList<>();
    private String outputFile = "output.ser";
    private String borrowedBooksFile = "borrow.ser";
    transient private Scanner input = new Scanner(System.in);


    public LibrarySystem() {
        //FileUtility.saveObject(outputFile, addedBooks, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public void addNewBook(){
        System.out.println("Please add the book title: ");
        String bookTitle = input.nextLine();
        System.out.println("Please add the author: ");
        String author = input.nextLine();
        System.out.println("Please add information about the book: ");
        String aboutThisBook = input.nextLine();
        Book newBook = new Book(bookTitle, author, aboutThisBook);

        saveFile(outputFile, newBook, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        addedBooks.add(newBook);
        //saveNewFile(outputFile, addedBooks);
    }

    public void saveNewFile(String filename, ArrayList<Book> b){
        FileUtility.saveObject(filename, b, StandardOpenOption.CREATE);
    }


    public void allLibraryBooks(){
        loadFile(outputFile, addedBooks);
        for(Book book: addedBooks){
            System.out.println(book);
            System.out.println("BOOKS!");
        }



        /*List<Book> books = (List<Book>)FileUtility.loadObject(outputFile);
        for(Book book: books){
            System.out.println("The books are: " + book);
        }*/

    }



    public void searchBookTitle(){
        System.out.println("Search title: ");
        String searchTitle = input.nextLine();

        List<Book> searchBooks = (List<Book>)FileUtility.loadObject(outputFile);
        for(Book book: searchBooks){
            if(searchTitle.equals(book.getBookTitle())){
                System.out.print("You searched for this book and we found it! " + "\n" + book.toString() + "\n");
                System.out.println("Would you like to rent this book? [Y]/[N]");
                String choice = input.nextLine();

                switch (choice){
                    case "Y":
                        System.out.println("You will now borrow the book: " + book.getBookTitle() + "\n");
                        searchBooks.remove(book);
                        addedBooks.remove(book);
                        saveFile(outputFile, searchBooks, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        saveFile(outputFile, addedBooks, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

                        borrowedBooks.add(book);
                        saveFile(borrowedBooksFile, borrowedBooks, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        System.out.println("Can you read this? " + book.toString());
                        break;

                    case "N":
                        System.out.println("NOT RENTING");
                        break;

                    default:
                        break;
                }

                break;
            }
            else if(!searchTitle.equals(book.getBookTitle())){
                System.out.println("Sorry we could not find the book you were looking for. ");
                break;
            }
        }
    }

    public void searchAuthor(){
        System.out.println("Search author: ");
        String searchAuthor = input.nextLine();

        List<Book> searchBooks = (List<Book>)FileUtility.loadObject(outputFile);
        for(Book book: searchBooks){
            if(searchAuthor.equals(book.getAuthor())){
                System.out.print("You searched for this author and we found it! " + "\n" + book.toString() + "\n");
                System.out.println("Would you like to rent this book? [Y]/[N]");
                String choice = input.nextLine();

                switch (choice){
                    case "Y":
                        System.out.println("You will now borrow the book: " + book.getBookTitle() + "\n");
                        borrowBook(book);
                        System.out.println("ENDING");
                        break;

                    case "N":
                        System.out.println("NOT RENTING");
                        break;

                    default:
                        break;
                }

                break;
            }
            else if(!searchAuthor.equals(book.getAuthor())){
                System.out.println("Sorry we could not find the book you were looking for. ");
                break;
            }
        }

    }

    public void borrowBook(Book b){
        System.out.println("Inside borrow book! ");

        addedBooks.remove(b);
        borrowedBooks.add(b);

        saveFile(borrowedBooksFile, addedBooks, StandardOpenOption.CREATE, StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND);

        List<Book> borrowed = (List<Book>)FileUtility.loadObject(borrowedBooksFile);
        for(Book bk: borrowed){
            System.out.println("Borrowed books: " + bk);
        }

        saveFile(outputFile, addedBooks, StandardOpenOption.CREATE, StandardOpenOption.CREATE_NEW, StandardOpenOption.APPEND);

        List<Book> newAddedBooks = (List<Book>)FileUtility.loadObject(outputFile);
        for(Book bkk: newAddedBooks){
            System.out.println("Available books: " + bkk);
        }
        }

        public void myBorrowedBooks(){
        List<Book> userBooks = (List<Book>)FileUtility.loadObject(borrowedBooksFile);
        for(Book b: userBooks){
            System.out.println("The borrowed books: " + b.toString());
        }
        }



    public void saveFile(String filename, Object o, StandardOpenOption... option) {
        Path path = Paths.get(filename);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(path, option));
            oos.writeObject(o);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Saved filed successfully! " + o.toString());
    }



    public Object loadFile(String filename, ArrayList<Book> bookFiles){
        Path path = Paths.get(filename);
        try {
            ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(path));
            try {
                bookFiles = (ArrayList<Book>) ois.readObject();
                for(Book book: bookFiles){
                    System.out.println("Object: " + "\n" + book);
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookFiles;
    }
}