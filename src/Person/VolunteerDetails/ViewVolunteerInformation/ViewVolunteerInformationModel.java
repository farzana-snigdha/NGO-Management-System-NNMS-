package Person.VolunteerDetails.ViewVolunteerInformation;

import Utilities.OracleConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ViewVolunteerInformationModel {
    protected String[] showDonorDetails(String[] info, String Volunteer_id) {
        try {
            List<String> list = new ArrayList<>();
            String sql = "select * from Volunteer where Volunteer_id=?";
            OracleConnection oc = new OracleConnection();
            PreparedStatement ps = oc.conn.prepareStatement(sql);
            ps.setString(1, Volunteer_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                for (int i = 1; i < 8; i++) {
                    list.add(rs.getString(i));
                }
            }
            info = list.toArray(info);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
