package dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.adapter;

import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.entity.RoleEntity;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.mapper.JpaRoleMapper;
import dev.guilhermesebastiao.authenticationapi.adapter.out.persistence.repository.JpaRoleRepository;
import dev.guilhermesebastiao.authenticationapi.domain.model.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryAdapterTest {

    @Mock
    private JpaRoleRepository repository;

    @Mock
    private JpaRoleMapper mapper;

    @InjectMocks
    private RoleRepositoryAdapter adapter;

    @Test
    @DisplayName("Should return the mapped role when the role name exists")
    void givenExistingEntity_whenFindRoleByName_thenReturnMappedRole() {
        String name = "ROLE_USER";
        RoleEntity entity = new RoleEntity();
        Role role = new Role();

        given(repository.findByName(name))
                .willReturn(Optional.of(entity));

        given(mapper.toDomain(entity))
                .willReturn(role);

        Optional<Role> result = adapter.findRoleByName(name);

        assertTrue(result.isPresent());
        assertSame(role, result.get());
        verify(repository).findByName(name);
        verify(mapper).toDomain(entity);
    }

    @Test
    @DisplayName("Should return an empty optional when the role name does not exist")
    void givenMissingEntity_whenFindRoleByName_thenReturnEmptyOptional() {
        String name = "ROLE_MISSING";

        given(repository.findByName(name))
                .willReturn(Optional.empty());

        Optional<Role> result = adapter.findRoleByName(name);

        assertTrue(result.isEmpty());
        verify(repository).findByName(name);
        verify(mapper, never()).toDomain(any(RoleEntity.class));
    }
}
