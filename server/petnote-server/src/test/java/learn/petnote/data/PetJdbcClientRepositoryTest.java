package learn.petnote.data;

import learn.petnote.models.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PetJdbcClientRepositoryTest {

    @Autowired
    private PetJdbcClientRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Test
    void createHappyPath() {
        Pet toCreate = makePet("Loki");
        Pet result = repository.create(toCreate);

        assertNotNull(result);
        assertTrue(result.getId() > 0);
        assertEquals("Loki", result.getPetName());
    }

    @Test
    void findByUserId() {
        // Assume userId = 1 has at least 1 pet seeded
        List<Pet> pets = repository.findByUserId(1);
        assertNotNull(pets);
        assertFalse(pets.isEmpty());
    }

    @Test
    void findById() {
        // Add a pet first so we know the ID
        Pet pet = repository.create(makePet("Buddy"));
        Pet found = repository.findById(pet.getId());

        assertNotNull(found);
        assertEquals("Buddy", found.getPetName());
    }

    @Test
    void update() {
        Pet pet = repository.create(makePet("Pepper"));
        pet.setWeight(30.0f);
        pet.setAge(5);

        boolean updated = repository.update(pet);
        assertTrue(updated);

        Pet reloaded = repository.findById(pet.getId());
        assertEquals(30.0f, reloaded.getWeight());
        assertEquals(5, reloaded.getAge());
    }

    @Test
    void deleteById() {
        Pet pet = repository.create(makePet("Ghost"));
        boolean deleted = repository.deleteById(pet.getId(), pet.getUserId());
        assertTrue(deleted);

        Pet shouldBeGone = repository.findById(pet.getId());
        assertNull(shouldBeGone);
    }

    @Test
    void updateNonExistentPet() {
        Pet fakePet = makePet("NonExistent");
        fakePet.setId(99999); // Assuming this ID does not exist
        boolean updated = repository.update(fakePet);
        assertFalse(updated, "Should not update a pet that doesn't exist");
    }

    @Test
    void findByIdNonExistent() {
        Pet pet = repository.findById(99999); // Non-existent ID
        assertNull(pet, "Should return null for non-existent pet ID");
    }

    @Test
    void deleteByIdNonExistent() {
        boolean deleted = repository.deleteById(99999, 1); // Non-existent ID
        assertFalse(deleted, "Should not delete anything for non-existent pet ID");
    }

    @Test
    void findByUserIdNoPets() {
        // Assuming userId 99999 has no pets
        List<Pet> pets = repository.findByUserId(99999);
        assertNotNull(pets, "Should return empty list, not null");
        assertTrue(pets.isEmpty(), "Should return an empty list if no pets found");
    }



    private Pet makePet(String name) {
        Pet pet = new Pet();
        pet.setPetName(name);
        pet.setSpecies("Dog");
        pet.setBreed("Terrier");
        pet.setAge(2);
        pet.setWeight(12.5f);
        pet.setUserId(1);
        pet.setProfilePictureURL("https://example.com/pet.jpg");
        return pet;
    }
}
