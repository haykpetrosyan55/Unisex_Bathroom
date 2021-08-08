import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Bathroom bathroom = Bathroom.getInstance();

        List<Person> employees = new ArrayList<>();

        for (int i = 0; i < 14; i++) {
            // Creates man and woman
            if (new Random().nextInt(2) == 0) {
                // Creates man
                employees.add(new Person(("Man" + i), Sex.MAN, bathroom));
            } else {
                // Creates woman
                employees.add(new Person(("Woman" + i), Sex.WOMAN, bathroom));
            }
        }

        employees.stream().map(Thread::new).forEach(Thread::start);

        employees.stream().map(Thread::new).forEach((t) -> {
            try {
                t.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }
}
