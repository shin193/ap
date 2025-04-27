package Exercise.Ex4.E3_4;

public class HallwayLightControl {
    private int firstSwitch;  // 0: down, 1: up
    private int secondSwitch; // 0: down, 1: up

    public HallwayLightControl() {
        firstSwitch = 0;
        secondSwitch = 0;
    }

    public int getFirstSwitchState() {
        return firstSwitch;
    }

    public int getSecondSwitchState() {
        return secondSwitch;
    }

    public int getLampState() {
        return (firstSwitch ^ secondSwitch);
    }

    public void toggleFirstSwitch() {
        firstSwitch = 1 - firstSwitch;
    }

    public void toggleSecondSwitch() {
        secondSwitch = 1 - secondSwitch;
    }
}