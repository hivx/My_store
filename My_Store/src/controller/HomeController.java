package controller;

import entity.media.Media;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class controls the flow of events in homescreen
 *
 */
public class HomeController extends BaseController {

    /**
     * this method gets all Media in DB and return back to home to display
     *
     * @return List[Media]
     * @throws SQLException
     */
    public List getAllMedia() throws SQLException {
        return new Media().getAllMedia();
    }

    public List getAllType() throws SQLException {
        return new Media().getAllType();
    }

    /**
     * this method gets all Media in DB where title like "title" and return back to home to display
     *
     * @return List[Media]
     * @throws SQLException
     */
    public List getMediaByTitle(String title) throws SQLException {
        return new Media().getMediaByTitle(title);
    }

}
