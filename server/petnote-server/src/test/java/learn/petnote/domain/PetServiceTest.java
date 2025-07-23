package learn.petnote.domain;

import learn.petnote.data.PetRepository;
import learn.petnote.models.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PetServiceTest {

    @Autowired
    PetService service;

    @MockBean
    PetRepository repository;

    @Test
    void createPetSuccess() {
        Pet toCreate = new Pet();
        toCreate.setPetName("Fido");
        toCreate.setSpecies("Dog");
        toCreate.setUserId(1);

        Pet created = new Pet();
        created.setId(42);
        created.setPetName("Fido");
        created.setSpecies("Dog");
        created.setUserId(1);

        when(repository.create(toCreate)).thenReturn(created);

        Result<Pet> result = service.createPet(toCreate);

        assertTrue(result.isSuccess());
        assertEquals(created, result.getpayload());
    }

    @Test
    void createPetValidationFails() {
        Pet invalidPet = new Pet();  // Missing required fields like petName

        Result<Pet> result = service.createPet(invalidPet);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().stream()
                .anyMatch(msg -> msg.toLowerCase().contains("pet name")));
    }

    @Test
    void updatePetMissingId() {
        Pet pet = new Pet();
        pet.setPetName("Fluffy");

        Result<Pet> result = service.updatePet(pet);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("Pet ID is required for update"));
    }

    @Test
    void updatePetSuccess() {
        Pet pet = new Pet();
        pet.setId(5);
        pet.setPetName("Fluffy");

        when(repository.update(pet)).thenReturn(true);

        Result<Pet> result = service.updatePet(pet);

        assertTrue(result.isSuccess());
        assertEquals(pet, result.getpayload());
    }

    @Test
    void updatePetNotFound() {
        Pet pet = new Pet();
        pet.setId(99);
        pet.setPetName("Ghost");

        when(repository.update(pet)).thenReturn(false);

        Result<Pet> result = service.updatePet(pet);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("Pet not found or update failed"));
    }

    @Test
    void deleteByIdSuccess() {
        int idToDelete = 10;
        when(repository.deleteById(idToDelete)).thenReturn(true);

        Result<Boolean> result = service.deletePetById(idToDelete);

        assertTrue(result.isSuccess());
        assertNull(result.getpayload());
    }

    @Test
    void deleteByIdFail() {
        int idToDelete = 99;
        when(repository.deleteById(idToDelete)).thenReturn(false);

        Result<Boolean> result = service.deletePetById(idToDelete);

        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("Pet not found or delete failed"));
    }

    @Test
    void findByIdSuccess() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setPetName("Buddy");

        when(repository.findById(1)).thenReturn(pet);

        Pet found = service.findById(1);

        assertNotNull(found);
        assertEquals(pet, found);
    }

    @Test
    void findByIdNotFound() {
        when(repository.findById(42)).thenReturn(null);

        Pet found = service.findById(42);

        assertNull(found);
    }
}