package nl.tudelft.oopp.demo.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import nl.tudelft.oopp.demo.entities.Building;
import nl.tudelft.oopp.demo.entities.Occasion;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.RoomReservation;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.BuildingRepository;
import nl.tudelft.oopp.demo.repositories.OccasionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.RoomReservationRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.util.UriComponentsBuilder;

@DataJpaTest
class RoomReservationControllerTest {
    private RoomReservation rr1;
    private RoomReservation rr2;
    private RoomReservation rr3;
    private RoomReservation rr4;

    private User u1;
    private User u2;
    private User u3;
    private User u4;

    private Room r1;
    private Room r2;
    private Room r3;
    private Room r4;

    @Mock
    private RoomReservationRepository roomReservationRepository;

    @InjectMocks
    private RoomReservationController roomReservationController;

    @Mock
    private UserRepository users;

    @Mock
    private BuildingRepository buildings;

    @Mock
    private RoomRepository rooms;

    @Mock
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @Mock
    private OccasionRepository occasionRepository;

    /**
     * Creates all roomReservations before each test.
     */


    @BeforeEach
    public void save() {
        u1 = new User("user1@email.com", "student", "fn1", "ln1", "user1");
        u2 = new User("user2@email.com", "student", "fn2", "ln2", "user2");
        u3 = new User("user3@email.com", "student", "fn3", "ln3", "user3");
        u4 = new User("user4@email.com", "employee", "fn4", "ln4", "user4");

        Building b1 = new Building("b1", LocalTime.parse("08:00"), LocalTime.parse("20:00"),"s1", "sNo1", "z1", "c1");
        r1 = new Room("r1", 11, b1);

        Building b2 = new Building("b2", LocalTime.parse("08:00"), LocalTime.parse("20:00"),"s2", "sNo2", "z2", "c1");
        r2 = new Room("r2", 21, b2);

        Building b3 = new Building("b3", LocalTime.parse("08:00"), LocalTime.parse("20:00"),"s1", "sNo3", "z3", "c1");
        r3 = new Room("r3", 31, b3);

        Building b4 = new Building("b4", LocalTime.parse("08:00"), LocalTime.parse("20:00"),"s1", "sNo4", "z4", "c1");
        r4 = new Room("r4", 41, b4);

        rr1 = new RoomReservation(LocalDate.parse("2020-02-01"), r1, LocalTime.parse("12:00"), LocalTime.parse("13:00"), u1);
        rr2 = new RoomReservation(LocalDate.parse("2020-02-02"), r2, LocalTime.parse("12:30"), LocalTime.parse("14:30"), u2);
        rr3 = new RoomReservation(LocalDate.parse("2020-02-03"), r3, LocalTime.parse("13:00"), LocalTime.parse("14:00"), u3);
        rr4 = new RoomReservation(LocalDate.parse("2020-02-04"), r4, LocalTime.parse("12:00"), LocalTime.parse("12:30"), u4);

    }

    /**
     * test if the controllers were loaded correctly and if they are not null.
     * Otherwise, @throws Exception
     */

    @Test
    public void testLoadController() throws Exception {
        assertThat(roomReservationController).isNotNull();
    }

    @Test
    void testGetRoomReservationsByUser() {
        RoomReservation rr5 = new RoomReservation(LocalDate.parse("2000-01-01"), r4, LocalTime.parse("17:00"), LocalTime.parse("18:00"), u1);
        when(roomReservationRepository.findByUserId(u1.getId())).thenReturn(List.of(rr1, rr5));

        when(users.findByUsername(u1.getUsername())).thenReturn(Optional.of(u1));

        Authentication auth = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public String getName() {
                return "user1";
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }


        };

