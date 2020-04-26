package begin.util;

import java.util.ArrayList;

class Data {

    private String data;
    private int id;
    Data(String data, int id) {
        this.data = data;
        this.id = id;
    }
}

public class Test {

    public static void main(String[] args) {

        ArrayList<Data> list = new ArrayList<>();
        while (true) {
            for (int i = 0; i<1000; i++) {
                Data data = new Data("hello I am data field hello I am data field hello I am data field hello I am data field" + i, i);
                if (i % 10 == 0) {
                    list.add(data);
                    //System.out.println("list add " + data);
                }
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
