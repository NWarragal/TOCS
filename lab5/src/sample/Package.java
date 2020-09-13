package sample;

public class Package {
    private byte header;
    private byte control;
    private byte destination;
    private byte source;
    private byte monitor;
    private byte data;

    Package() {
        this.control = 0;
        this.destination = 0;
        this.source = 0;
        this.monitor = 0;
    }

    public void setControl(byte control) {
        this.control = control;
    }

    public void setDestination(byte destination) {
        this.destination = destination;
    }

    public void setSource(byte source) {
        this.source = source;
    }


    public void setMonitor(byte monitor) {
        this.monitor = monitor;
    }

    public void setData(byte data){
        this.data = data;
    }

    public void setData(String data){
        this.data = data.getBytes()[0];
    }

    public byte getControl() {
        return control;
    }

    public byte getDestination() {
        return destination;
    }


    public byte getData() {
        return data;
    }

    public void freeData() {
        data = 0;
    }

    public byte getSource() {
        return source;
    }


    public byte getHeader() {
        return header;
    }

    public byte getMonitor() {
        return monitor;
    }

    public void setHeader(byte header) {
        this.header = header;
    }
}