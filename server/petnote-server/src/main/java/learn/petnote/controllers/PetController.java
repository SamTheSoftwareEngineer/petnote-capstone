package learn.petnote.controllers;

import learn.petnote.domain.CloudinaryService;
import learn.petnote.domain.PetService;
import learn.petnote.domain.Result;
import learn.petnote.models.Pet;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pet")
@CrossOrigin(origins = {"http://localhost:5173"})
public class PetController {

    private final PetService service;
    private final CloudinaryService cloudinaryService;

    public PetController(PetService service, CloudinaryService cloudinaryService) {
        this.service = service;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Pet pet) {
        Result<Pet> result = service.createPet(pet);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getpayload(), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        Pet pet = service.findById(id);

        if (pet != null) {
            return ResponseEntity.ok(pet);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Pet>> findByUserId(@PathVariable int userId) {
        List<Pet> pets = service.findByUserId(userId);
        return ResponseEntity.ok(pets);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Pet> updatePet(
            @PathVariable int id,
            @RequestParam("petName") String petName,
            @RequestParam(value = "breed", required = false) String breed,
            @RequestParam(value = "species", required = false) String species,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam(value = "weight", required = false) Float weight,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) {

        Pet existingPet = service.findById(id);
        if (existingPet == null) {
            return ResponseEntity.notFound().build();
        }

        String imageUrl = existingPet.getProfilePictureURL(); // keep existing image
        try {
            if (profilePicture != null && !profilePicture.isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(profilePicture); // new upload
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        existingPet.setPetName(petName);
        existingPet.setBreed(breed);
        existingPet.setSpecies(species);
        existingPet.setAge(age);
        existingPet.setWeight(weight);
        existingPet.setProfilePictureURL(imageUrl);

        Pet updated = service.updatePet(existingPet).getpayload();
        return ResponseEntity.ok(updated);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable int id) {
        Result<Boolean> result = service.deletePetById(id);

        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPet(
            @RequestParam String petName,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String species,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) Float weight,
            @RequestParam int userId,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture) {

        String imageUrl = null;
        try {
            if (profilePicture != null && !profilePicture.isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(profilePicture);
            }
        } catch (IOException e) {
            // log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Image upload failed.");
        }

        Pet pet = new Pet(
                0,
                imageUrl,
                petName,
                breed,
                species,
                age != null ? age : 0,
                weight != null ? weight : 0f,
                userId,
                null
        );

        Result<Pet> result = service.createPet(pet);

        if (!result.isSuccess()) {
            return ResponseEntity
                    .badRequest()
                    .body(result.getErrorMessages());
        }

        Pet created = result.getpayload();
        URI location = URI.create("/api/pets/" + created.getId());

        return ResponseEntity
                .created(location)
                .body(created);
    }
}


