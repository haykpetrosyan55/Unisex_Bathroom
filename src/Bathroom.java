import java.util.LinkedHashSet;
import java.util.concurrent.Semaphore;

public class Bathroom {

    private static final int CAPACITY = 3;
    private static final Bathroom instance = new Bathroom(CAPACITY);
    private Sex currentSex;
    private final int capacity;
    private final LinkedHashSet<Person> employees;

    // Semaphore
    private final Semaphore semaphore;
    private final Semaphore leftMutex;
    private final Semaphore enterMutex;

    public Bathroom(int capacity) {
        this.capacity = capacity;
        this.currentSex = Sex.NONE;
        this.employees = new LinkedHashSet<>();
        this.semaphore = new Semaphore(this.capacity, true);
        this.leftMutex = new Semaphore(1, true);
        this.enterMutex = new Semaphore(1, true);
    }

    public static Bathroom getInstance() {
        return instance;
    }

    public void addUser(Person person) {
        try {
            this.semaphore.acquire();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        if (this.isEmpty()) {
            this.currentSex = person.getSex();
        }

        if (!this.isFull() && !this.employees.contains(person)
                && getCurrentSex().equals(person.getSex())) {
            try {
                this.enterMutex.acquire();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            if (this.employees.add(person)) {
                System.out.println(person.getName() + " entered the bathroom");
            }
            this.enterMutex.release();

            if (this.isFull()) {
                System.out.println("The bathroom is full");
            }
        }
    }

    public void removeUser(Person person) {
        this.semaphore.release();

        if (!this.isEmpty()) {
            try {
                this.leftMutex.acquire();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if (this.employees.remove(person)) {
                System.out.println(person.getName() + " left the bathroom");
            }
            this.leftMutex.release();

            if (this.isEmpty()) {
                System.out.println("The bathroom is empty");
                this.currentSex = Sex.NONE;
            }
        }
    }

    public boolean isInTheBathroom(Person person) {
        return this.employees.contains(person);
    }

    public boolean isFull() {
        return this.capacity == this.employees.size();
    }

    public boolean isEmpty() {
        return this.employees.isEmpty();
    }

    public Sex getCurrentSex() {
        return this.currentSex;
    }

    @Override
    public String toString() {
        return "Bathroom{" + "currentSex = " + this.currentSex
                + ", capacity = " + this.capacity
                + ", numberOfUsers = " + this.employees.size() + '}';
    }
}
