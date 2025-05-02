package Repositories;

import entities.*;
import utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventRepository {
    Connection connection;
    public EventRepository() {
        connection = Database.getInstance().getConnection();
    }
    public void createEvent(Event event) {
        String sql = "INSERT INTO `event` (`name`, `description`, `startDate`, `endDtae`, `duration`, `price`, `finalPrice`, `nbParticipant`, `maxParticipant`, `vuesNb`, `fidelityPoints`, `visa`, `idCreator`, `promotionRate`, `isDeleted`, `genreEvent`, `statusEvent`, `typeEvent`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, event.getName());
            ps.setString(2, event.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(event.getStartDate()));
            ps.setDate(4, java.sql.Date.valueOf(event.getEndDate()));
            ps.setInt(5, event.getDuration());
            ps.setFloat(6, event.getPrice());
            ps.setFloat(7, event.getFinalPrice());
            ps.setInt(8, event.getNbParticipant());
            ps.setInt(9, event.getMaxParticipants());
            ps.setInt(10, event.getVuesNb());
            ps.setInt(11, event.getFidelityPoints());
            ps.setBoolean(12, event.isVisa());
            ps.setInt(13, event.getIdCreator());
            ps.setFloat(14, event.getPromotionRate());
            ps.setBoolean(15, event.isDeleted());
            ps.setString(16, event.getGenreEvent().name());
            ps.setString(17, event.getStatusEvent().name());
            ps.setString(18, event.getTypeEvent().name());
            ps.executeUpdate();
            System.out.println("Event added successfully");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deleteEvent(int idEvent) {
        String sql = "UPDATE event set isDeleted = true where idEvent = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idEvent);
            ps.executeUpdate();
            System.out.println("Event deleted successfully");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public void updateEvent(Event event,int id) {
            String sql = "UPDATE event SET name=?, description=?, startDate=?, endDate=?, duration=?, price=?, nbParticipant=?, " +
                    "maxParticipants=?, VuesNb=?, FidelityPoints=?, visa=?, idCreator=?, promotionRate=?, finalPrice=?, isDeleted=?, " +
                    "genreEvent=?, statusEvent=?, typeEvent=? WHERE idEvent=?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, event.getName());
                ps.setString(2, event.getDescription());
                ps.setDate(3, java.sql.Date.valueOf(event.getStartDate()));
                ps.setDate(4, java.sql.Date.valueOf(event.getEndDate()));
                ps.setInt(5, event.getDuration());
                ps.setFloat(6, event.getPrice());
                ps.setInt(7, event.getNbParticipant());
                ps.setInt(8, event.getMaxParticipants());
                ps.setInt(9, event.getVuesNb());
                ps.setInt(10, event.getFidelityPoints());
                ps.setBoolean(11, event.isVisa());
                ps.setInt(12, event.getIdCreator());
                ps.setFloat(13, event.getPromotionRate());
                ps.setFloat(14, event.getFinalPrice());
                ps.setBoolean(15, event.isDeleted());
                ps.setString(16, event.getGenreEvent().name());
                ps.setString(17, event.getStatusEvent().name());
                ps.setString(18, event.getTypeEvent().name());
                ps.setInt(19, id);
                ps.executeUpdate();
                System.out.println("Event updated successfully");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }

    public List<Event> getAllEvents() {
        String sql = "SELECT * FROM event where isDeleted=false";
        List<Event> events = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return events;
    }
    public List<Event> getDeletedEvents() {
        String sql = "SELECT * FROM event where isDeleted=true";
        List<Event> events = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return events;
    }

    private Event mapResultSetToEvent(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setIdEvent(rs.getInt("idEvent"));
        event.setName(rs.getString("name"));
        event.setDescription(rs.getString("description"));
        event.setStartDate(rs.getDate("startDate").toLocalDate());
        event.setEndDate(rs.getDate("endDate").toLocalDate());
        event.setDuration(rs.getInt("duration"));
        event.setPrice(rs.getFloat("price"));
        event.setNbParticipant(rs.getInt("nbParticipant"));
        event.setMaxParticipants(rs.getInt("maxParticipants"));
        event.setVuesNb(rs.getInt("VuesNb"));
        event.setFidelityPoints(rs.getInt("FidelityPoints"));
        event.setVisa(rs.getBoolean("visa"));
        event.setIdCreator(rs.getInt("idCreator"));
        event.setPromotionRate(rs.getFloat("promotionRate"));
        event.setFinalPrice(rs.getFloat("finalPrice"));
        event.setDeleted(rs.getBoolean("isDeleted"));
        event.setGenreEvent(GenreEvent.valueOf(rs.getString("genreEvent")));
        event.setStatusEvent(StatusEvent.valueOf(rs.getString("statusEvent")));
        event.setTypeEvent(TypeEvent.valueOf(rs.getString("typeEvent")));
        // Note: listParticipantsID is not handled here — you might need a separate table
        return event;
    }

    public Event getEventById(int idEvent) {
        String sql = "SELECT * FROM event  WHERE idEvent=? and isDeleted=false";
        Event event = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idEvent);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                event = mapResultSetToEvent(rs);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return event;
    }
    public List<Event> getEventByGenre(TypeEvent genre) {
        String sql = "SELECT * FROM event WHERE genreEvent=? and isDeleted=false";
        List<Event> events = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, genre.name());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return events;
    }

    public List<Event> getEventByType(TypeEvent type) {
        String sql = "SELECT * FROM event WHERE typeEvent=? and isDeleted=false";
        List<Event> events = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, type.name());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return events;
    }
    public List<Event> getEventByStatus(StatusEvent status) {
        String sql = "SELECT * FROM event WHERE statusEvent=? and isDeleted=false";
        List<Event> events = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return events;
    }
    public List<Event> getEventByPriceRange(float price1, float price2) {
        String sql = "SELECT * FROM event WHERE price between ? and ? and isDeleted=false";
        List<Event> events = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setFloat(1, price1);
            ps.setFloat(2, price2);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                events.add(mapResultSetToEvent(rs));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return events;
    }

    public void postponeEvent(Event event, Date date1, Date date2) {
        String query = "Update event set startDate = ?, endDate = ? where idEvent = ? and isDeleted=false";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, (java.sql.Date) date1);
            ps.setDate(2, (java.sql.Date) date2);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public boolean updateStatus(int id, StatusEvent statusEvent) {
       String query = "Update event set statusEvent = ? where idEvent = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, statusEvent.name());
            ps.setInt(2, id);
            ps.executeUpdate();
            System.out.println("Event updated successfully");
            return true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void addParticipant(User user,Event event) {
        int idUser = user.getId();
        int idEevent = event.getIdEvent();
        String sql = "INSERT INTO event_participant (id_participant,id_event) VALUES (?,?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idUser);
            ps.setInt(2, idEevent);
            ps.executeUpdate();
            System.out.println("User added to event successfully");
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        String sql1 = "UPDATE event set nbParticipant = ? where idEvent = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql1)){
            ps.setInt(1, event.getNbParticipant()+1);
            ps.setInt(2, idEevent);
            ps.executeUpdate();
            System.out.println("Event updated successfully");
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public void deleteParticipant(User user,Event event ) {
        int idUser = user.getId();
        int idEvent = event.getIdEvent();
        String sql = "DELETE FROM event_participant WHERE id_participant = ? and id_event = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, idUser);
            ps.setInt(2, idEvent);
            ps.executeUpdate();
            System.out.println("Event updated successfully");
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        String sql1 = "UPDATE event set nbParticipant = ? where idEvent = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql1)){
            ps.setInt(1, event.getNbParticipant()-1);
            ps.setInt(2, idEvent);
            ps.executeUpdate();
            System.out.println("Event updated successfully");
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public void add_vues(Event event){
        String sql = "UPDATE event set VuesNb = ? where idEvent = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1, event.getVuesNb()+1);
            ps.setInt(2, event.getIdEvent());
            ps.executeUpdate();
            System.out.println("Event updated successfully");
        }catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void change_promotion(Event event, float percentage) {
        String sql = "UPDATE event set promotionRate = ?,finalPrice = ? where idEvent = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setFloat(1, percentage);
            ps.setFloat(2, event.getPrice()*(percentage/100));
            ps.setInt(3, event.getIdEvent());
            ps.executeUpdate();
            System.out.println("Event updated successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void change_promotionPlus10(Event event) {
        String sql = "UPDATE event set promotionRate = ? finalPrice = ? where idEvent = ?";
        try(PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setFloat(1, event.getPromotionRate()+10);
            ps.setFloat(2, event.getPrice()*((event.getPromotionRate()+10)/100));
            ps.setInt(3, event.getIdEvent());
            ps.executeUpdate();
            System.out.println("Event updated successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
