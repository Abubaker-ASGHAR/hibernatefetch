package org.example;

import org.example.entity.Author;
import org.example.entity.Book;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create an author and books
        Author author = new Author("George Orwell1");
        author.addBook(new Book("198"));
        author.addBook(new Book("Animal Farm1"));

        // Save the author and books
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(author);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }

        // Fetch the author with books
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Author fetchedAuthor = session.get(Author.class, author.getId());
            System.out.println("Author: " + fetchedAuthor);
            System.out.println("Books:");
            for (Book book : fetchedAuthor.getBooks()) {
                System.out.println("- " + book.getTitle());
                // At this point, the author has NOT been loaded yet because of FetchType.LAZY
                System.out.println("- " + book.getAuthor());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
