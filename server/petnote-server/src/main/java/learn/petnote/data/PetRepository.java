package learn.petnote.data;

import learn.petnote.models.Pet;

import java.util.List;

public interface PetRepository {
    Pet create(Pet pet);
    List<Pet> findByUserId(int userId);
    Pet findById(int id);
    boolean update(Pet pet);
    boolean deleteById(int id);
}
