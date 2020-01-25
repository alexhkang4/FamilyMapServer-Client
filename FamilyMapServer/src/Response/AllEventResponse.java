package Response;

import Model.EventM;

import java.util.ArrayList;

public class AllEventResponse extends ResponseParent{
    private ArrayList<EventM> data;

    public ArrayList<EventM> getData() {
        return data;
    }
    public void setData(ArrayList<EventM> data) {
        this.data = data;
    }

}
