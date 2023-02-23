package co.antonis.gwt.example.client.model.exception;


import co.antonis.gwt.example.client.model.others.InfoMessageStructure;

public class GenericException extends Exception {
    InfoMessageStructure info;

    public GenericException() {
    }

    public GenericException setInfo(InfoMessageStructure info) {
        this.info = info;
        return this;
    }

    public InfoMessageStructure getInfo(){
        return info;
    }
}
