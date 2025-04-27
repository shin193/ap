package Exercise.Ex4.E3_4;


public class Ex4_E3_4 {
    public static void main(String[] args) {
        HallwayLightControl hallway = new HallwayLightControl();
        System.out.println( "lamp 1 state :"+hallway.getFirstSwitchState());
        System.out.println( "lamp 2 state :"+hallway.getSecondSwitchState());
        System.out.println("Lamp: " + hallway.getLampState());
        hallway.toggleFirstSwitch();
        System.out.println( "lamp 1 state :"+hallway.getFirstSwitchState());
        System.out.println( "lamp 2 state :"+hallway.getSecondSwitchState());
        System.out.println("Lamp: " + hallway.getLampState());
        hallway.toggleSecondSwitch();
        System.out.println( "lamp 1 state :"+hallway.getFirstSwitchState());
        System.out.println( "lamp 2 state :"+hallway.getSecondSwitchState());
        System.out.println("Lamp: " + hallway.getLampState());
        hallway.toggleFirstSwitch();
        System.out.println( "lamp 1 state :"+hallway.getFirstSwitchState());
        System.out.println( "lamp 2 state :"+hallway.getSecondSwitchState());
        System.out.println("Lamp: " + hallway.getLampState());
    }
}
