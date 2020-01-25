package Response;

import Model.PersonM;

import java.util.ArrayList;

public class AllPersonResponse extends ResponseParent {
    private ArrayList<PersonM> data;

    public ArrayList<PersonM> getData() {
        return data;
    }
    public void setData(ArrayList<PersonM> data) {
        this.data = data;
    }
}
