package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Time;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Room_Reservation")
public class RoomReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private Date date;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id", nullable = false)
    private Room room;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Time startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Time endTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public RoomReservation() {}

    /**
     * Create a new RoomReservation instance.
     *
     * @param id A unique identifier of the RoomReservation.
     * @param date Te date for which the Room is reserved.
     * @param room The room that is reserved.
     * @param startTime The start time of the RoomReservation.
     * @param endTime The end time of the RoomReservation.
     * @param user The user that has reserved the room.
     */

    public RoomReservation(long id,
                           Date date,
                           Room room,
                           Time startTime,
                           Time endTime,
                           User user) {
        this.id = id;
        this.date = date;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoomReservation that = (RoomReservation) o;
        return id == that.id
                && Objects.equals(date, that.date)
                && Objects.equals(room, that.room)
                && Objects.equals(startTime, that.startTime)
                && Objects.equals(endTime, that.endTime)
                && Objects.equals(user, that.user);
    }
}