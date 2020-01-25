package Service;

import DataAccess.DataAccessException;
import DataAccess.Database;
import Response.ClearResponse;

public class Clear {
    public ClearResponse clear() {
        Database db = new Database();
        ClearResponse clearResponse = new ClearResponse();
        try {
            db.clearTables();
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            } catch (DataAccessException ex) {
                ex.printStackTrace();
            }
            clearResponse.setMessage("Error clearing database.");
        }
        if (db.isSuccess) {
            clearResponse.setMessage("Clear succeeded.");
        }
        return clearResponse;
    }
}
