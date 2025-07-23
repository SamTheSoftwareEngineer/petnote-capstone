package learn.petnote.data;

import learn.petnote.models.Pet;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PetMapper implements RowMapper<Pet>{
    @Override
    public Pet mapRow(ResultSet rs, int rowNum) throws SQLException {
        Pet pet = new Pet();
        pet.setId(rs.getInt("id"));
        pet.setProfilePictureURL(rs.getString("profilePictureURL"));
        pet.setPetName(rs.getString("petName"));
        pet.setBreed(rs.getString("breed"));
        pet.setSpecies(rs.getString("species"));
        pet.setAge(rs.getInt("age"));
        pet.setWeight(rs.getFloat("weight"));
        pet.setUserId(rs.getInt("userId"));
        pet.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        return pet;
    }
}
