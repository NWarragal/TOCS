package sample;

public class Send {

    private int attemptNumber = 10;
    private Boxes box;
    private double attemptCount = 0;


    public void setAttemptCount(int attemptCount) {
        this.attemptNumber = attemptCount;
    }

    Send(Boxes box) {
        this.box = box;
    }

    private boolean checkCollision(){
        return ((System.currentTimeMillis() % 6) == 1);
    }

    private void waiting(){
        try {
            Thread.sleep((long)(Math.random() * (Math.pow(2, (Math.min(attemptCount, 10))))));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void send(String message){
        box.listOutput.clear();
        box.listdeb.clear();
        box.listDebug.setItems(box.listdeb);
        attemptCount = 0;
        char[] msg;
        msg = message.toCharArray();
        for (int i = 0; i < message.length(); i++) {
            while (true) {
                if (((int) (System.currentTimeMillis()) % 2) == 0) {
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            char buf = msg[i];
            attemptCount = 0;
            while (true) {
                if (!checkCollision()) {
                    attemptCount++;
                    if (attemptNumber <= attemptCount) {
                        String deb = "";
                        for (int j = 0; j < attemptNumber; j++) {
                            deb += "x";
                        }
                        deb += "#";
                        box.listdeb.add(deb);
                        box.listDebug.setItems(box.listdeb);
                        break;
                    } else {
                        waiting();
                    }
                } else {
                    String deb = "";
                    for (int j = 0; j < attemptCount - 1; j++) {
                        deb += "x";
                    }
                    deb += "*";
                    box.listdeb.add(deb);
                    box.listDebug.setItems(box.listdeb);
                    String symb = box.listOutput.getText();
                    symb += buf;
                    box.listOutput.setText(symb);
                    break;
                }
            }
        }
    }
}
