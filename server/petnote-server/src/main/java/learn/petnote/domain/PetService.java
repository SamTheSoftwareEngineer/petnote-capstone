package learn.petnote.domain;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import learn.petnote.data.PetRepository;
import learn.petnote.models.Pet;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class PetService {

    PetRepository repository;

    public PetService(PetRepository repository) {
        this.repository = repository;
    }

    public Result<Pet> createPet(Pet pet) {
        Result<Pet> result = new Result<>();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Pet> violation : violations){
                result.addErrorMessage(violation.getMessage(), ResultType.INVALID);
            }
            return result;
        }

        // If no errors, save the pet

        if (result.isSuccess()) {
            System.out.println("Creating pet...");
            Pet created = repository.create(pet);

            if (created == null) {
                result.addErrorMessage("Failed to create pet", ResultType.INVALID);
                return result;
            }

            System.out.println("Successfully created pet!");
            result.setpayload(created);
        }

        return result;
    }

    public Result<Pet> updatePet(Pet pet) {
        Result<Pet> result = new Result<>();

        if (pet == null || pet.getId() <= 0) {
            result.addErrorMessage("Pet ID is required for update", ResultType.INVALID);
            return result;
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Pet>> violations = validator.validate(pet);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Pet> violation : violations) {
                result.addErrorMessage(violation.getMessage(), ResultType.INVALID);
            }
            return result;
        }

        boolean success = repository.update(pet);
        if (!success) {
            result.addErrorMessage("Pet not found or update failed", ResultType.NOT_FOUND);
            return result;
        }

        result.setpayload(pet);
        return result;
    }

    public Result<Boolean> deletePetById(int id) {
        Result<Boolean> result = new Result<>();

        if (id <= 0) {
            result.addErrorMessage("Invalid pet ID", ResultType.INVALID);
            return result;
        }

        boolean success = repository.deleteById(id);
        if (!success) {
            result.addErrorMessage("Pet not found or delete failed", ResultType.NOT_FOUND);
        }

        return result;
    }

    public Pet findById(int id) {
        if (id <= 0) {
            return null;
        }
        return repository.findById(id);
    }

    public List<Pet> findByUserId(int userId) {
        if (userId <= 0) {
            return Collections.emptyList();
        }
        return repository.findByUserId(userId);
    }


}
