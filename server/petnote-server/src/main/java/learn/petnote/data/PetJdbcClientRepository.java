package learn.petnote.data;

import learn.petnote.models.Pet;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetJdbcClientRepository implements PetRepository {

    private final JdbcClient jdbcClient;

    public PetJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Pet create(Pet pet) {
        final String sql = """
                insert into pet (profilePictureURL, petName, breed, species, age, weight, userId)
                values (:profilePictureURL, :petName, :breed, :species, :age, :weight, :userId)
                """;

        KeyHolder keyholder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("profilePictureURL", pet.getProfilePictureURL())
                .param("petName", pet.getPetName())
                .param("breed", pet.getBreed())
                .param("species", pet.getSpecies())
                .param("age", pet.getAge())
                .param("weight", pet.getWeight())
                .param("userId", pet.getUserId())
                .update(keyholder, "id");

        if (rowsAffected == 0) {
            return null;
        }

        pet.setId(keyholder.getKey().intValue());

        int id = keyholder.getKey().intValue();

        return findById(id);
    }

    @Override
    public List<Pet> findByUserId(int userId) {
        final String sql = """
                select * from pet where userId = :userId""";

        return jdbcClient.sql(sql)
                .param("userId", userId)
                .query(new PetMapper())
                .list();
    }

    @Override
    public Pet findById(int id) {
        final String sql = """
                select * from pet where id = :id
                """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(new PetMapper())
                .optional().orElse(null);
    }

    @Override
    public boolean update(Pet pet) {
        final String sql = """
            UPDATE pet
            SET profilePictureURL = :profilePictureURL,
                petName = :petName,
                breed = :breed,
                species = :species,
                age = :age,
                weight = :weight
            WHERE id = :id AND userId = :userId
        """;

        int rows = jdbcClient.sql(sql)
                .param("profilePictureURL", pet.getProfilePictureURL())
                .param("petName", pet.getPetName())
                .param("breed", pet.getBreed())
                .param("species", pet.getSpecies())
                .param("age", pet.getAge())
                .param("weight", pet.getWeight())
                .param("id", pet.getId())
                .param("userId", pet.getUserId())
                .update();

        return rows > 0;
    }

    @Override
    public boolean deleteById(int id, int userId) {
        final String sql = "DELETE FROM pet WHERE id = :id AND userId = :userId";
        int rows = jdbcClient.sql(sql)
                .param("id", id)
                .param("userId", userId)
                .update();
        return rows > 0;

    }
}
