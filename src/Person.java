
public class Person implements Runnable{

    private final String name;
    private final Sex sex;
    private final Bathroom bathroom;
    private boolean canLeave;
    private boolean needBathroom;

    public Person(String name, Sex sex, Bathroom bathroom) {
        this.name = name;
        this.sex = sex;
        this.bathroom = bathroom;
        this.canLeave = false;
        this.needBathroom = true;
    }

    public void useBathroom() {
        this.bathroom.addUser(this);
        if (this.bathroom.isInTheBathroom(this)) {
            try {
                Thread.sleep(500);
                this.canLeave = true;
                System.out.println(getName() + " Done");
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void leaveBathroom() {
        this.bathroom.removeUser(this);
        this.canLeave = false;
        this.needBathroom = false;
    }

    public String getName() {
        return this.name;
    }

    public Sex getSex() {
        return this.sex;
    }

    @Override
    public void run() {
        System.out.println(this.getName());
        // If the person needs to go to the bathroom
        while (this.needBathroom) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // If they want to use
            if ((this.bathroom.getCurrentSex().equals(this.getSex())
                    || this.bathroom.getCurrentSex().equals(Sex.NONE))
                    && !this.bathroom.isFull()
                    && !this.bathroom.isInTheBathroom(this)) {
                this.useBathroom();
            }
            // If they want to leave
            if (this.canLeave) {
                this.leaveBathroom();
            }
        }
    }

    @Override
    public String toString() {
        return "Person{" + "name = " + this.name + ", sex = " + this.sex + '}';
    }
}
