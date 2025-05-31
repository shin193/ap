package Exercise.Ex5;

import java.util.ArrayList;


public class StringCounter {
    private ArrayList <String> items;
    private ArrayList <Integer> counter;
    ////////////////////////////////////////////
    public StringCounter() {
        this.items = new ArrayList();
        this.counter = new ArrayList();
    }
    //////////////////////////////////////////////
    public void addItem(String item) {
        int index = this.items.indexOf(item);
        if (index != -1) {
            this.counter.set(index, this.counter.get(index) + 1);
        }
        else {
            this.items.add(item);
            counter.add(1);
        }
    }
    ///////////////////////////////////////////////
    public ArrayList<String> getCounts() {
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> itemsPlus = new ArrayList<>(items);
        ArrayList<Integer> countsPlus = new ArrayList<>(counter);

        while (!countsPlus.isEmpty()) {
            int maxIndex = 0;
            for (int j = 1; j < countsPlus.size(); j++) {
                if (countsPlus.get(j) > countsPlus.get(maxIndex)) {
                    maxIndex = j;
                }
            }
            String entry = itemsPlus.get(maxIndex) + " : " + countsPlus.get(maxIndex);
            result.add(entry);
            itemsPlus.remove(maxIndex);
            countsPlus.remove(maxIndex);
        }
        return result;
    }

    }


