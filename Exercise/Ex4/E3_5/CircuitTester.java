package Exercise.Ex4.E3_5;

public class CircuitTester {
  private   int firstSwitch;
  private   int secondSwitch;
        public CircuitTester() {
            this.firstSwitch = 0;
            this.secondSwitch = 0;
        }

        public int  state() {
            return firstSwitch^secondSwitch;
        }

         void showAllSwitches() {
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < 2; i++) {
                    System.out.println("switch " + ":");
                    System.out.println(firstSwitch + " -> " + secondSwitch);
                    if (state() == 1) {
                        System.out.println("lamp is on");
                    } else {
                        System.out.println("lamp is off");
                    }
                    firstSwitch = 1 - firstSwitch;
                }
                secondSwitch = 1 - secondSwitch;
            }
    }
}
