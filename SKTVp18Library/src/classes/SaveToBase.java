package classes;

import interfaces.Saveble;
import entity.Book;
import entity.History;
import entity.Reader;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class SaveToBase implements Saveble {
    EntityManager em;
    EntityTransaction tx;
    

    public SaveToBase() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SKTVp18LibraryPU");
        em = emf.createEntityManager();
        tx = em.getTransaction();
    }
    
    @Override
    public void saveBooks(List<Book> listBooks){
        List<Book> listBooksSaved = loadBooks();
        tx.begin();
            for(int i=0; i<listBooks.size();i++){
                if(listBooksSaved.contains(listBooks.get(i))
                        && !listBooksSaved.get(i).equals(listBooks.get(i))){
                    em.merge(listBooks.get(i));
                }else{
                    em.persist(listBooks.get(i));
                }
            }
        tx.commit();
    }
    @Override
    public List<Book> loadBooks(){
        return em.createQuery("SELECT b FROM Book b")
                .getResultList();
    }
    @Override
    public void saveReaders(List<Reader> listReaders){
        List<Reader> listReadersSaved = loadReaders();
        tx.begin();
            for(int i=0; i<listReaders.size();i++){
                if(listReadersSaved.contains(listReaders.get(i))
                        && !listReadersSaved.get(i).equals(listReaders.get(i))){
                    em.merge(listReaders.get(i));
                }else{
                    em.persist(listReaders.get(i));
                }
            }
        tx.commit();
    }
    @Override
    public List<Reader> loadReaders(){
        return em.createQuery("SELECT r FROM Reader r")
                .getResultList();
    }
    @Override
    public void saveHistories(List<History> listHistories) {
        List<History> listHistoriesSaved = null;
        
            for(int i=0; i<listHistories.size();i++){
                listHistoriesSaved = loadHistories();
                tx.begin();
                    if(listHistoriesSaved.contains(listHistories.get(i))){
                        if(!listHistoriesSaved.get(i).equals(listHistories.get(i))){
                            em.merge(listHistories.get(i).getBook());
                            em.merge(listHistories.get(i));
                        }
                    }else{
                        em.persist(listHistories.get(i));  
                        em.merge(listHistories.get(i).getBook());
                    }
                tx.commit();
            }
        
    }
    @Override
   public List<History> loadHistories() {
        return em.createQuery("SELECT h FROM History h")
                .getResultList();
    }
}