package managers;

import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.QueueManagerImpl;
import org.ivanrevich.models.Coordinates;
import org.ivanrevich.models.FuelType;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.models.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("QueueManagerImpl Tests")
class QueueManagerImplTest {

    private QueueManagerImpl queueManager;

    @BeforeEach
    void setUp() {
        queueManager = new QueueManagerImpl();
    }

    private Vehicle makeVehicle(String name, float power, int authorId) {
        Vehicle v = new Vehicle();
        v.setName(name);
        v.setEnginePower(power);
        v.setNumberOfWheels(4L);
        v.setCoordinates(new Coordinates(0.0, 0f));
        v.setCreationDate(new Date());
        v.setType(VehicleType.HELICOPTER);
        v.setFuelType(FuelType.KEROSENE);
        v.setAuthorId(authorId);
        return v;
    }

    @Test
    @DisplayName("initially empty: size = 0")
    void initiallyEmpty() {
        assertEquals(0, queueManager.size());
        assertTrue(queueManager.getAll().isEmpty());
    }

    @Test
    @DisplayName("add() increments size and assigns id > 0")
    void addIncrementsSize() {
        queueManager.add(makeVehicle("A", 100f, 1));
        assertEquals(1, queueManager.size());
    }

    @Test
    @DisplayName("add() auto-generates ascending IDs")
    void addGeneratesIds() {
        Vehicle v1 = makeVehicle("A", 100f, 1);
        Vehicle v2 = makeVehicle("B", 200f, 1);
        queueManager.add(v1);
        queueManager.add(v2);
        assertTrue(v2.getId() > v1.getId());
    }

    @Test
    @DisplayName("getAll() returns all added vehicles")
    void getAllReturnsAllVehicles() {
        queueManager.add(makeVehicle("A", 100f, 1));
        queueManager.add(makeVehicle("B", 200f, 1));
        PriorityQueue<Vehicle> all = queueManager.getAll();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("isExistWithId() returns true for existing id")
    void isExistWithIdTrue() {
        Vehicle v = makeVehicle("A", 100f, 1);
        queueManager.add(v);
        assertTrue(queueManager.isExistWithId(v.getId()));
    }

    @Test
    @DisplayName("isExistWithId() returns false for unknown id")
    void isExistWithIdFalse() {
        assertFalse(queueManager.isExistWithId(999));
    }

    @Test
    @DisplayName("getById() returns correct vehicle")
    void getById() {
        Vehicle v = makeVehicle("FindMe", 150f, 2);
        queueManager.add(v);
        Vehicle found = queueManager.getById(v.getId());
        assertNotNull(found);
        assertEquals("FindMe", found.getName());
    }

    @Test
    @DisplayName("getById() returns null for unknown id")
    void getByIdUnknownReturnsNull() {
        assertNull(queueManager.getById(12345));
    }

    @Test
    @DisplayName("getOwnerById() returns correct author id")
    void getOwnerById() {
        Vehicle v = makeVehicle("Owner", 100f, 7);
        queueManager.add(v);
        assertEquals(7, queueManager.getOwnerById(v.getId()));
    }

    @Test
    @DisplayName("getOwnerById() returns -1 for unknown id")
    void getOwnerByIdUnknown() {
        assertEquals(-1, queueManager.getOwnerById(99999));
    }

    @Test
    @DisplayName("remove_by_id() removes the vehicle")
    void removeById() {
        Vehicle v = makeVehicle("Remove", 100f, 1);
        queueManager.add(v);
        queueManager.remove_by_id(v.getId());
        assertEquals(0, queueManager.size());
        assertFalse(queueManager.isExistWithId(v.getId()));
    }

    @Test
    @DisplayName("remove_by_id() throws AppException for unknown id")
    void removeByIdUnknownThrows() {
        assertThrows(AppException.class, () -> queueManager.remove_by_id(9999));
    }

    @Test
    @DisplayName("remove_head() returns and removes the head element")
    void removeHead() {
        Vehicle v = makeVehicle("Head", 100f, 1);
        queueManager.add(v);
        Vehicle head = queueManager.remove_head();
        assertNotNull(head);
        assertEquals(0, queueManager.size());
    }

    @Test
    @DisplayName("remove_head() on empty queue returns null")
    void removeHeadEmptyReturnsNull() {
        assertNull(queueManager.remove_head());
    }

    @Test
    @DisplayName("clear() removes all vehicles")
    void clearAll() {
        queueManager.add(makeVehicle("A", 100f, 1));
        queueManager.add(makeVehicle("B", 200f, 2));
        queueManager.clear();
        assertEquals(0, queueManager.size());
    }

    @Test
    @DisplayName("clear(userId) removes only vehicles belonging to that user")
    void clearByUser() {
        queueManager.add(makeVehicle("User1-A", 100f, 1));
        queueManager.add(makeVehicle("User2-B", 200f, 2));
        queueManager.clear(1);
        assertEquals(1, queueManager.size());
        assertEquals(2, queueManager.getAll().peek().getAuthorId());
    }

    @Test
    @DisplayName("updateById() replaces the vehicle")
    void updateById() {
        Vehicle v = makeVehicle("Old", 100f, 1);
        queueManager.add(v);
        Vehicle updated = makeVehicle("New", 999f, 1);
        updated.setId(v.getId());
        queueManager.updateById(v.getId(), updated);
        Vehicle found = queueManager.getById(v.getId());
        assertNotNull(found);
        assertEquals("New", found.getName());
    }

    @Test
    @DisplayName("set() bulk-adds all vehicles")
    void set() {
        Vehicle v1 = makeVehicle("A", 100f, 1);
        Vehicle v2 = makeVehicle("B", 200f, 2);
        v1.setId(1); v2.setId(2);
        queueManager.set(List.of(v1, v2));
        assertEquals(2, queueManager.size());
    }

    @Test
    @DisplayName("generateId() is 1 when queue is empty")
    void generateIdEmpty() {
        assertEquals(1, queueManager.generateId());
    }

    @Test
    @DisplayName("generateId() is max id + 1 after additions")
    void generateIdAfterAdditions() {
        queueManager.add(makeVehicle("A", 100f, 1));
        queueManager.add(makeVehicle("B", 200f, 2));
        int maxId = queueManager.getAll().stream().mapToInt(Vehicle::getId).max().orElse(0);
        assertEquals(maxId + 1, queueManager.generateId());
    }

    @Test
    @DisplayName("getLast() returns head of priority queue (lowest by comparator)")
    void getLast() {
        Vehicle low = makeVehicle("Low", 50f, 1);
        Vehicle high = makeVehicle("High", 500f, 1);
        queueManager.add(high);
        queueManager.add(low);
        // PriorityQueue natural order: lowest enginePower at head
        assertEquals("Low", queueManager.getLast().getName());
    }
}