        assertEquals(List.of(rr1, rr5),
            roomReservationController.getRoomReservationsByUser(u1.getId(), auth).getBody());
    }

    @Test
    void testGetRoomReservationsByUserAndRoom() {
        RoomReservation rr5 = new RoomReservation(LocalDate.parse("2020-01-04"), r4, LocalTime.parse("15:00"), LocalTime.parse("16:00"), u1);
        when(roomReservationRepository.findByUserIdAndRoomId(
            u1.getId(), r1.getId())).thenReturn(List.of(rr1));

        when(users.findByUsername(u1.getUsername())).thenReturn(Optional.of(u1));
        assertEquals(List.of(rr1), roomReservationController.getRoomReservationsByUserAndRoom(
            u1.getId(), r1.getId(), new Authentication() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return null;
                }

                @Override
                public Object getCredentials() {
                    return null;
                }

                @Override
                public Object getDetails() {
                    return null;
                }

                @Override
                public Object getPrincipal() {
                    return null;
                }

                @Override
                public boolean isAuthenticated() {
                    return false;
                }

                @Override
                public String getName() {
                    return u1.getUsername();
                }

                @Override
                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

                }


            }).getBody());
    }

    @Test
    public void testGetRoomReservationsTest() {
        List<RoomReservation> expectedList = new ArrayList<RoomReservation>(
            List.of(rr1, rr2, rr3, rr4));
        when(roomReservationRepository.findAll()).thenReturn(expectedList);
        List<RoomReservation> actualList = roomReservationController.getRoomReservationsAll();

        assertEquals(expectedList, actualList);
    }

    @Test
    void testGetRoomReservationById() {
        Optional<RoomReservation> optionalRoomReservation = Optional.of(rr1);
        ResponseEntity<RoomReservation> entity = ResponseEntity.of(optionalRoomReservation);

        when(roomReservationRepository.findById(rr1.getId())).thenReturn(optionalRoomReservation);
        assertEquals(entity, roomReservationController.getRoomReservationById(rr1.getId(), new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public String getName() {
                return "user1";
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }


        }));
    }

    @Test
    void testNewRoomReservation() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

        User u1 = new User("user1@email.com", "student", "fn1", "ln1", "user1");
        Building b1 = new Building("b1", LocalTime.parse("08:00"), LocalTime.parse("20:00"),"s1", "sNo1", "z1", "c1");
        Room r1 = new Room("r1", 11, b1);
        LocalDate date = LocalDate.now().plusDays(1);
        RoomReservation roomReservation = new RoomReservation(
            date, r1, LocalTime.parse("13:00"), LocalTime.parse("14:00"), u1);

        Optional<RoomReservation> optionalRoomReservation = Optional.of(roomReservation);
        ResponseEntity<RoomReservation> responseEntity = ResponseEntity.of(optionalRoomReservation);

        when(roomReservationRepository.save(roomReservation)).thenReturn(roomReservation);
        when(users.findByUsername(u1.getUsername())).thenReturn(Optional.of(u1));
        when(buildings.findById(b1.getId())).thenReturn(Optional.of(b1));
        when(rooms.findById(r1.getId())).thenReturn(Optional.of(r1));
        when(occasionRepository.findByBuildingIdAndDate(b1.getId(), date)).thenReturn(new ArrayList<Occasion>());

        assertEquals(roomReservation, roomReservationController.newRoomReservation(
            roomReservation, uriComponentsBuilder, new Authentication() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return new Collection<GrantedAuthority>() {
                        @Override
                        public int size() {
                            return 0;
                        }

                        @Override
                        public boolean isEmpty() {
                            return false;
                        }

                        @Override
                        public boolean contains(Object o) {
                            return false;
                        }

                        @Override
                        public Iterator<GrantedAuthority> iterator() {
                            return null;
                        }

                        @Override
                        public Object[] toArray() {
                            return new String[]{"ROLE_USER"};
                        }

                        @Override
                        public <T> T[] toArray(T[] ts) {
                            return null;
                        }

                        @Override
                        public boolean add(GrantedAuthority grantedAuthority) {
                            return false;
                        }

                        @Override
                        public boolean remove(Object o) {
                            return false;
                        }

                        @Override
                        public boolean containsAll(Collection<?> collection) {
                            return false;
                        }

                        @Override
                        public boolean addAll(Collection<? extends GrantedAuthority> collection) {
                            return false;
                        }

                        @Override
                        public boolean removeAll(Collection<?> collection) {
                            return false;
                        }

                        @Override
                        public boolean retainAll(Collection<?> collection) {
                            return false;
                        }

                        @Override
                        public void clear() {

                        }
                    };
                }

                @Override
                public Object getCredentials() {
                    return null;
                }

                @Override
                public Object getDetails() {
                    return null;
                }

                @Override
                public Object getPrincipal() {
                    return null;
                }

                @Override
                public boolean isAuthenticated() {
                    return false;
                }

                @Override
                public String getName() {
                    return "user1";
                }

                @Override
                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

                }


            }).getBody());
    }

    @Test
    void testReplaceRoomReservation() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();

        User u1 = new User("user1@email.com", "student", "fn1", "ln1", "user1");
        Building b1 = new Building("b1", LocalTime.parse("08:00"), LocalTime.parse("20:00"),"s1", "sNo1", "z1", "c1");
        Room r1 = new Room("r1", 11, b1);
        RoomReservation roomReservation = new RoomReservation(
            LocalDate.parse("2020-03-22"), r1, LocalTime.parse("13:00"), LocalTime.parse("14:00"), u1);

        Optional<RoomReservation> optionalRoomReservation = Optional.of(rr1);

        ResponseEntity<RoomReservation> entity = ResponseEntity.of(optionalRoomReservation);
        Optional<RoomReservation> newR = Optional.of(roomReservation);
        ResponseEntity<RoomReservation> responseEntity = ResponseEntity.of(newR);

        when(roomReservationRepository.save(roomReservation)).thenReturn(roomReservation);
        when(roomReservationRepository.findById(rr1.getId())).thenReturn(optionalRoomReservation);

        assertEquals(responseEntity.getBody(), roomReservationController.replaceRoomReservation(
            roomReservation, 1, uriComponentsBuilder, new Authentication() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return null;
                }

                @Override
                public Object getCredentials() {
                    return null;
                }

                @Override
                public Object getDetails() {
                    return null;
                }

                @Override
                public Object getPrincipal() {
                    return null;
                }

                @Override
                public boolean isAuthenticated() {
                    return false;
                }

                @Override
                public String getName() {
                    return "user1";
                }

                @Override
                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

                }


            }).getBody());
    }

    @Test
    void testDeleteRoomReservation() {
        List<RoomReservation> actualList = new ArrayList<RoomReservation>(
            List.of(rr1, rr3, rr4));
        List<RoomReservation> expectedList = new ArrayList<RoomReservation>(
            List.of(rr1, rr2, rr3, rr4));

        Optional<RoomReservation> optionalRoomReservation = Optional.of(rr2);
        ResponseEntity<RoomReservation> responseEntity = ResponseEntity.of(optionalRoomReservation);

        roomReservationController.deleteRoomReservation(rr2.getId(), new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public String getName() {
                return "user1";
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }


        });

        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) {
                actualList.remove(1);
                return null;
            }
        }).when(roomReservationRepository).deleteById(rr2.getId());
        when(roomReservationRepository.findAll()).thenReturn(expectedList);

        assertEquals(expectedList, roomReservationController.getRoomReservationsAll());
    }
}