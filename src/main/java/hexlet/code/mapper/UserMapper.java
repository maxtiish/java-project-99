package hexlet.code.mapper;


import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import org.mapstruct.BeforeMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Mapping(target = "passwordDigest", source = "password")
    public abstract User map(UserCreateDTO dto);
    public abstract UserDTO map(User model);
    @Mapping(target = "passwordDigest", ignore = true)
    public abstract void update(UserUpdateDTO dto, @MappingTarget User model);

    @BeforeMapping
    public void encryptPasswordUpdate(UserUpdateDTO dto, @MappingTarget User user) {
        var password = dto.getPassword();
        if (password != null && password.isPresent()) {
            user.setPasswordDigest(passwordEncoder.encode(password.get()));
        }
    }

    @BeforeMapping
    public void encryptPasswordCreate(UserCreateDTO dto) {
        var password = dto.getPassword();
        dto.setPassword(passwordEncoder.encode(password));
    }
}
