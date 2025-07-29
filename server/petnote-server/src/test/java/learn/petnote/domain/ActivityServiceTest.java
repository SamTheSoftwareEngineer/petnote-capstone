package learn.petnote.domain;

import learn.petnote.data.ActivityRepository;
import learn.petnote.data.PetRepository;
import learn.petnote.models.Activity;
import learn.petnote.models.ActivityName;
import learn.petnote.models.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ActivityServiceTest {

    @Autowired
    ActivityService service;

    @MockBean
    PetRepository petRepository;

    @MockBean
    ActivityRepository activityRepository;

    private Activity validActivity;
    private Pet ownersPet;

    @BeforeEach
    void setUp() {
        validActivity = new Activity();
        validActivity.setId(0); // new
        validActivity.setActivityDate(LocalDateTime.now().minusHours(1));
        validActivity.setActivityName(ActivityName.WALK);
        validActivity.setPetId(10);
        validActivity.setUserId(100);
        validActivity.setCompleted(true);

        ownersPet = new Pet();
        ownersPet.setId(10);
        ownersPet.setUserId(100);
    }

    // ---------- create ----------

    @Test
    void create_nullActivity_shouldFail() {
        Result<Activity> result = service.create(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getResultType());
        verifyNoInteractions(activityRepository);
    }

    @Test
    void create_missingRequiredFields_shouldFail() {
        Activity a = new Activity(); // missing everything
        Result<Activity> result = service.create(a);

        assertFalse(result.isSuccess());
        assertTrue(result.getResultType().equals(ResultType.INVALID));
        verifyNoInteractions(activityRepository);
    }

    @Test
    void create_petNotFound_shouldFail() {
        when(petRepository.findById(validActivity.getPetId())).thenReturn(null);

        Result<Activity> result = service.create(validActivity);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getResultType());
        verify(activityRepository, never()).create(any());
    }

    @Test
    void create_petOwnedByDifferentUser_shouldFail() {
        Pet othersPet = new Pet();
        othersPet.setId(10);
        othersPet.setUserId(999);

        when(petRepository.findById(validActivity.getPetId())).thenReturn(othersPet);

        Result<Activity> result = service.create(validActivity);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getResultType());
        verify(activityRepository, never()).create(any());
    }

    @Test
    void create_happyPath_shouldSucceed() {
        when(petRepository.findById(validActivity.getPetId())).thenReturn(ownersPet);

        Activity created = copy(validActivity);
        created.setId(55);

        when(activityRepository.create(validActivity)).thenReturn(created);

        Result<Activity> result = service.create(validActivity);

        assertTrue(result.isSuccess());
        assertNotNull(result.getpayload());
        assertEquals(55, result.getpayload().getId());
        verify(activityRepository).create(validActivity);
    }

    // ---------- update ----------

    @Test
    void update_nullActivity_shouldFail() {
        Result<Activity> result = service.update(null);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getResultType());
        verifyNoInteractions(activityRepository);
    }

    @Test
    void update_missingId_shouldFail() {
        Activity toUpdate = copy(validActivity);
        toUpdate.setId(0); // invalid id

        Result<Activity> result = service.update(toUpdate);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getResultType());
        verify(activityRepository, never()).update(any());
    }

    @Test
    void update_notFound_shouldFail() {
        Activity toUpdate = copy(validActivity);
        toUpdate.setId(22);

        when(activityRepository.findById(22)).thenReturn(null);

        Result<Activity> result = service.update(toUpdate);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getResultType());
        verify(activityRepository, never()).update(any());
    }

    @Test
    void update_unauthorizedOwnership_shouldFail() {
        Activity existing = copy(validActivity);
        existing.setId(22);

        Pet othersPet = new Pet();
        othersPet.setId(existing.getPetId());
        othersPet.setUserId(999);

        when(activityRepository.findById(22)).thenReturn(existing);
        when(petRepository.findById(existing.getPetId())).thenReturn(othersPet);

        Activity toUpdate = copy(existing);
        toUpdate.setUserId(validActivity.getUserId()); // request says 100, but pet belongs to 999

        Result<Activity> result = service.update(toUpdate);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getResultType());
        verify(activityRepository, never()).update(any());
    }

    @Test
    void update_happyPath_shouldSucceed() {
        Activity existing = copy(validActivity);
        existing.setId(22);

        when(activityRepository.findById(22)).thenReturn(existing);
        when(petRepository.findById(existing.getPetId())).thenReturn(ownersPet);
        when(activityRepository.update(any(Activity.class))).thenReturn(true);

        Activity toUpdate = copy(existing);
        toUpdate.setActivityName(ActivityName.FEED);
        toUpdate.setUserId(validActivity.getUserId()); // correct owner id

        Result<Activity> result = service.update(toUpdate);

        assertTrue(result.isSuccess());
        assertEquals(ActivityName.FEED, result.getpayload().getActivityName());
        verify(activityRepository).update(any(Activity.class));
    }

    // ---------- delete ----------

    @Test
    void delete_notFound_shouldReturnNotFound() {
        when(activityRepository.findById(999)).thenReturn(null);

        Result<Void> result = service.deleteById(999, 100);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getResultType());
        verify(activityRepository, never()).deleteById(anyInt());
    }

    @Test
    void delete_unauthorized_shouldReturnInvalid() {
        Activity existing = copy(validActivity);
        existing.setId(44);

        Pet othersPet = new Pet();
        othersPet.setId(existing.getPetId());
        othersPet.setUserId(999);

        when(activityRepository.findById(44)).thenReturn(existing);
        when(petRepository.findById(existing.getPetId())).thenReturn(othersPet);

        Result<Void> result = service.deleteById(44, 100);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.INVALID, result.getResultType());
        verify(activityRepository, never()).deleteById(anyInt());
    }

    @Test
    void delete_success_shouldReturnSuccess() {
        Activity existing = copy(validActivity);
        existing.setId(44);

        when(activityRepository.findById(44)).thenReturn(existing);
        when(petRepository.findById(existing.getPetId())).thenReturn(ownersPet);
        when(activityRepository.deleteById(44)).thenReturn(true);

        Result<Void> result = service.deleteById(44, 100);

        assertTrue(result.isSuccess());
        verify(activityRepository).deleteById(44);
    }

    // ---------- helpers ----------

    private Activity copy(Activity src) {
        Activity a = new Activity();
        a.setId(src.getId());
        a.setActivityDate(src.getActivityDate());
        a.setActivityName(src.getActivityName());
        a.setPetId(src.getPetId());
        a.setUserId(src.getUserId());
        a.setCompleted(src.isCompleted());
        return a;
    }

}